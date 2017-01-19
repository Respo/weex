
(ns respo-weex.comp.task
  (:require [clojure.string :as string]
            [hsl.core :refer [hsl]]
            [respo-weex.alias :refer [div input span create-comp button]]
            [respo-weex.comp.debug :refer [comp-debug]]
            [respo-weex.comp.space :refer [comp-space]]
            [respo-weex.comp.text :refer [comp-text]]
            [respo-weex.style.widget :as widget]))

(def style-task {:padding-top 4, :position :relative, :padding-bottom 4})

(defn update-state [state text] text)

(defn handle-done [task-id] (fn [e dispatch!] (dispatch! :toggle task-id)))

(def style-done
  {:vertical-align :middle, :width 60, :outline :none, :border :none, :height 60})

(defn init-state [props] "")

(def style-row {:justify-content :flex-start, :display :flex, :flex-direction :row})

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
        :attrs {:placeholder "Task", :value (:text task)}}))
     (div
      {:style style-row}
      (input
       {:style (merge widget/input {:width 320}),
        :event {:input (on-text-state mutate!)},
        :attrs {:placeholder "Temp notes", :value state}})
      (comp-space 8 nil)
      (div
       {:style widget/button, :event {:click (handle-remove task)}}
       (comp-text "Remove" widget/button-text)))
     (div {:style {}} (comp-text state nil)))))

(def task-component (create-comp :task init-state update-state render))

(defn on-click [props state] (fn [event dispatch!] (println "clicked.")))
