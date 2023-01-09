(ns app.pages.profile
  (:require
   [app.articles :refer [articles-state loading-state fetch-by favourited-by]]
   [app.profile :refer [profile-state follow! unfollow!]]
   [app.auth :refer [auth-state]]
   [app.components.articles :refer [articles]]
   [goog.string :as gstring]
   [reitit.frontend.easy :as rfe]
   [reagent.core :as r]))

;; (def elems [1 2 3 4 5])

;; (println (reduce + (map #(* % %) elems)))


(comment
  (= (:username @auth-state)
     (:username @profile-state)))

(defn edit-profile-settings [user?]
  (when user?
    [:a.btn.btn-sm.btn-outline-secondary.action-btn
     {:href (rfe/href :routes/settings)}
     [:i.ion-gear-a]
     (gstring/unescapeEntities "&nbsp;")
     "Edit Profile Settings"]))

(defn toggle-follow [{:keys [following username]}]
  (if following
    (unfollow! username)
    (follow! username)))

(defn follow-user-button [user? user]
  (when-not user?
    [:button {:class    ["btn btn-sm action-btn"
                         (if (:following user)
                           "btn-secondary"
                           "btn-outline-scondary")]
              :on-click #(toggle-follow user)}
     [:i.ion-plus-round]
     (gstring/unescapeEntities "&nbsp;")
     (if (:following user) "Unfollow" "Follow")]))

(defonce tab-state (r/atom :author))

(defn fetch-author [username]
  (do
    (reset! tab-state :author)
    (fetch-by username 0)))

(defn fetch-favourited [username]
  (do
    (reset! tab-state :favourited)
    (favourited-by username 0)))


(defn profile-page []
  (let [user? (= (:username @auth-state)
                 (:username @profile-state))]
    (when @profile-state
      [:div.profile-page
       [:div.user-info>div.container
        [:div.row>div.col-xs-12.col-md-10.offset-md-1
         [:img.user-img {:src (:image @profile-state)}]
         [:h4 (:username @profile-state)]
         [:p (:bio @profile-state)]
         [edit-profile-settings user?]
         [follow-user-button user? @profile-state]]]
       [:div.container>div.row>div.col-sx-12.col-md-10.offset-md-1
        [:div.articles-toggle
         [:ul.nav.nav-pills.outline-active
          [:li.nav-item
           [:a {:class ["nav-link" (when (= @tab-state :author) "active")]
                :on-click #(fetch-author (:username @profile-state))}
            "My Articles"]]
          [:li.nav-item
           [:a {:class ["nav-link" (when (= @tab-state :favourited) "active")]
                :on-click #(fetch-favourited (:username @profile-state))}
            "Favourited Articles"]]
          ]]
        [articles {:articles (:articles (deref articles-state))
              :loading? @loading-state}]]
       ])))
