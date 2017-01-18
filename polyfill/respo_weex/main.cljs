
(ns respo-weex.main
  (:require [respo-weex.core :refer [render! clear-cache! gc-states!]]
            [respo-weex.schema :as schema]
            [respo-weex.updater.core :refer [updater]]
            [respo-weex.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]))

(defonce global-store (atom schema/store))

(defonce global-states (atom {}))

(defonce id-ref (atom 0))

(defn id! []
  (swap! id-ref inc)
  @id-ref)

(defn dispatch! [op op-data]
  (let [op-id (id!)
        new-store (updater @global-store op op-data op-id)]
    (reset! global-store new-store)))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (comment println "store:" @global-store)
    (comment println "states:" @global-states)
    (render!
      (comp-container @global-store @global-states)
      target
      dispatch!
      global-states)))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (add-watch global-store :gc (fn [] (gc-states! global-states)))
  (add-watch global-store :rerender render-app!)
  (add-watch global-states :rerender render-app!))

(set! (.-onload js/window) -main)

(defn save-store! []
  (.setItem js/localStorage "respo" (pr-str @global-store)))

(set! (.-onbeforeunload js/window) save-store!)

(defn on-jsload []
  (clear-cache!)
  (render-app!)
  (.log js/console "code updated."))
