
(ns respo-weex.comp.space (:require [respo-weex.alias :refer [create-comp div]]))

(defn style-space [w h]
  (if (some? w)
    {:width w, :height "1px", :display :inline-block}
    {:width "1px", :height h, :display :inline-block}))

(defn render [w h] (fn [state mutate] (div {:style (style-space w h)})))

(def comp-space (create-comp :space render))
