
(ns respo-weex.util.comparator (:require [respo-weex.polyfill :refer [raise*]]))

(defn type-as-int [x]
  (cond
    (number? x) 0
    (keyword? x) 1
    (string? x) 2
    :else (raise* "use number, keyword or string in coord!")))

(defn compare-more [x y]
  (let [type-x (type-as-int x), type-y (type-as-int y)]
    (if (= type-x type-y) (compare x y) (compare type-x type-y))))
