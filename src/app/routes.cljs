(ns app.routes
  (:require [reagent.core :as r]
            ;; routing
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [reitit.frontend.controllers :as rfc]
            ;; state
            [app.auth :as auth ]
            [app.profile :as profile]
            ;;pages
            [app.pages.home :refer [home-page]]
            [app.pages.login :refer [login-page]]
            [app.pages.register :refer [register-page]]
            [app.pages.settings :refer [settings-page]]
            [app.pages.profile :refer [profile-page]]))

(defonce routes-state (r/atom nil))
(defonce temp (atom nil))

(comment @temp)

(comment
  "takes route name and generates the route path, if not found returns nil"
  (rfe/href ::login))

(def routes
  [["/"         {:name        :routes/home
                 :view        #'home-page
                 :controllers [{:start #(println "enter - home page")
                                :stop  #(println "exit - home page")}]}]
   ["/login"    {:name        :routes/login
                 :view        #'login-page
                 :controllers [{:start #(println "enter - login page")
                                :stop  (fn []
                                         (println "exit - login page")
                                         (when  (seq @auth/error-state)
                                           (do (println "Gets invoked again")
                                               (reset! auth/error-state nil))))}]}]
   ["/register" {:name        :routes/register
                 :view        #'register-page
                 :controllers [{:start #(println "enter - register page")
                                :stop  (fn []
                                         (println "exit - register page")
                                         (when (seq @auth/error-state)
                                           (do (println "Gets invoked again")
                                               (reset! auth/error-state nil))))}]}]
   ["/settings" {:name :routes/settings
                 :view #'settings-page}]
   ["/user/@:username" {:name        :routes/profile
                        :view        #'profile-page
                        :parameters  {:path {:username string?}}
                        :controllers [{:identity (fn [match] (:path (:parameters match)))
                                       :start    (fn [{:keys [username] :as props}]
                                                   (profile/fetch! username)
                                                   (println "Entering Profile of -" username )
                                                   (reset! temp props))}]}]])

(defn router-start! []
  (rfe/start!
   (rf/router routes {:data {:coercion    rss/coercion
                             :controllers [{:start (fn []
                                                     (auth/me))
                                            :stop  #(println "Root controller stop")}]}})
   (fn [new-match] (swap! routes-state (fn [old-match]
                                        (if new-match
                                          (assoc new-match :controllers
                                                 (rfc/apply-controllers
                                                  (:controllers old-match)
                                                  new-match))))))
   {:use-fragment false }))

(comment (-> @routes-state :data :view))
