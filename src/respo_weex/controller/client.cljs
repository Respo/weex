
(ns respo-weex.controller.client
  (:require [respo-weex.render.patcher :refer [apply-dom-changes]]
            [respo-weex.polyfill :refer [read-string*]]
            [respo-weex.util.format :refer [event->string event->edn]]
            [respo-weex.render.make-dom :refer [make-element]]
            [respo-weex.util.information :refer [bubble-events no-bubble-events]]))

(defn read-events [target] (read-string* (aget (.-attr target) "event")))

(defn read-coord [target] (read-string* (aget (.-attr target) "coord")))

(defn maybe-trigger [target event-name simple-event deliver-event]
  (let [coord (read-coord target), active-events (read-events target)]
    (comment .log js/console coord active-events event-name)
    (if (contains? active-events event-name)
      (deliver-event coord event-name simple-event)
      (if (> (count coord) 1)
        (recur (.-parentElement target) event-name simple-event deliver-event)))))

(defonce dom-registry (atom {}))

(defn release-instance [mount-point]
  (set! (.-innerHTML mount-point) "")
  (doall
   (->> (:listeners (get @dom-registry mount-point))
        (map
         (fn [entry]
           (let [event-string (name (key entry)), listener (key entry)]
             (.removeEventListener mount-point event-string listener))))))
  (swap! dom-registry dissoc mount-point))

(defn build-listener [event-name deliver-event]
  (fn [event]
    (let [target (.-target event)
          coord (read-coord target)
          active-events (read-events target)
          simple-event (event->edn event)]
      (maybe-trigger target event-name simple-event deliver-event))))

(defn activate-instance [entire-dom mount-point deliver-event]
  (let [no-bubble-collection (->> no-bubble-events
                                  (map
                                   (fn [event-name]
                                     [event-name (build-listener event-name deliver-event)]))
                                  (into {}))]
    (set! (.-innerHTML mount-point) "")
    (.appendChild mount-point (make-element entire-dom no-bubble-collection))))

(defn initialize-instance [mount-point deliver-event]
  (let [bubble-collection (->> bubble-events
                               (map
                                (fn [event-name]
                                  [event-name (build-listener event-name deliver-event)]))
                               (into {}))]
    (doall
     (->> bubble-collection
          (map
           (fn [entry]
             (let [event-string (name (key entry)), listener (val entry)]
               (.addEvent mount-point event-string listener))))))
    (swap! dom-registry assoc mount-point {:listeners bubble-collection})))

(defn patch-instance [changes mount-point deliver-event]
  (let [no-bubble-collection (->> no-bubble-events
                                  (map
                                   (fn [event-name]
                                     [event-name (build-listener event-name deliver-event)]))
                                  (into {}))]
    (apply-dom-changes changes mount-point no-bubble-collection)))
