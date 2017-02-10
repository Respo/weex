
(ns weex.framework
  (:require [clojure.string :as string]))

(def fs (js/require "fs"))

(def js-path "target/main.js")

(def fw-comment "// { \"framework\": \"Vanilla\" }\n\n")

(defn -main []
  (let [source-file (.readFileSync fs js-path "utf8")]
    (if (string/starts-with? source-file fw-comment)
      (println "Already done.")
      (do (.writeFileSync fs js-path (str fw-comment source-file))
        (println "Done!")))))

(-main)
