
(ns respo-weex.controller.client
  (:require [respo-weex.render.patcher :refer [apply-dom-changes]]
            [respo-weex.polyfill :refer [read-string*]]
            [respo-weex.util.format :refer [event->string event->edn]]
            [respo-weex.render.make-dom :refer [make-element]]
            [respo-weex.util.information :refer [no-bubble-events]]))

(defn maybe-trigger [event-name simple-event deliver-event coord]
  (comment .log js/console coord event-name)
  (deliver-event coord event-name simple-event))

(defn read-events [target] (read-string* (aget (.-attr target) "event")))

(defn build-listener [event-name deliver-event]
  (fn [event coord]
    (let [simple-event (event->edn event)]
      (maybe-trigger event-name simple-event deliver-event coord))))

(defn activate-instance [entire-dom mount-point deliver-event]
  (let [no-bubble-collection (->> no-bubble-events
                                  (map
                                   (fn [event-name]
                                     [event-name (build-listener event-name deliver-event)]))
                                  (into {}))]
    (.appendChild mount-point (make-element entire-dom no-bubble-collection))))

(defn patch-instance [changes mount-point deliver-event]
  (let [no-bubble-collection (->> no-bubble-events
                                  (map
                                   (fn [event-name]
                                     [event-name (build-listener event-name deliver-event)]))
                                  (into {}))]
    (apply-dom-changes changes mount-point no-bubble-collection)))
