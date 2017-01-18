
(ns respo-weex.comp.todolist
  (:require [clojure.string :as string]
            [hsl.core :refer [hsl]]
            [respo-weex.comp.task :refer [task-component]]
            [respo-weex.alias :refer [div span input create-comp]]
            [respo-weex.comp.zero :refer [component-zero]]
            [respo-weex.comp.debug :refer [comp-debug]]
            [respo-weex.comp.space :refer [comp-space]]
            [respo-weex.comp.text :refer [comp-text]]
            [respo-weex.comp.wrap :refer [comp-wrap]]
            [respo-weex.polyfill :refer [text-width* io-get-time* set-timeout*]]
            [respo-weex.style.widget :as widget]))

(defn clear-done [e dispatch!] (println "dispatch clear-done") (dispatch! :clear nil))

(defn update-state [old-state changes]
  (comment println "changes:" (pr-str old-state) (pr-str changes))
  (merge old-state changes))

(defn handle-add [state mutate!]
  (fn [e dispatch!] (dispatch! :add (:draft state)) (mutate! {:draft ""})))

(def style-root
  {:line-height "24px",
   :color :black,
   :font-size 16,
   :background-color (hsl 120 20 98),
   :padding 10,
   :font-family "\"微软雅黑\", Verdana"})

(def style-list {:color :black, :background-color (hsl 120 20 98)})

(def style-toolbar
  {:white-space :nowrap,
   :padding "4px 0",
   :justify-content :start,
   :display :flex,
   :flex-direction :row})

(def style-panel {:display :flex, :margin-bottom 4, :flex-direction :row})

(defn on-test [e dispatch!]
  (println "trigger test!")
  (dispatch! :clear nil)
  (let [started (io-get-time*)]
    (loop [x 200] (dispatch! :add "empty") (if (> x 0) (recur (dec x))))
    (loop [x 20] (dispatch! :hit-first (rand)) (if (> x 0) (recur (dec x))))
    (dispatch! :clear nil)
    (loop [x 10] (dispatch! :add "only 10 items") (if (> x 0) (recur (dec x))))
    (println "time cost:" (- (io-get-time*) started))))

(defn on-focus [e dispatch!] (println "Just focused~"))

(defn init-state [props] {:draft "", :locked? false})

(defn on-text-change [mutate!] (fn [e dispatch!] (mutate! {:draft (:value e)})))

(defn on-lock [locked? mutate!] (fn [e dispatch!] (mutate! {:locked? (not locked?)})))

(defn render [tasks]
  (fn [state mutate!]
    (div
     {:style style-root}
     (comment comp-debug state {:left "80px"})
     (div
      {:style style-panel}
      (input
       {:style (merge
                widget/input
                {:width (max
                         200
                         (+ 24 (text-width* (:draft state) 16 "BlinkMacSystemFont")))}),
        :event {:focus on-focus, :input (on-text-change mutate!)},
        :attrs {:placeholder "Text", :value (:draft state)}})
      (comp-space 8 nil)
      (span
       {:style widget/button, :event {:click (handle-add state mutate!)}}
       (comp-text "Add" nil))
      (comp-space 8 nil)
      (span
       {:style widget/button, :event {:click clear-done}, :attrs {:inner-text "Clear"}})
      (comp-space 8 nil)
      (div
       {}
       (div {:style widget/button, :event {:click on-test}} (comp-text "heavy tasks" nil))))
     (div
      {:style style-list, :attrs {:class-name "task-list"}}
      (->> tasks (reverse) (map (fn [task] [(:id task) (task-component task)]))))
     (if (> (count tasks) 0)
       (div
        {:style style-toolbar, :attrs {:spell-check true}}
        (div
         {:style widget/button, :event (if (:locked? state) {} {:click clear-done})}
         (comp-text "Clear2"))
        (comp-space 8 nil)
        (div
         {:style widget/button, :event {:click (on-lock (:locked? state) mutate!)}}
         (comp-text (str "Lock?" (:locked? state)) nil))
        (comp-space 8 nil)
        (comp-wrap)))
     (comment comp-debug tasks {}))))

(def comp-todolist (create-comp :todolist init-state update-state render))
