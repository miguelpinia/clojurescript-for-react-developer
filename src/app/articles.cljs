(ns app.articles
  (:require [reagent.core :as r]
            [app.api :refer [api-uri]]
            [app.auth :refer [get-auth-header]]
            [ajax.core :refer [GET json-response-format]]))

(defonce articles-state (r/atom nil))
(defonce loading-state (r/atom false))


(defn handler [response]
  (reset! loading-state false)
  (reset! articles-state response))

(defn error-handler [{:keys [status status-text]}]
  (reset! loading-state false)
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn articles-browse []
  (reset! loading-state true)
  (GET (str api-uri "/articles?limit=20")
       {:handler handler
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))

(comment
  (articles-browse)
  (first (:articles (deref articles-state))))

(defn articles-feed []
  (reset! loading-state true)
  (GET (str api-uri "/articles/feed?limit=10&offset=0")
       {:handler handler
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler error-handler}))
