(ns app.components.main-view
  (:require [app.articles :refer [articles-state tab-state articles-feed articles-browse loading-state]]
            [app.components.articles :refer [articles]]
            [reagent.core :as r]))

;;  (defonce tab-state (r/atom :all))

;; (comment
;;   @tab-state)

(defn handle-feed []
  (do
    (reset! tab-state :feed)
    (articles-feed)))

(defn handle-all []
  (do
    (reset! tab-state :all)
    (articles-browse)))

(defn feed-toggle []
  [:div.feed-toggle
    [:ul.nav.nav-pills.outline-active
     [:li.nav-item
      [:a {:class ["nav-link" (when (= @tab-state :feed) "active" )]
           :on-click handle-feed}
       "Your Feed"]]
     [:li.nav-item
      [:a {:class ["nav-link" (when (= @tab-state :all) "active" )]
           :on-click handle-all}
       "Global Feed"]]]])

(defn main-view []
  [:div.col-md-9
   [feed-toggle]
   [articles {:articles (:articles (deref articles-state))
              :loading? @loading-state}]])
