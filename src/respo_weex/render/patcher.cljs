
(ns respo-weex.render.patcher
  (:require [clojure.string :as string]
            [respo-weex.polyfill :refer [read-string*]]
            [respo-weex.util.format :refer [dashed->camel event->prop ensure-string]]
            [respo-weex.render.make-dom :refer [make-element style->string]]
            [respo-weex.util.information :refer [no-bubble-events]]))

(defn is-no-bubble? [event-name] (some? (some (fn [x] (= x event-name)) no-bubble-events)))

(defn rm-event [target event-name]
  (let [event-prop (event->prop event-name)
        existing-events (read-string* (aget (.-dataset target) "event"))
        new-events-list (pr-str (disj existing-events event-name))]
    (if (is-no-bubble? event-name) (aset target event-prop nil))
    (aset (.-dataset target) "event" new-events-list)))

(defn replace-style [target op]
  (let [style-name (dashed->camel (name (key op))), style-value (ensure-string (val op))]
    (aset (.-style target) style-name style-value)))

(defn replace-element [target op no-bubble-collection]
  (let [new-element (make-element op no-bubble-collection)
        parent-element (.-parentElement target)]
    (.insertBefore parent-element new-element target)
    (.remove target)))

(defn append-element [target op no-bubble-collection]
  (let [new-element (make-element op no-bubble-collection)] (.appendChild target new-element)))

(defn add-event [target event-name no-bubble-collection]
  (let [event-prop (event->prop event-name)
        existing-events (read-string* (aget (.-dataset target) "event"))
        new-events-list (pr-str (conj existing-events event-name))
        maybe-listener (get no-bubble-collection event-name)]
    (if (some? maybe-listener) (aset target event-prop maybe-listener))
    (aset (.-dataset target) "event" new-events-list)))

(defn rm-prop [target op] (aset target (dashed->camel (name op)) nil))

(defn add-prop [target op]
  (let [prop-name (dashed->camel (name (key op))), prop-value (val op)]
    (case prop-name
      "style" (aset target prop-name (style->string prop-value))
      (aset target prop-name prop-value))))

(defn replace-prop [target op]
  (let [prop-name (dashed->camel (name (key op))), prop-value (val op)]
    (if (= prop-name "value")
      (if (not= prop-value (.-value target)) (aset target prop-name prop-value))
      (aset target prop-name prop-value))))

(defn add-style [target op]
  (let [style-name (dashed->camel (name (key op))), style-value (ensure-string (val op))]
    (aset (.-style target) style-name style-value)))

(defn rm-style [target op]
  (let [style-name (dashed->camel (name op))] (aset (.-style target) style-name nil)))

(defn rm-element [target op] (.remove target))

(defn find-target [root coord]
  (if (empty? coord)
    root
    (let [index (first coord), child (aget (.-children root) index)]
      (recur child (rest coord)))))

(defn add-element [target op no-bubble-collection]
  (let [new-element (make-element op no-bubble-collection)
        parent-element (.-parentElement target)]
    (.insertBefore parent-element new-element target)))

(defn apply-dom-changes [changes mount-point no-bubble-collection]
  (let [root (.-firstChild mount-point)]
    (doall
     (->> changes
          (map
           (fn [op]
             (let [op-type (first op)
                   coord (get op 1)
                   op-data (get op 2)
                   target (find-target root coord)]
               (comment println op-type target op-data)
               (case op-type
                 :replace-prop (replace-prop target op-data)
                 :add-prop (add-prop target op-data)
                 :rm-prop (rm-prop target op-data)
                 :add-style (add-style target op-data)
                 :replace-style (replace-style target op-data)
                 :rm-style (rm-style target op-data)
                 :add-event (add-event target op-data no-bubble-collection)
                 :rm-event (rm-event target op-data)
                 :add (add-element target op-data no-bubble-collection)
                 :rm (rm-element target op-data)
                 :replace (replace-element target op-data no-bubble-collection)
                 :append (append-element target op-data no-bubble-collection)
                 (println "not implemented:" op-type)))))))))
