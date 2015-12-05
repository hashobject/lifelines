(ns cup.styles
  (:require [garden.def :refer [defrule defstyles]]
            [garden.stylesheet :refer [rule]]))

(defstyles screen
  (let [body (rule :body)]
    [
      (body
       {:font-family "Helvetica Neue"
        :font-size   "16px"
        :line-height 1.5})
      [:div.mapboxgl-popup-tip {
        :display "none"
        }]
      [:div.mapboxgl-popup-content {
        :padding 0
        :background "inherit"
        :box-shadow "none"
        }]
      [:div.mapboxgl-popup-content :img {
        :border-radius "20px"
        }]
    ]))
