(ns app.pages.login
  (:require
   [app.auth :as auth :refer [error-state]]
   [app.components.list-errors :refer [list-errors]]
   [clojure.string :as s]
   [reitit.frontend.easy :as rfe]
   [reagent.core :as r]))

(defn login! [event login-input]
  (.preventDefault event)
  (println "LOGIN!")
  (auth/login! login-input))

(defn login-page []
  (let [initial-state {:email    ""
                       :password ""}
        state         (r/atom initial-state)]
    (fn []
      [:div.auth-page>div.container.page>div.row
       [:div.col-md-6.offset-md-3.col-xs-12
        [:h1.text-xs-center "Sign In"]
        [:p.text-xs-center [:a {:href (rfe/href :routes/register)} "Need an account?"]]
        [list-errors @error-state]
        [:form {:on-submit #(login! % @state)}
         [:fieldset
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type        :email
             :value       (:email @state)
             :on-change   #(swap! state assoc :email (.. % -target -value))
             :placeholder "my-email@mail.com"}]]
          [:fieldset.form-group
           [:input.form-control.form-control-lg
            {:type        :password
             :value       (:password @state)
             :on-change   #(swap! state assoc :password (.. % -target -value))
             :placeholder "Your password"}]]
          [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign In"]]]]])))
