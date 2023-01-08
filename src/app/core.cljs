(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            [app.layout :refer [app]]
            [app.routes :refer [router-start!]]
            [app.articles :refer [articles-browse]]))


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
