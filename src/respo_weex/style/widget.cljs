
(ns respo-weex.style.widget (:require [hsl.core :refer [hsl]]))

(def button-text {:color :white, :font-size 32, :font-family "Avenir,Verdana"})

(def input
  {:font-size 32,
   :line-height "60px",
   :padding-left 8,
   :padding-right 8,
   :outline :none,
   :background-color "#ccc",
   :border :none,
   :color :white})

(def button
  {:display :inline-block,
   :padding-left 8,
   :padding-right 8,
   :cursor :pointer,
   :background-color "#aad",
   :line-height "60px",
   :height 60})
