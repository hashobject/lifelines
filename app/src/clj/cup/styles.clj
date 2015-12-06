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
      [:.overlay {
        :z-index 1
      }]
      [:.people-bar {
        :display "block"
        :z-index 2
        :max-width "200px"
        }
        [:ul {
          :list-style "none"
          }
          [:li {
            :display "block"
            }]
          ]
        ]
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
          :top "-55px"
          :left "-30px"
          :display "none"
          }
        ]
        [:&:hover
          [:div.person {
              :display "block"
            }]
        ]
      ]
      [:div.person-marker {
        :display "block"
        :width "16px"
        :height "16px"
        :border-radius "8px"
        :padding 0
        :position "relative"
        }
      ]
      [:img.avatar {
        :width "30px"
        :border-radius "15px"
      }]
      [:div.bottom-bar {
        ; :width "100%"
        ; :position "absolute"
        :z-index 2
      }]
      ; [:input#years {
      ;   :width "100%"
      ; }]
      ; [:ol#years-labels {
      ;   :list-style "none"
      ;   }
      ;   [:li {
      ;     :display "inline-block"
      ;     :position "absolute"
      ;     }
      ;   ]
      ; ]
      ; [:ol#lifelines {
      ;   :list-style "none"
      ;   }
      ;   [:li {
      ;     :display "block"
      ;     :position "relative"
      ;     }
      ;   ]
      ; ]
    ]))
