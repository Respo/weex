
(ns respo-weex.main
  (:require [respo-weex.core :refer [render! clear-cache! gc-states!]]
            [respo-weex.schema :as schema]
            [respo-weex.updater.core :refer [updater]]
            [respo-weex.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]))

(defonce id-ref (atom 0))

(defn id! [] (swap! id-ref inc) @id-ref)

(defonce global-store (atom schema/store))

(defn dispatch! [op op-data]
  (let [op-id (id!), new-store (updater @global-store op op-data op-id)]
    (reset! global-store new-store)))

(defonce global-states (atom {}))

(defn render-app! [target]
  (render! (comp-container @global-store @global-states) target dispatch! global-states))

(defn -main []
  (enable-console-print!)
  (let [target (document.createElement "div" {})]
    (document.documentElement.appendChild target)
    (render-app! target)
    (add-watch global-store :gc (fn [[]] (gc-states! global-states)))
    (add-watch global-store :renderer (fn [] (render-app! target)))
    (add-watch
     global-states
     :renderer
     (fn [] (println "State changes:" @global-states) (render-app! target)))))

(-main)
