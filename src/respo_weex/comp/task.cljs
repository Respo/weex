
(ns respo-weex.comp.task
  (:require [clojure.string :as string]
            [hsl.core :refer [hsl]]
            [respo-weex.alias :refer [div input span create-comp button]]
            [respo-weex.comp.debug :refer [comp-debug]]
            [respo-weex.comp.space :refer [comp-space]]
            [respo-weex.comp.text :refer [comp-text]]
            [respo-weex.style.widget :as widget]))

(def style-task {:padding-top 4, :padding-bottom 4, :position :relative})

(defn update-state [state text] text)

(defn handle-done [task-id] (fn [e dispatch!] (dispatch! :toggle task-id)))

(def style-done
  {:width 60, :height 60, :outline :none, :border :none, :vertical-align :middle})

(defn init-state [props] "")

(def style-row {:display :flex, :flex-direction :row, :justify-content :flex-start})

(defn on-text-change [task]
  (fn [event dispatch!]
    (let [task-id (:id task), text (:value event)]
      (println "Change" event)
      (dispatch! :update {:id task-id, :text text}))))

(defn handle-remove [task] (fn [e dispatch!] (dispatch! :remove (:id task))))

(defn on-text-state [mutate!] (fn [e dispatch!] (mutate! (:value e))))

(defn render [task]
  (fn [state mutate!]
    (div
     {:style style-task}
     (div
      {:style style-row}
      (div
       {:style (merge style-done {:background-color (if (:done? task) "#669" "#ccf")}),
        :event {:click (handle-done (:id task))}})
      (comp-space 8 nil)
      (input
       {:style (merge widget/input {:width 320}),
        :event {:input (on-text-change task)},
        :attrs {:value (:text task), :placeholder "Task"}}))
     (comp-space nil 8)
     (div
      {:style style-row}
      (input
       {:style (merge widget/input {:width 320}),
        :event {:input (on-text-state mutate!)},
        :attrs {:value state, :placeholder "Temp notes"}})
      (comp-space 8 nil)
      (div
       {:style widget/button, :event {:click (handle-remove task)}}
       (comp-text "Remove" widget/button-text)))
     (comp-space nil 8)
     (div {:style {}} (comp-text state nil)))))

(def task-component (create-comp :task init-state update-state render))

(defn on-click [props state] (fn [event dispatch!] (println "clicked.")))
