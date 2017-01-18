
(ns respo-weex.render.make-dom
  (:require [clojure.string :as string]
            [respo-weex.util.format :refer [dashed->camel event->prop ensure-string]]
            [respo-weex.polyfill :refer [document-create-element*]]))

(defn style->string [styles]
  (string/join
   ""
   (->> styles
        (map
         (fn [entry]
           (let [k (first entry), v (ensure-string (last entry))] (str (name k) ":" v ";")))))))

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
    (aset (.-dataset element) "coord" (pr-str (:coord virtual-element)))
    (aset (.-dataset element) "event" (pr-str event-keys))
    (doall
     (->> attrs
          (map
           (fn [entry]
             (let [k (dashed->camel (name (first entry))), v (last entry)]
               (.setAttribute element k v)
               (aset element k v))))))
    (.setAttribute element "style" (style->string style))
    (doall
     (->> (:event virtual-element)
          (map
           (fn [entry]
             (comment println "Looking into event:" entry)
             (let [event-name (key entry)
                   name-in-string (event->prop event-name)
                   maybe-listener (get no-bubble-collection event-name)]
               (comment println "listener:" event-name maybe-listener name-in-string)
               (if (some? maybe-listener) (aset element name-in-string maybe-listener)))))))
    (doseq [child-element child-elements] (.appendChild element child-element))
    element))
