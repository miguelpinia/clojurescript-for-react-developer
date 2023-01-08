(ns app.layout
  (:require [app.components.header :refer [header]]
            [app.routes :refer [routes-state]]))

(defn app []
  ;; vector -> data
  ;; Hiccup
  [:div
   [header]
   (let [current-view (->  @routes-state :data :view)]
     [current-view])])
