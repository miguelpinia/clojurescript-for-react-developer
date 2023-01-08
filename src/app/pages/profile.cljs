(ns app.pages.profile
   )

;; (comment @routes-state)
(def temp (atom nil))

(comment @temp)

(defn profile-page [{{:keys [username]} :path-params}]
  [:div "Profile page: " username])
