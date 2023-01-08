(ns app.routes
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [app.pages.home :refer [home-page]]
            [app.pages.login :refer [login-page]]
            [app.pages.register :refer [register-page]]
            [app.pages.settings :refer [settings-page]]))

(defonce routes-state (r/atom nil))

(comment
  "takes route name and generates the route path, if not found returns nil"
  (rfe/href ::login))

(def routes
  [["/"         {:name :home
                 :view #'home-page}]
   ["/login"    {:name :login
                 :view #'login-page}]
   ["/register" {:name :register
                 :view #'register-page}]
   ["/settings" {:name :settings
                 :view #'settings-page}]])

(defn router-start! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [matched-route] (reset! routes-state matched-route))
   {:use-fragment false }))

(comment (-> @routes-state :data :view))
