
(ns respo-weex.render.make-dom
  (:require [clojure.string :as string]
            [respo-weex.util.format :refer [dashed->camel event->prop ensure-string]]
            [respo-weex.polyfill :refer [document-create-element*]]))

(defn make-element [virtual-element no-bubble-collection]
  (let [tag-name (name (:name virtual-element))
        attrs (:attrs virtual-element)
        style (:style virtual-element)
        children (:children virtual-element)
        element (document-create-element* tag-name)
        child-elements (->> children
                            (map
                             (fn [entry] (make-element (last entry) no-bubble-collection))))
        event-keys (into #{} (keys (:event virtual-element)))]
    (.call (aget element "setAttr") element "coord" (pr-str (:coord virtual-element)))
    (.call (aget element "setAttr") element "event" (pr-str event-keys))
    (doall
     (->> attrs
          (map
           (fn [entry]
             (let [k (dashed->camel (name (first entry))), v (last entry)]
               (.call (aget element "setAttr") element k v))))))
    (.call
     (aget element "setClassStyle")
     element
     (let [result (->> style
                       (map (fn [entry] (let [[k v] entry] [(dashed->camel (name k)) v])))
                       (into {})
                       (clj->js))]
       result))
    (doall
     (->> (:event virtual-element)
          (map
           (fn [entry]
             (let [event-name (key entry)
                   name-in-string (name event-name)
                   maybe-listener (get no-bubble-collection event-name)]
               (comment println "Add listener:" element name-in-string maybe-listener)
               (if (some? maybe-listener)
                 (.call
                  (aget element "addEvent")
                  element
                  name-in-string
                  (fn [event] (maybe-listener event (:coord virtual-element))))))))))
    (doseq [child-element child-elements] (.appendChild element child-element))
    element))
