
(ns respo-weex.comp.text (:require [respo-weex.alias :refer [create-comp span code text]]))

(defn render-code [content style]
  (fn [state mutate!] (code {:style style, :attrs {:inner-text content}})))

(def comp-code (create-comp :code render-code))

(defn render [content style]
  (fn [state mutate!] (text {:style style, :attrs {:value content}})))

(def comp-text (create-comp :text render))
