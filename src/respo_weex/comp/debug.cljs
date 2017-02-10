
(ns respo-weex.comp.debug
  (:require [hsl.core :refer [hsl]] [respo-weex.alias :refer [create-comp div span]]))

(def default-style
  {:position :absolute,
   :background-color (hsl 0 0 0),
   :color :white,
   :opacity 0.4,
   :font-size "10px",
   :font-family "Menlo",
   :box-shadow (str "0 0 1px " (hsl 0 0 0 0.8)),
   :line-height 1.6,
   :padding "2px 4px",
   :pointer-events :none})

(defn render [data more-style]
  (fn [state mutate!]
    (div
     {:style (merge default-style more-style)}
     (span {:attrs {:inner-text (pr-str data)}}))))

(def comp-debug (create-comp :debug render))
