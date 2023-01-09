(ns app.components.articles
  (:require [reitit.frontend.easy :as rfe]))

(defn article-preview [{:keys [title description username favoritesCount author createdAt tagList] :as data}]
  (let [username (:username author)
        _ (println author)]
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

(defn articles [items]
  (if-not (seq items)
    [:div.article-preview "Loading..."]
    (if (= 0 (count items))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} items]
         ^{:key slug}
         [article-preview article])])))
