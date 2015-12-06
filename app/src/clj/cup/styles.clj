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
      [:div.person-marker {
        :display "block"
        :width "16px"
        :height "16px"
        :border-radius "8px"
        :padding 0
        }]
      [:img.avatar {
        :width "30px"
        :border-radius "15px"
      }]
      [:div#years-control {
        :width "100%"
        :position "relative"
      }]
      [:input#years {
        :width "100%"
      }]
      [:ol#years-labels {
        :list-style "none"
        }
        [:li {
          :display "inline-block"
          :position "absolute"
          }
        ]
      ]
      [:ol#lifelines {
        :list-style "none"
        }
        [:li {
          :display "block"
          :position "relative"
          }
        ]
      ]
      [:div#people {
        :display "block"
        :z-index 1
        :position "relative"
        }
        [:ul {
          :list-style "none"
          }]
        ]
    ]))
