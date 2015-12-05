(ns cup.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def cities {
  "Paris" [2.352222 48.856614]
  "London" [-0.127758 51.507351]})

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
