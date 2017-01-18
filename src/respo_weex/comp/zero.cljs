
(ns respo-weex.comp.zero (:require [respo-weex.alias :refer [create-comp div span]]))

(defn render [] (fn [state mutate] (div {:attrs {:inner-text 0}})))

(def component-zero (create-comp :zero render))
