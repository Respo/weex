
(ns respo-weex.comp.wrap
  (:require [respo-weex.alias :refer [create-comp div]]
            [respo-weex.comp.text :refer [comp-text]]))

(defn render [] (fn [state mutate!] (comp-text "pure component component" nil)))

(def comp-wrap (create-comp :wrap render))
