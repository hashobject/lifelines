(ns cup.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def cities {
  "Paris" [2.352222 48.856614]
  "London" [-0.127758 51.507351]})

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
   :avatar "https://en.wikipedia.org/wiki/File:Salvador_Dal%C3%AD_1939.jpg"
   :locations {
    "1925-1929" "Paris"
   }}))

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
              (assoc person :locs expanded)))
      people-data)))


(def people
  '({:name "Ernest Hemingway" :year "1930" :location "Paris" :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ernest_Hemingway_1923_passport_photo.jpg/440px-Ernest_Hemingway_1923_passport_photo.jpg"}
    {:name "Pablo Picasso" :year "1930" :location "Paris" :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/Pablo_Picasso%2C_1904%2C_Paris%2C_photograph_by_Ricard_Canals_i_Llamb%C3%AD.jpg/440px-Pablo_Picasso%2C_1904%2C_Paris%2C_photograph_by_Ricard_Canals_i_Llamb%C3%AD.jpg"}
    {:name "Gandhi" :year "1914" :location "London" :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Portrait_Gandhi.jpg/400px-Portrait_Gandhi.jpg"}))

(defn create-person-popup [coordinates avatar map]
  (let [person-popup (js/mapboxgl.Popup. (js-obj "closeOnClick" false "closeButton" false))
        html (str "<img width='40px' src='" avatar "'>")]
        (do
          (.setLngLat person-popup (clj->js coordinates))
          (.setHTML person-popup html)
          (.addTo person-popup map)
          )))

(defn render-person [person map]
  (let [coordinates (get cities (:location person))]
    (create-person-popup coordinates (:avatar person) map)))

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/h1 nil (:text data)))))

(defn init []
  (aset js/mapboxgl "accessToken" "pk.eyJ1IjoiaGFzaG9iamVjdCIsImEiOiJjaWh0ZWU4MjkwMTdsdGxtMWIzZ3hnbnVqIn0.RQjfkzc1hI2UuR0vzjMtJQ")
  (let [props (js-obj "container" "map"
                      "zoom" 0
                      "style" "mapbox://styles/mapbox/streets-v8")
        app-map (js/mapboxgl.Map. props)]
    (doall
      (map
        (fn [person]
          (render-person person app-map)
        )
        people))
    (aset js/window "appMap" app-map)

    (om/root widget
            {:text "Hello world3!"}
            {:target (. js/document (getElementById "container"))})))
