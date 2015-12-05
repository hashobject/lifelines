(ns cup.app
  (:require [dommy.core :as dommy :refer-macros [sel sel1]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def cities {
  "Paris" [2.352222 48.856614]
  "London" [-0.127758 51.507351]
  "Zurich" [8.541694 47.376887]
  "Bern" [7.447447 46.947974]
  "Berlin" [13.404954 52.520007]
  "Princeton" [-74.667223 40.357298]
  "Trieste" [13.776818 45.649526]
  "Rome" [12.496366 41.902784]
  "Vienna" [16.373819 48.208174]
  })

(def people-data '(
  {:name "Mahatma Gandhi"
   :link "https://en.wikipedia.org/wiki/Mahatma_Gandhi#English_barrister"
   :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Portrait_Gandhi.jpg/400px-Portrait_Gandhi.jpg"
   :locations {
    "1888-1891" "London"}}
  {:name "Ernest Hemingway"
   :link "https://en.wikipedia.org/wiki/Ernest_Hemingway"
   :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ernest_Hemingway_1923_passport_photo.jpg/440px-Ernest_Hemingway_1923_passport_photo.jpg"
   :locations {
    "1921-1928" "Paris"
    "1944-1945" "London"}}
  {:name "Albert Einstein"
   :link "https://en.wikipedia.org/wiki/Albert_Einstein"
   :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Einstein_patentoffice.jpg/340px-Einstein_patentoffice.jpg"
   :locations {
    "1896-1900" "Zurich"
    "1900-1904" "Bern"
    "1905-1914" "Zurich"
    "1914-1921" "Berlin"
    "1933-1955" "Princeton"
   }}
 {:name "James Joyce"
  :link "https://en.wikipedia.org/wiki/James_Joyce"
  :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Revolutionary_Joyce_Better_Contrast.jpg/440px-Revolutionary_Joyce_Better_Contrast.jpg"
  :locations {
   "1905-1906" "Trieste"
   "1906-1907" "Rome"
   "1907-1909" "Trieste"
   "1915-1920" "Zurich"
   "1920-1940" "Paris"
   "1931-1931" "London"
   "1941-1941" "Zurich"
  }}
  {:name "Salvador Dalí"
   :link "https://en.wikipedia.org/wiki/Salvador_Dal%C3%AD"
   :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Salvador_Dal%C3%AD_1939.jpg/440px-Salvador_Dal%C3%AD_1939.jpg"
   :locations {
    "1925-1929" "Paris"
   }}
  {:name "Sigmund Freud"
   :link "https://en.wikipedia.org/wiki/Sigmund_Freud#Escape_from_Nazism"
   :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/Sigmund_Freud_LIFE.jpg/400px-Sigmund_Freud_LIFE.jpg"
   :locations {
     "1881-1938" "Vienna"
     "1938-1939" "London"
     }}
   ))

(defn expand-location [location-item]
  (let [years-str (first location-item)
        location (second location-item)
        years (clojure.string/split years-str #"-")
        start-year (int (first years))
        end-year (int (last years))
        years (range start-year end-year)]
    (doall
      (map
        (fn [year]
          [year location])
        years))))

(defn flatten-one-level [coll]
  (mapcat  #(if (sequential? %) % [%]) coll))

(defn expand-locations [locations]
  (flatten-one-level
    (map expand-location locations)))


(def expanded-people-data
  (doall
    (map
      (fn [person]
        (let [locations (:locations person)
              expanded (expand-locations locations)]
              (assoc person :locations expanded)))
      people-data)))

(defn people-by-year [people-data year]
  (remove
    (fn [person]
      (nil? (:year person)))
    (map
      (fn [person]
        (let [locations (:locations person)
              location-for-year
                ; TODO: for now support one location for one person per year
                (first
                  (filter
                    (fn [location]
                      (= year (first location)))
                    locations))]
              (assoc person :year (first location-for-year)
                            :location (second location-for-year))))
      people-data)))


(defn create-person-popup [coordinates avatar]
  (let [person-popup (js/mapboxgl.Popup. (js-obj "closeOnClick" false "closeButton" false))
        html (str "<img width='40px' src='" avatar "'>")]
        (do
          (.setLngLat person-popup (clj->js coordinates))
          (.setHTML person-popup html)
          person-popup
          )))

(defn render-person [person]
  (let [coordinates (get cities (:location person))]
    (create-person-popup coordinates (:avatar person))))

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/h1 nil (:text data)))))

(defn year-change-handler [e]
  (.log js/console "You clicked my button! Congratulations" (dommy/value (sel1 :#years))))

(defn add-all-to-map [items app-map]
  (doall
    (map
      (fn [item]
        (.addTo item app-map))
      items)))

(defn remove-all-to-map [items]
  (map
    (fn [item]
      (.remove item))
    items))

(defn render-for-year [year app-map]
  (let [people (people-by-year expanded-people-data 1905)
        ui-popups (doall (map render-person people))]
      (do
        (js/console.log "people by year" (first ui-popups) (-> people count) year)
        (add-all-to-map ui-popups app-map))))

(defn init []
  (dommy/listen! (sel1 :#years) :change year-change-handler)

  (aset js/mapboxgl "accessToken" "pk.eyJ1IjoiaGFzaG9iamVjdCIsImEiOiJjaWh0ZWU4MjkwMTdsdGxtMWIzZ3hnbnVqIn0.RQjfkzc1hI2UuR0vzjMtJQ")
  (let [props (js-obj "container" "map"
                      "zoom" 1
                      "center" (clj->js [12.496366 41.902784])
                      "style" "mapbox://styles/mapbox/streets-v8")
        app-map (js/mapboxgl.Map. props)]
    (render-for-year 1925 app-map)
    (aset js/window "appMap" app-map)

    (om/root widget
            {:text ""}
            {:target (. js/document (getElementById "container"))})))
