
(ns respo-weex.controller.deliver
  (:require [respo-weex.controller.resolver
             :refer
             [find-event-target get-markup-at get-component-at]]
            [respo-weex.util.detect :refer [component? element?]]))

(defn build-deliver-event [element-ref dispatch!]
  (fn [coord event-name simple-event]
    (let [target-element (find-event-target @element-ref coord event-name)
          target-component (get-component-at @element-ref coord)
          target-listener (get (:event target-element) event-name)]
      (if (some? target-listener)
        (do
         (comment println "listener found:" coord event-name)
         (target-listener simple-event dispatch!))
        (comment println "found no listener:" coord event-name)))))

(defn mutate-factory [global-element global-states]
  (fn [coord]
    (fn [& state-args]
      (println "Calling mutate!" coord state-args)
      (let [component (get-markup-at @global-element (subvec coord 0 (- (count coord) 1)))
            init-state (:init-state component)
            update-state (:update-state component)
            state-path (conj coord 'data)
            old-state (let [inner-states (get-in @global-states coord)]
                        (if (contains? inner-states 'data)
                          (get inner-states 'data)
                          (apply init-state (:args component))))
            new-state (apply update-state (cons old-state state-args))]
        (comment
         println
         "compare states:"
         (pr-str @global-states)
         state-path
         (pr-str old-state)
         (pr-str new-state))
        (swap! global-states assoc-in (conj coord 'data) new-state)))))
