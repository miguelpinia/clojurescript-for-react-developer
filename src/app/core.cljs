(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [spec-tools.data-spec :as ds]
            [ajax.core :refer [GET POST json-response-format]]))

(defonce articles-state (r/atom nil))
(defonce routes-state (r/atom nil))

(comment
  "takes route name and generates the route path, if not found returns nil"
  (rfe/href ::login))

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
   [:a.navbar-brand {:href (rfe/href ::home)} "Conduit"]
   [:ul.nav.navbar-nav.pull-xs-right
    [:li.nav-item
     [:a.nav-link  {:href (rfe/href ::home)} "Home"]]
    [:li.nav-item
     [:a.nav-link  {:href (rfe/href ::login)} "Login"]]
    [:li.nav-item
     [:a.nav-link  {:href (rfe/href ::register)} "Sign Up"]]]])


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


(defn auth-signin [event]
  (.preventDefault event)
  (println "LOGIN"))

(defn login-page []
  [:div.auth-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Sign In"]
    [:p.text-xs-center [:a {:href (rfe/href ::register)} "Need an account?"]]
    [:form {:on-submit auth-signin}
     [:fieldset
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :email :placeholder "miguel@mail.com"}]]
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :password :placeholder "Your password"}]]
      [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign In"]]]]])

(defn auth-signup [event]
  (.preventDefault event)
  (println "REGISTER"))

(defn register-page []
  [:div.auth-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Sign Up"]
    [:p.text-xs-center [:a {:href (rfe/href ::login)} "Have an account?"]]
    [:form {:on-submit auth-signup}
     [:fieldset
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :text :placeholder "Username"}]]
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :email :placeholder "email"}]]
      [:fieldset.form-group
       [:input.form-control.form-control-lg {:type :password :placeholder "Your password"}]]
      [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Sign Up"]]]]])

(defn settings-form []
  [:form
   [:fieldset
    [:fieldset.form-group
     [:input.form-control {:type :text :placeholder "URL of profile picture"}]
     ]
    [:fieldset.form-group
     [:input.form-control.form-control-lg {:type :text :placeholder "Username"}]]
    [:fieldset.form-group
     [:textarea.form-control.form-control-lg {:type :text
                                             :placeholder "Short bio about you"
                                             :rows 8}]]
    [:fieldset.form-group
     [:input.form-control.form-control-lg
      {:type :email :placeholder "Email"}]]
    [:fieldset.form-group
     [:input.form-control.form-control-lg
      {:type :password :placeholder "Password"}]]
    [:button.btn.btn-lg.btn-primary.pull-xs-right {:type :submit} "Update Settings" ]]])


(defn settings-page []
  [:div.settings-page>div.container.page>div.row
   [:div.col-md-6.offset-md-3.col-xs-12
    [:h1.text-xs-center "Your settings"]
    [settings-form]
    [:hr]
    [:button.btn.btn-outline-danger "Or click here to logout..."]]
   ])


(def routes
  [["/"      {:name ::home
              :view #'home-page}]
   ["/login" {:name ::login
              :view #'login-page}]
   ["/register" {:name ::register
                 :view #'register-page}]
   ["/settings" {:name ::settings
                 :view #'settings-page}]])

;; Step 5 - write the router-start! function

;; (def match (atom nil))
;; (comment (r/render [current-page] (.getElementById js/document "app")))


(defn router-start! []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [matched-route] (reset! routes-state matched-route))
   {:use-fragment false }))


(comment (-> @routes-state :data :view))

(defn app []
  ;; vector -> data
  ;; Hiccup
  [:div
   [header]
   (let [current-view (->  @routes-state :data :view)]
     [current-view])])


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
  (router-start!)
  (articles-browse)
  (render))
