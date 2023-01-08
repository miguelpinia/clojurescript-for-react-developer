(ns app.pages.register
  (:require
   [app.auth :as auth :refer [error-state]]
   [app.components.list-errors :refer [list-errors]]
   [clojure.string :as s]
   [reagent.core :as r]
   [reitit.frontend.easy :as rfe]))

(defn register! [event registration-input]
  (.preventDefault event)
  (auth/register! registration-input))


(defn register-page []
  (let [initial-state {:email ""
                       :password ""
                       :username ""}
        state (r/atom initial-state)]
    (fn []
      [:div.auth-page>div.container.page>div.row
       [:div.col-md-6.offset-md-3.col-xs-12
        [:h1.text-xs-center "Sign Up"]
        [:p.text-xs-center [:a {:href (rfe/href :routes/login)} "Have an account?"]]
        [list-errors @error-state]
        [:form {:on-submit #(register! % @state)}
         [:fieldset
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :text
             :value (:username @state)
             :on-change #(swap! state assoc :username (.. % -target -value))
             :placeholder "Username"}]]
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :email
             :value (:email @state)
             :on-change #(swap! state assoc :email (.. % -target -value))
             :placeholder "Email"}]]
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type :password
             :value (:password @state)
             :on-change #(swap! state assoc :password (.. % -target -value))
             :placeholder "Your password"}]]
          [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign Up"]]]]])))
