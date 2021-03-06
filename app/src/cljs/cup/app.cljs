(ns cup.app
  (:require [clojure.set :as cs]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def cities {
  "A Coruña" [43.362344,-8.41154]
  "Aarau" [47.390434,8.045701]
  "Allegheny, Pennsylvania" [40.445081,-80.008775]
  "Avignon" [43.949317,4.805528]
  "Baltimore, Maryland" [39.290385,-76.612189]
  "Barcelona" [41.385064,2.173403]
  "Berlin" [52.520007,13.404954]
  "Bern" [46.947974,7.447447]
  "Bordeaux" [44.837789,-0.57918]
  "Cambridge, Massachusetts" [42.373616,-71.109733]
  "Chicago" [41.878114,-87.629798]
  "Dublin" [53.349805,-6.26031]
  "Figueres" [42.265507,2.958105]
  "Fossalta di Piave" [45.646751,12.513665]
  "Havana" [23.05407,-82.345189]
  "Kansas City, Missouri" [39.099727,-94.578567]
  "Ketchum, Idaho" [43.68074,-114.363662]
  "Key West" [24.555059,-81.779987]
  "Kinshasa" [-4.441931,15.266293]
  "Leipzig" [51.339696,12.373075]
  "London" [51.507351,-0.127758]
  "Los Angeles" [34.07362,-118.400356]
  "Madrid" [40.416775,-3.70379]
  "Milan" [45.465422,9.185924]
  "Munich" [48.135125,11.581981]
  "Málaga" [36.721261,-4.421266]
  "New York" [40.712784,-74.005941]
  "Oak Park, Illinois" [41.885032,-87.784503]
  "Oakland, California" [37.804364,-122.271114]
  "Pamplona" [42.812526,-1.645775]
  "Paris" [48.856614,2.352222]
  "Pavia" [45.184725,9.158207]
  "Port Lligat" [42.296083,3.288227]
  "Prague" [50.075538,14.4378]
  "Princeton" [40.357298,-74.667223]
  "Pula" [44.866623,13.849579]
  "Púbol" [42.014498,2.982868]
  "Příbor" [49.640936,18.144996]
  "Rome" [41.902784,12.496366]
  "Schruns" [47.080072,9.919807]
  "Toronto" [43.653226,-79.383184]
  "Trieste" [45.649526,13.776818]
  "Ulm" [48.401082,9.987608]
  "Vienna" [48.208174,16.373819]
  "Zurich" [47.376887,8.541694]
  })

(def people-data '(
  ; {:name "Mahatma Gandhi"
  ;  :link "https://en.wikipedia.org/wiki/Mahatma_Gandhi"
  ;  :avatar "img/gandhi.png"
  ;  :color "#FF9800"
  ;  :locations {
  ;   "1888-1891" "London"
  ;   }}
  {:name "Gertrude Stein"
   :link "https://en.wikipedia.org/wiki/Gertrude_Stein"
   :avatar "img/stein.png"
   :color "#E040FB"
   :locations {
    "1874-1877" "Allegheny, Pennsylvania"
    "1877-1877" "Vienna"
    "1877-1878" "Paris"
    "1878-1892" "Oakland, California"
    "1892-1893" "Baltimore, Maryland"
    "1893-1897" "Cambridge, Massachusetts"
    "1897-1902" "Baltimore, Maryland"
    "1902-1903" "London"
    "1903-1934" "Paris"
    "1934-1934" "New York"
    "1934-1935" "Los Angeles"
    "1935-1946" "Paris"
    }}
  {:name "Ernest Hemingway"
   :link "https://en.wikipedia.org/wiki/Ernest_Hemingway"
   :avatar "img/hemingway.png"
   :color "#8BC34A"
   :locations {
    "1899-1917" "Oak Park, Illinois"
    "1917-1918" "Kansas City, Missouri"
    "1918-1918" "Fossalta di Piave"
    "1918-1919" "Milan"
    "1919-1920" "Oak Park, Illinois"
    "1920-1921" "Chicago"
    "1921-1923" "Paris"
    "1923-1923" "Pamplona"
    "1923-1924" "Toronto"
    "1924-1924" "Pamplona"
    "1924-1925" "Paris"
    "1925-1925" "Pamplona"
    "1925-1926" "Schruns"
    "1926-1928" "Paris"
    "1928-1929" "Key West"
    "1929-1930" "Paris"
    "1930-1937" "Key West"
    "1937-1939" "Madrid"
    "1939-1940" "Havana"
    "1940-1941" "Ketchum, Idaho"
    "1941-1945" "London"
    "1945-1953" "Havana" ; probably 1954
    "1954-1954" "Kinshasa"
    "1954-1956" "Havana"
    "1956-1957" "Paris"
    "1957-1959" "Havana"
    "1959-1959" "Pamplona"
    "1959-1960" "Havana"
    "1960-1961" "Ketchum, Idaho"
    }}
  {:name "Albert Einstein"
   :link "https://en.wikipedia.org/wiki/Albert_Einstein"
   :avatar "img/einstein.png"
   :color "#FF4081"
   :locations {
    "1879-1880" "Ulm"
    "1880-1894" "Munich"
    "1894-1895" "Pavia"
    "1895-1896" "Aarau"
    "1896-1900" "Zurich"
    "1900-1905" "Bern"
    "1905-1914" "Zurich"
    "1911-1912" "Prague"
    "1912-1914" "Zurich"
    "1914-1933" "Berlin"
    "1933-1955" "Princeton"
   }}
 {:name "James Joyce"
  :link "https://en.wikipedia.org/wiki/James_Joyce"
  :avatar "img/joyce.png"
  :color "#FFC107"
  :locations {
   "1882-1904" "Dublin"
   "1904-1904" "Zurich"
   "1904-1905" "Pula"
   "1905-1906" "Trieste"
   "1906-1907" "Rome"
   "1907-1909" "Trieste"
   "1909-1910" "Dublin"
   "1910-1915" "Trieste"
   "1915-1920" "Zurich"
   "1920-1931" "Paris"
   "1931-1931" "London"
   "1931-1940" "Paris"
   "1940-1941" "Zurich"
  }}
  {:name "Salvador Dalí"
   :link "https://en.wikipedia.org/wiki/Salvador_Dal%C3%AD"
   :avatar "img/dali.png"
   :color "#00BCD4"
   :locations {
    "1904-1922" "Figueres"
    "1922-1926" "Madrid"
    "1926-1934" "Paris"
    "1934-1934" "New York"
    "1934-1936" "Port Lligat"
    "1936-1936" "London"
    "1936-1938" "New York"
    ;"1936-1936" "New York"
    ;"1936-1938" "Port Lligat"
    "1938-1938" "London"
    ;"1938-1938" "Roquebrune-Cap-Martin"
    "1938-1939" "Paris"
    "1939-1939" "New York"
    "1939-1940" "Bordeaux"
    "1940-1948" "New York"
    "1948-1962" "Port Lligat"
    "1962-1962" "New York"
    "1962-1982" "Port Lligat"
    "1982-1988" "Púbol"
    "1988-1989" "Figueres"
    }}
  {:name "Sigmund Freud"
   :link "https://en.wikipedia.org/wiki/Sigmund_Freud"
   :avatar "img/freud.png"
   :color "#4CAF50"
   :locations {
    "1855-1859" "Příbor"
    "1859-1860" "Leipzig"
    "1860-1938" "Vienna"
    "1938-1939" "London"
    }}
  {:name "Pablo Picasso"
   :link "https://en.wikipedia.org/wiki/Pablo_Picasso"
   :avatar "img/picasso.png"
   :color "#FFEB3B"
   :locations {
    "1881-1891" "Málaga"
    "1891-1895" "A Coruña"
    "1895-1897" "Barcelona"
    "1897-1900" "Madrid"
    "1900-1901" "Paris"
    "1901-1901" "Madrid"
    "1901-1914" "Paris"
    "1914-1918" "Avignon"
    "1918-1973" "Paris"
    }}
   ))

