
(set-env!
  :asset-paths #{"assets/"}
  :resource-paths #{"polyfill" "src"}
  :dependencies '[[org.clojure/clojure       "1.8.0"       :scope "test"]
                  [org.clojure/clojurescript "1.9.293"     :scope "test"]
                  [adzerk/boot-cljs          "1.7.228-1"   :scope "test"]
                  [andare                    "0.4.0"       :scope "test"]
                  [cirru/boot-stack-server   "0.1.30"      :scope "test"]
                  [cumulo/shallow-diff       "0.1.2"       :scope "test"]
                  [fipp                      "0.6.9"       :scope "test"]
                  [mvc-works/hsl             "0.1.2"]
                  [respo/ui                  "0.1.6"]
                  [respo                     "0.3.37"]])

(require '[adzerk.boot-cljs   :refer [cljs]])

(def +version+ "0.1.0")

(task-options!
  pom {:project     'Respo/weex
       :version     +version+
       :description "Workflow"
       :url         "https://github.com/mvc-works/respo-weex"
       :scm         {:url "https://github.com/mvc-works/respo-weex"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(deftask build-simple []
  (comp
    (watch)
    (cljs :optimizations :simple
          :compiler-options {:language-in :ecmascript5
                             :parallel-build true
                             :pseudo-names true
                             :source-map true})
    (target)))

(deftask build-advanced []
  (comp
    (cljs :optimizations :advanced
          :compiler-options {:language-in :ecmascript5
                             :parallel-build true
                             :optimize-constants true
                             :source-map false})
    (target)))

(deftask build []
  (comp
    (pom)
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env!
    :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
