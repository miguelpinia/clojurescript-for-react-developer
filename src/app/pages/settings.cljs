(ns app.pages.settings
  (:require [reitit.frontend.easy :as rfe]
            [app.auth :as auth :refer [auth-state]]
            [reagent.core :as r]))

(defn save-user [event user]
  (.preventDefault event)
  (println user))

(comment @auth-state)

(defn settings-form [user]
  (let [state (r/atom user)]
    (fn []
      [:form {:on-submit #(save-user % @state)}
       [:fieldset
        [:fieldset.form-group
         [:input.form-control {:type        :text
                               :placeholder "URL of profile picture"
                               :on-change   #(swap! state assoc :image (.. % -target -value))
                               :value       (:image @state)}]
         ]
        [:fieldset.form-group
         [:input.form-control.form-control-lg {:type        :text
                                               :placeholder "Username"
                                               :on-change   #(swap! state assoc :username (.. % -target -value))
                                               :value       (:username @state)}]]
        [:fieldset.form-group
         [:textarea.form-control.form-control-lg {:type        :text
                                                  :placeholder "Short bio about you"
                                                  :rows        8
                                                  :on-change   #(swap! state assoc :bio (.. % -target -value))
                                                  :value       (:bio @state)}]]
        [:fieldset.form-group
         [:input.form-control.form-control-lg  {:type        :email
                                                :placeholder "Email"
                                                :on-change   #(swap! state assoc :email (.. % -target -value))
                                                :value       (:email @state)}]]
        [:fieldset.form-group
         [:input.form-control.form-control-lg  {:type        :password
                                                :on-change   #(swap! state assoc :password (.. % -target -value))
                                                :value       (:password @state)
                                                :placeholder "Password"}]]
        [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Update Settings" ]]])))

(defn logout []
  (println "LOGOUT!")
  (.removeItem js/localStorage "auth-user-token")
  (rfe/push-state :routes/home)
  (reset! auth/auth-state nil))


(defn settings-page []
  [:div.settings-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Your settings"]
    [settings-form @auth-state]
    [:hr]
    [:button.btn.btn-outline-danger
     {:on-click logout}
     "Or click here to logout..."]]])
