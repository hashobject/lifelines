(ns cup.styles
  (:require [garden.def :refer [defrule defstyles]]
            [garden.stylesheet :refer [rule]]))

(defstyles screen
  (let [body (rule :body)]
    [
      [:div.mapboxgl-popup-tip {
        :display "none"
        }]
      [:div.mapboxgl-popup {
        :z-index 1
      }]
      [:div.mapboxgl-popup-content {
        :padding 0
        :background "inherit"
        :box-shadow "none"
        }
        [:div.person {
          :position "absolute"
          :top "-56px"
          :left "-18px"
          :display "none"
          }
        ]
        [:&:hover
          [:div.person {
              :display "block"
            }]
        ]
      ]
    ]))