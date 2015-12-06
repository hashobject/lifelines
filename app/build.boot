(set-env!
 :source-paths    #{"src/cljs" "src/clj"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.48-6"   :scope "test"]
                 [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.1"      :scope "test"]
                 [pandeiro/boot-http        "0.6.3"      :scope "test"]
                 [org.clojure/clojurescript "1.7.122"]
                 [org.omcljs/om "0.8.6"]
                 [prismatic/dommy "1.1.0"]
                 [confetti "0.1.0-SNAPSHOT"]
                 [hashobject/boot-s3 "0.1.2-SNAPSHOT"]
                 [org.martinklepsch/boot-garden "1.2.5-3" :scope "test"]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[confetti.boot-confetti :refer [create-site sync-bucket]]
 '[hashobject.boot-s3 :refer :all]
 '[org.martinklepsch.boot-garden :refer [garden]])


 (task-options!
   s3-sync {
       :bucket "lifelines.io"
       :access-key (System/getenv "AWS_ACCESS_KEY")
       :secret-key (System/getenv "AWS_SECRET_KEY")
       :source "/"
       :options {"Cache-Control" "max-age=315360000, no-transform, public"}}
   create-site {:creds {
     :access-key (System/getenv "AWS_ACCESS_KEY")
     :secret-key (System/getenv "AWS_SECRET_KEY")}}
   sync-bucket {
     ; :dry-run true
     :bucket "code-hashobject-com-confetti-static-si-sitebucket-1d00u2838ns0u"
     :creds {
       :access-key (System/getenv "AWS_ACCESS_KEY")
       :secret-key (System/getenv "AWS_SECRET_KEY")}
   })
(deftask build []
  (comp
        (cljs)
        (garden :styles-var 'cup.styles/screen
:output-to "css/garden.css")))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced}
                      garden {:pretty-print false})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none :source-map true}
                 reload {:on-jsload 'cup.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))
