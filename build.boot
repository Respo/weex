
(set-env!
  :resource-paths #{"polyfill"}
  :dependencies '[[org.clojure/clojure       "1.8.0"       :scope "test"]
                  [org.clojure/clojurescript "1.9.293"     :scope "test"]
                  [adzerk/boot-cljs          "1.7.228-1"   :scope "test"]
                  [cirru/boot-stack-server   "0.1.24"      :scope "test"]
                  [mvc-works/hsl             "0.1.2"]
                  [respo/ui                  "0.1.6"]
                  [respo                     "0.3.34"]])

(require '[adzerk.boot-cljs   :refer [cljs]]
         '[stack-server.core  :refer [start-stack-editor! transform-stack]]
         '[respo.alias        :refer [html head title script style meta' div link body]]
         '[respo.render.html  :refer [make-html]]
         '[clojure.java.io    :as    io])

(def +version+ "0.1.0")

(task-options!
  pom {:project     'Respo/weex
       :version     +version+
       :description "Workflow"
       :url         "https://github.com/mvc-works/respo-weex"
       :scm         {:url "https://github.com/mvc-works/respo-weex"}
       :license     {"MIT" "http://opensource.org/licenses/mit-license.php"}})

(deftask editor! []
  (comp
    (wait)
    (start-stack-editor!)
    (target :dir #{"src/"})))

(deftask dev! []
  (set-env!
    :asset-paths #{"assets/"})
  (comp
    (editor!)
    (cljs :optimizations :simple
          :compiler-options {:language-in :ecmascript5})
    (target)))

(deftask generate-code []
  (comp
    (transform-stack :filename "stack-sepal.ir")
    (target :dir #{"src/"})))

(deftask build-simple []
  (set-env!
    :asset-paths #{"assets/"})
  (comp
    (transform-stack :filename "stack-sepal.ir")
    (cljs :optimizations :simple
          :compiler-options {:language-in :ecmascript5
                             :parallel-build true
                             :pseudo-names true
                             :static-fns true
                             :source-map true})
    (target)))

(deftask build-advanced []
  (set-env!
    :asset-paths #{"assets/"})
  (comp
    (transform-stack :filename "stack-sepal.ir")
    (cljs :optimizations :advanced
          :compiler-options {:language-in :ecmascript5
                             :pseudo-names true
                             :static-fns true
                             :parallel-build true
                             :optimize-constants true
                             :source-map true})
    (target)))

(deftask rsync []
  (with-pre-wrap fileset
    (sh "rsync" "-r" "target/" "repo.tiye.me:repo/Respo/weex" "--exclude" "main.out" "--delete")
    fileset))

(deftask build []
  (comp
    (transform-stack :filename "stack-sepal.ir")
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