(defn expand-location [location-item]
  (let [years-str (first location-item)
        location (second location-item)
        years (clojure.string/split years-str #"-")
        start-year (int (first years))
        ; NOTE: we do inc here because of range implementation
        end-year (inc (int (last years)))
        years (range start-year end-year)]
    (doall
      (map
        (fn [year]
          [year location])
        years))))

(defn sort-set [c]
  (sort (set c)))


(defn flatten-one-level [coll]
  (mapcat  #(if (sequential? %) % [%]) coll))

(defn find-first [f coll]
  (first (filter f coll)))

(defn position [x coll & {:keys [from-end all] :or {from-end false all false}}]
  (let [all-idxs (keep-indexed (fn [idx val] (when (= val x) idx)) coll)]
  (cond
   (true? from-end) (last all-idxs)
   (true? all)      all-idxs
   :else            (first all-idxs))))

(defn find-all-cities [people]
  (sort-set (flatten (map #(vals (:locations %)) people))))

(defn expand-locations [locations]
  (flatten-one-level
    (map expand-location locations)))


(def expanded-people-data
  (doall
    (map
      (fn [person]
        (let [locations (:locations person)
              expanded (sort-by first (expand-locations locations))
              byear (->> expanded first first)
              dyear (->> expanded last first)]
              ;(println "locations for person" (:name person) expanded)
              ;(println "selected byear for" (:name person) byear expanded)
              ;(println "selected dyear for" (:name person) dyear)
              (assoc person
                :locations expanded
                :byear byear
                :dyear dyear
                )))
      people-data)))

(defn find-location-for-year [year locations]
  (first
    (filter
      (fn [location]
        (= (int year) (-> location first int)))
      locations)))

(defn people-by-year [people-data year]
  (remove
    (fn [person]
      (nil? (:year person)))
    (map
      (fn [person]
        (let [locations (:locations person)
              ; TODO: for now support one location for one person per year
              location-for-year (find-location-for-year year locations)]
              (if (nil? location-for-year)
                (do
                  ;(println (:name person) "had no location in" year)
                  person)
                (do
                  ;(println (:name person) "was in" (second location-for-year) "in" year)
                  (assoc person :year (first location-for-year)
                                :location (second location-for-year))))))
      people-data)))


(def marker-offsets
  [
    "left: -4px;"
    "left: -4px; top: -4px;"
    "top: -4px;"
    "top: -4px; right: -4px;"
    "right: -4px;"
    "right: -4px; bottom: -4px;"
    "bottom: -4px;"
    "bottom: -4px; left: -4px;"
    ;
    "left: -2px;"
    "left: -2px; top: -2px;"
    "top: -2px;"
    "top: -2px; right: -2px;"
    "right: -2px;"
    "right: -2px; bottom: -2px;"
    "bottom: -2px;"
    "bottom: -2px; left: -2px;"
    ;
    "left: -3px;"
    "left: -3px; top: -3px;"
    "top: -3px;"
    "top: -3px; right: -3px;"
    "right: -3px;"
    "right: -3px; bottom: -3px;"
    "bottom: -3px;"
    "bottom: -3px; left: -3px;"
  ])

(defn create-person-popup [coordinates person]
  (let [lng-lat (reverse coordinates)
        offset (rand-nth marker-offsets)
        name (:name person)
        color (:color person)
        avatar (:avatar person)
        person-popup (js/mapboxgl.Popup. (js-obj "closeOnClick" false "closeButton" false))
        ;(str "<img width='40px' src='" avatar "'>")
        html (str
          "<div class='person open' style='border-color:"
          color
          "'>"
          "<img class='avatar' src='" avatar "'>"
          "<div class='data'>"
          "<span class='name'>" name "</span>"
          "<span class='location'>" (:location person) "</span>"
          "</div>"
          "</div>"
          "<div class='person-marker' "
          " data-name='" name "' "
          "style='background-color:" color ";"
          offset
          "'>"
          "</div>")]
        (do
          (if (nil? (first lng-lat))
            (println "not found geo data")
            (.setLngLat person-popup (clj->js lng-lat)))
          (.setHTML person-popup html)
          person-popup
          )))

(defn render-person [person]
  (let [coordinates (get cities (:location person))
        ui-control (create-person-popup coordinates person)]
    {:name (:name person)
     :location (:location person)
     :control ui-control}))


(defn add-all-to-map [items app-map]
  (doall
    (map
      (fn [item]
        (.addTo (:control item) app-map))
      items)))

(defn remove-all-from-map [items]
  (doall
    (map
      (fn [item]
        (println "removing item" (:name item))
        (.remove (:control item)))
      items)))


(defonce state
  (atom {
      :map nil
      :curr-year 1914
      :prev-year nil
      :curr-people nil
      :prev-people nil
  }))


(defn create-people-controls [people]
  (doall (map render-person people)))

(defn render-people-for-year [people year]
  (println "render people for year" (count people) year)
  (let [app-map (:map @state)
        people-to-popups (create-people-controls people)]
    (do
      (add-all-to-map people-to-popups app-map)
      people-to-popups)))

(defn render-for-year [year]
  (println "render for year" year)
  (let [app-map (:map @state)
        people (people-by-year expanded-people-data year)]
      (render-people-for-year people year)))

(defn filter-people-by-names [people names]
  (filter
    (fn [person]
      (some #(= (:name person) %) names))
    people))


(defn name-loc-key [person]
  (str (:name person) "<<>>" (:location person)))


(defn name-from-key [name-loc-key]
  (first (clojure.string/split name-loc-key "<<>>")))

(defn names-from-keys [keys]
  (map name-from-key keys))

(defn render-people [new-year]
  (println "render people on the map for" new-year)
  (if-let [prev-year (:prev-year @state)]
    ; do something when prev year exists
    (do
      (println "previous data found. more comple logic is coming")
      (let [people (people-by-year expanded-people-data new-year)
            prev-people (:prev-people @state)
            prev-name-loc-keys (set (map name-loc-key prev-people))
            new-name-loc-keys (set (map name-loc-key people))
            keys-to-delete (cs/difference prev-name-loc-keys new-name-loc-keys)
            keys-to-create (cs/difference new-name-loc-keys prev-name-loc-keys)
            keys-to-remain (cs/intersection new-name-loc-keys prev-name-loc-keys)
            names-to-delete (names-from-keys keys-to-delete)
            names-to-create (names-from-keys keys-to-create)
            names-to-remain (names-from-keys keys-to-remain)
            people-to-popups-to-remove (filter-people-by-names prev-people names-to-delete)
            people-to-create (filter-people-by-names people names-to-create)
            created-people-to-popups (render-people-for-year people-to-create new-year)
            remained-people-to-popups (filter-people-by-names prev-people names-to-remain)
            new-curr-people (concat remained-people-to-popups created-people-to-popups)]
        (println "people for the prev year" prev-name-loc-keys prev-year)
        (println "people for the new year" new-name-loc-keys new-year)
        (println "names to delete" names-to-delete (count people-to-popups-to-remove))
        (println "names to remain" names-to-remain (count remained-people-to-popups))
        (println "names to create" names-to-create  (count created-people-to-popups))
        (println "new current people size" (count new-curr-people))
        (remove-all-from-map people-to-popups-to-remove)
        (swap! state assoc
                :curr-year new-year
                :curr-people new-curr-people)
      )
      )
    ; first-time render
    (let [people-to-popups (render-for-year new-year)]
      (swap! state assoc
              :curr-year new-year
              :curr-people people-to-popups))))

(defn year-change-handler [e]
  (println "attached listener event")
  (let [$current-year (sel1 :.current-year)
        new-year (dommy/value (sel1 :#years))
        curr-year (:curr-year @state)
        curr-people (:curr-people @state)]
    (println "year-changed from" curr-year "to" new-year state)
    (println "changed prev year and prev people" (count curr-people))
    (println "transaction. before" (:curr-year @state) (:prev-year @state) (map :name (:prev-people @state)))
    (swap! state assoc
            :curr-year new-year
            :prev-year curr-year
            :curr-people []
            :prev-people curr-people)
    (println "transaction. after" (:curr-year @state) (:prev-year @state) (map :name (:prev-people @state)))
    (dommy/set-html! $current-year new-year)
    (render-people new-year)))

(defn create-map []
  (aset js/mapboxgl "accessToken" "pk.eyJ1IjoiaGFzaG9iamVjdCIsImEiOiJjaWh0ZWU4MjkwMTdsdGxtMWIzZ3hnbnVqIn0.RQjfkzc1hI2UuR0vzjMtJQ")
  (let [props (js-obj "container" "map"
                      "zoom" 2
                      "center" (clj->js [-21.496366 35.902784])
                      "style" "mapbox://styles/mapbox/streets-v8")
        app-map (js/mapboxgl.Map. props)]
    ; save map into state atom
    (swap! state assoc :map app-map)
    ; save map into windown
    (aset js/window "appMap" app-map)))


(defn resolve-people [data]
  (let [curr-people-names (map :name (:curr-people data))
        people
          (map
            (fn [name]
              (find-first #(= name (:name %)) expanded-people-data))
            curr-people-names)]
        people))


(defn people-widget [data owner]
  (reify
    om/IRender
    (render [this]
      (let [people (resolve-people data)
            sorted-people (sort-by :byear people)]
        (apply dom/ul nil
          (map
            (fn [person]
              (dom/li #js {:className "person" :style #js {:borderColor (:color person)}}
                (dom/img #js{:className "avatar" :src (:avatar person)})
                (dom/div #js {:className "data"}
                  (dom/span #js {:className "name"} (:name person)))
                )
              )
            sorted-people)
          )))))

(create-map)
(render-people (:curr-year @state))

(defn find-all-byears [people]
  (sort-set(map :byear people)))

(defn find-all-dyears [people]
  (sort-set (map :dyear people)))

(defn find-all-touch-years [people]
  (sort
    (cs/union
      (find-all-byears people)
      (find-all-dyears people))))

(defn render-person-lifeline [person all-years year-width]
  (let [index (position (:byear person) all-years)
        offset (* index year-width)
        life-duration (- (:dyear person) (:byear person))
        width (* life-duration year-width)
        html (str "<li><div style='left:"
                  offset "px;"
                  "background-color:" (:color person)
                  ";height: 2px;margin-bottom: 2px;"
                  "width:" width "px;"
                  "'></div></li>")]
        html))

(defn render-people-lifelines [people all-years year-width]
  (let [items-html
          (map
            #(render-person-lifeline % all-years year-width)
            people)
        html-str (clojure.string/join "" items-html)]
        html-str))

(defn render-years [rendered-years all-years year-width]
  (let [labels-html
          (map
            (fn [year]
              (let [index (position year all-years)
                    offset (* index year-width)]
              (str "<li style='left:"  offset "px'>" year "</li>")))
            rendered-years)
        html-str (clojure.string/join "" labels-html)]
        html-str))

(defn render-timeline []
  (let [$range-labels (sel1 :.years-labels)
        $lifelines (sel1 :.lifelines)
        $range (sel1 :#years)
        $range-width (-> $range dommy/bounding-client-rect :width)
        touch-years (find-all-touch-years expanded-people-data)
        min-year (first touch-years)
        max-year (last touch-years)
        all-years (sort-set (range min-year (inc max-year)))
        partition-size
          (cond
            (< $range-width 401) 20
            (< $range-width 600) 10
            (< $range-width 1000) 6
            :else 5
            )
        rendered-years (map first (partition-all partition-size all-years))
        years-count (- max-year min-year)
        year-width (/ $range-width years-count)
        labels-html (render-years rendered-years all-years year-width)
        ; NOTE: render lifelines for all people
        ;current-people (resolve-people @state)
        lifelines-html (render-people-lifelines expanded-people-data all-years year-width)]
    (dommy/set-attr! $range :min min-year)
    (dommy/set-attr! $range :max max-year)
    (dommy/set-html! $range-labels labels-html)
    (dommy/set-html! $lifelines lifelines-html)
  ))
;(dommy/unlisten! (sel1 :#years) :change year-change-handler)


(println "exec")

(println "all cities>>" (clojure.string/join "\n" (find-all-cities people-data)))

;(println "byears>>>" (sort-set (map :byear expanded-people-data)))
;(println "dyears>>>" (sort-set (map :dyear expanded-people-data)))


(render-timeline)
(defn init []
  (enable-console-print!)
  (println "init")
  (dommy/listen! (sel1 :#years) :change year-change-handler)
  (om/root people-widget
          state
          {:target (sel1 :.people-bar)}))
