
(ns respo-weex.util.information (:require [clojure.string :as string]))

(def bubble-events [])

(def no-bubble-events
  [:focus
   :blur
   :scroll
   :click
   :dblclick
   :change
   :input
   :keydown
   :keyup
   :wheel
   :mousedown
   :touchstart])
