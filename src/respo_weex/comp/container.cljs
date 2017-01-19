
(ns respo-weex.comp.container
  (:require [respo-weex.alias :refer [create-comp div]]
            [respo-weex.comp.text :refer [comp-text]]
            [respo-weex.comp.todolist :refer [comp-todolist]]))

(def style-states {:padding 8})

(defn render [store states]
  (fn [state mutate!]
    (div
     {}
     (comp-todolist store)
     (div
      {:style style-states}
      (comp-text (pr-str store) nil)
      (comp-text (pr-str states) nil)))))

(def comp-container (create-comp :container render))
