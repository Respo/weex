
(ns respo-weex.util.format
  (:require [clojure.string :as string] [respo-weex.util.detect :refer [component? element?]]))

(defn prop->attr [x] (case x "class-name" "class" x))

(defn event->prop [x] (str "on" (name x)))

(defn ensure-string [x] (cond (string? x) x (keyword? x) (name x) :else (str x)))

(defn event->string [x] (subs (name x) 3))

(defn dashed->camel
  ([x] (dashed->camel "" x false))
  ([acc piece promoted?]
   (if (= piece "")
     acc
     (let [cursor (get piece 0), piece-followed (subs piece 1)]
       (if (= cursor "-")
         (recur acc piece-followed true)
         (recur
          (str acc (if promoted? (string/upper-case cursor) cursor))
          piece-followed
          false))))))

(defn purify-events [events] (->> events (map (fn [entry] [(key entry) true])) (into {})))

(defn event->edn [event]
  (.log js/console "simplify event:" event)
  (-> (case (.-type event)
        "click" {:type :click}
        "keydown" {:key-code (.-keyCode event), :type :keydown}
        "keyup" {:key-code (.-keyCode event), :type :keyup}
        "input" {:value (aget event "value"), :type :input}
        "change" {:value (aget event "value"), :type :change}
        "focus" {:type :focus}
        {:msg (str "Unhandled event: " (.-type event)), :type (.-type event)})
      (assoc :original-event event)))

(defn mute-element [element]
  (if (component? element)
    (update element :tree mute-element)
    (-> element
        (update :event (fn [events] (list)))
        (update
         :children
         (fn [children]
           (->> children (map (fn [entry] [(first entry) (mute-element (last entry))]))))))))

(defn purify-element [markup]
  (if (nil? markup)
    nil
    (if (component? markup)
      (recur (:tree markup))
      (into
       {}
       (-> markup
           (update :event purify-events)
           (update
            :children
            (fn [children]
              (->> children (map (fn [entry] [(first entry) (purify-element (last entry))]))))))))))
