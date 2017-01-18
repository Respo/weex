
(ns respo-weex.render.html
  (:require [clojure.string :as string]
            [respo-weex.util.format
             :refer
             [prop->attr purify-element mute-element ensure-string]]
            [respo-weex.util.detect :refer [component? element?]]
            [respo-weex.render.expander :refer [render-app]]
            [respo-weex.controller.deliver :refer [mutate-factory]]))

(def global-element (atom nil))

(defn escape-html [text] (-> text (string/replace (re-pattern "\"") "&quot;")))

(defn style->string [styles]
  (string/join
   ""
   (->> styles
        (map
         (fn [entry]
           (let [k (first entry), v (last entry)]
             (str (name k) ":" (if (string? v) (escape-html v) (ensure-string v)) ";")))))))

(defn entry->string [entry]
  (let [k (first entry), v (last entry)]
    (str (prop->attr (name k)) "=" (pr-str (if (= k :style) (style->string v) v)))))

(defn props->string [props]
  (->> props
       (filter
        (fn [entry]
          (let [k (first entry)] (not (re-matches (re-pattern "^:on-.+") (str k))))))
       (map entry->string)
       (string/join " ")))

(defn element->html [element]
  (let [tag-name (name (:name element))
        attrs (into {} (:attrs element))
        text-inside (or (:innerHTML attrs) (:inner-text attrs))
        tailored-props (-> attrs (dissoc :innerHTML) (dissoc :inner-text))
        props-in-string (props->string tailored-props)
        children (->> (:children element)
                      (map (fn [entry] (let [child (last entry)] (element->html child)))))]
    (str
     "<"
     tag-name
     (if (> (count props-in-string) 0) " " "")
     props-in-string
     ">"
     (or text-inside (string/join "" children))
     "</"
     tag-name
     ">")))

(def global-states (atom {}))

(def build-mutate (mutate-factory global-element global-states))

(defn make-html [tree]
  (let [element (render-app tree @global-states build-mutate nil)]
    (element->html (purify-element (mute-element element)))))

(defn element->string [element]
  (let [tag-name (name (:name element))
        attrs (into {} (:attrs element))
        styles (or (:style element) {})
        text-inside (or (:innerHTML attrs) (:inner-text attrs))
        formatted-coord (pr-str (:coord element))
        formatted-event (pr-str (into #{} (keys (:event element))))
        tailored-props (-> attrs
                           (dissoc :innerHTML)
                           (dissoc :inner-text)
                           (merge
                            {:data-coord formatted-coord, :data-event formatted-event})
                           ((fn [props]
                              (if (> (count styles) 0) (assoc props :style styles) props))))
        props-in-string (props->string tailored-props)
        children (->> (:children element)
                      (map (fn [entry] (let [child (last entry)] (element->string child)))))]
    (str
     "<"
     tag-name
     (if (> (count props-in-string) 0) " " "")
     props-in-string
     ">"
     (or text-inside (string/join "" children))
     "</"
     tag-name
     ">")))

(defn make-string [tree]
  (let [element (render-app tree @global-states build-mutate nil)]
    (element->string (purify-element (mute-element element)))))
