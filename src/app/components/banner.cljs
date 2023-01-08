(ns app.components.banner)

(defn banner [app-name token]
  (when token
    [:div.banner>div.container
     [:h1.logo-front app-anme]
     [:p "A place to share your knowledge"]]))
