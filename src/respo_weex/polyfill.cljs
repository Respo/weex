
(ns respo-weex.polyfill
  (:require [cljs.reader :refer [read-string]]))

(def read-string* read-string)

(defn document-create-element* [tag-name]
  (.createElement js/document tag-name))

(defn io-get-time* [] (.valueOf (js/Date.)))

(defn raise* [x] (throw (js/Error. x)))

(defonce ctx
 (if (and (exists? js/document) (exists? js/window))
   (.getContext (.createElement js/document "canvas") "2d")
   nil))

(defn text-width* [content font-size font-family]
  (if (some? ctx)
    (do
      (set! (.-font ctx) (str font-size "px " font-family))
      (.-width (.measureText ctx content)))
    nil))

(defn log* [arg]
  (.log js/console arg))

(defn set-timeout* [f]
  (js/setTimeout f 0))
