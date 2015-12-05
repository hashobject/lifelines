(ns cup.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def people
  '({:name "Ernest Hemingway" :year "1930" :location "Paris" :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Ernest_Hemingway_1923_passport_photo.jpg/440px-Ernest_Hemingway_1923_passport_photo.jpg"}
    {:name "Pablo Picasso" :year "1930" :location "Paris" :avatar "https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/Pablo_Picasso%2C_1904%2C_Paris%2C_photograph_by_Ricard_Canals_i_Llamb%C3%AD.jpg/440px-Pablo_Picasso%2C_1904%2C_Paris%2C_photograph_by_Ricard_Canals_i_Llamb%C3%AD.jpg"}))

(defn create-person-popup [lng lat map avatar]
  (let [person-popup (js/mapboxgl.Popup. (js-obj "closeOnClick" false "closeButton" false))
        html (str "<img width='40px' src='" avatar "'>")]
        (do
          (.setLngLat person-popup (clj->js [lng lat]))
          (.setHTML person-popup html)
          (.addTo person-popup map)
          )))

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
          (create-person-popup -96 37 app-map (:avatar person))
        )
        people))
    (aset js/window "appMap" app-map)

    (om/root widget
            {:text "Hello world3!"}
            {:target (. js/document (getElementById "container"))})))
