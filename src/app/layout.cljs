(ns app.layout
  (:require [app.components.header :refer [header]]
            [app.routes :refer [routes-state]]
            [app.auth :refer [auth-state]]))

(comment @routes-state)

(defn app []
  ;; vector -> data
  ;; Hiccup
  [:div
   [header @auth-state]
   (let [current-view (->  @routes-state :data :view)]
     [current-view @routes-state])])
