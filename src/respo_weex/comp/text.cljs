
(ns respo-weex.comp.text (:require [respo-weex.alias :refer [create-comp span code text]]))

(defn render [content style]
  (fn [state mutate!] (text {:attrs {:value content}, :style style})))

(def comp-text (create-comp :text render))

(defn render-code [content style]
  (fn [state mutate!] (code {:attrs {:inner-text content}, :style style})))

(def comp-code (create-comp :code render-code))
