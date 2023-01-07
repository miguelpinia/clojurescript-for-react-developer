(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [ajax.core :refer [GET POST json-response-format]]))

(defonce articles-state (r/atom nil))

(defonce api-uri  "https://conduit.productionready.io/api")

(defn handler [response]
  (reset! articles-state response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn articles-browse []
  (GET (str api-uri "/articles?limit=20")
       {:handler handler
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (articles-browse)
  (first (:articles (deref articles-state))))

;; (defonce mock-articles
;;   [{:title "Backpacking is fun"} {:title "Do something"}])

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand "Conduit"]])


(defn banner [token]
  (when token
    [:div.banner>div.container
     [:h1.logo-front "Conduit"]
     [:p "A place to share your knowledge"]]))

(defn article-preview [{:keys [title description favoritesCount author createdAt tagList]}]
  [:div.article-preview
   [:div.article-meta
    [:a
     [:img {:src (:image author)}]]
   [:div.info
    [:a.author (:username author)]
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
       [:li.tag-default.tag-pill.tag-outline tag])]]])

(defn articles [items]
  (if-not (seq items)
    [:div.article-preview "Loading..."]
    (if (= 0 (count items))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} items]
         ^{:key slug}
         [article-preview article])])))

(defn main-view []
  [:div.col-md-9
   [:div.feed-toggle
    [:ul.nav.nav-pills.outline-active
     [:li.nav-item]
     [:a.nav-link.active {:href ""} "Global Feed"]]]
   [articles (:articles (deref articles-state))]])

(defn home-page []
  [:div.home-page
   [banner "auth-user-token"]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3
     [:div.sidebar
      [:p "Popular tags"]]]]])


(defn app []
  ;; vector -> data
  ;; Hiccup
  [:div
   [header]
   [home-page ]])


(defn ^:dev/after-load render
  "
  Render the toplevel component for this app.
  Dev Mode - runs every time the required dependencies changes
  "
  []
  (r/render [app] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  ;; runs only once, when the app starts
  (articles-browse)
  (render))
