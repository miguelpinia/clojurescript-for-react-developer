(ns app.components.articles
  (:require [reitit.frontend.easy :as rfe]))

(defn article-preview [{:keys [title description username favoritesCount author createdAt tagList] :as data}]
  (let [username (:username author)]
    (fn []
      [:div.article-preview
       [:div.article-meta
        [:a {:href (rfe/href :routes/profile {:username username})}
         [:img {:src (:image author)}]]
        [:div.info
         [:a.author {:href (rfe/href :routes/profile {:username username})}
          (:username author)]
         [:span.date (.toDateString (new js/Date createdAt))]]
        [:div.pull-xs-right
         [:button.btn.btn-sm.btn-outline-primary
          [:i.ion-heart favoritesCount]]]]
       [:a.preview.link
        [:h4 title]
        [:p description]
        [:span "Read more..."]
        [:ul.tag-list
         (for [tag tagList]
           ^{:key tag}
           [:li.tag-default.tag-pill.tag-outline tag])]]])))

(defn articles [{:keys [articles loading?] :as items}]
  (if loading?
    [:div.article-preview "Loading..."]
    (if (= 0 (count articles))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} articles]
         ^{:key slug}
         [article-preview article])])))
