
(ns respo-weex.style.widget (:require [hsl.core :refer [hsl]]))

(def input
  {:line-height "60px",
   :color :white,
   :font-size 32,
   :background-color "#ccc",
   :padding-right 8,
   :padding-left 8,
   :outline :none,
   :border :none})

(def button-text {:color :white, :font-size 32, :font-family "Avenir,Verdana"})

(def button
  {:line-height "60px",
   :background-color "#aad",
   :cursor :pointer,
   :padding-right 8,
   :padding-left 8,
   :display :inline-block,
   :height 60})
