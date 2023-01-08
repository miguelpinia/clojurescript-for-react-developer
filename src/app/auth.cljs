(ns app.auth
  (:require [reagent.core :as r]
            [app.api :refer [api-uri error-handler]]
            [ajax.core :refer [POST json-request-format json-response-format]]))

(defonce auth-state (r/atom nil))

(defn auth-success! [{{:keys [token] :as user} :user}]
  (.setItem js/localStorage "auth-user-token" token)
  (reset! auth-state user))

(comment
  "Saving value into local storage"
  (.setItem js/localStorage
            "auth-user-token"
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImxlYXJudWlkZXY2QGZvby5jb20iLCJ1c2VybmFtZSI6ImxlYXJudWlkZXY2QGZvby5jb20iLCJpYXQiOjE2NzMxMzk1NjgsImV4cCI6MTY3ODMyMzU2OH0.72RfpahzTilarecoapj1EEyfsLK048U7klfrbNAeWLM"))

(comment
  "Getting auth value from local storage - will be used to make auth requests"
  (.getItem js/localStorage "auth-user-token")  )

(comment
  "Removing value"
  (.removeItem js/localStorage "auth-user-token"))

(comment "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImxlYXJudWlkZXY3QGZvby5jb20iLCJ1c2VybmFtZSI6ImxlYXJudWlkZXY3QGZvby5jb20iLCJpYXQiOjE2NzMxNDAxMjcsImV4cCI6MTY3ODMyNDEyN30.HOVQtTwv8VenqvSbCIJx8BxxVfhNs4R4E3IG4qkzpgA")

(comment (:token @auth-state))

(defn login! [input] ;; {:email "" :password ""}
  (POST (str api-uri "/users/login")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler error-handler}))

(comment (login!  {:email "learnuidev3@foo.com"
                   :password "Foo bar"}))

(defn register! [input]
  (POST (str api-uri "/users")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler error-handler}))

(comment (register! {:username "learnuidev7@foo.com"
                     :email "learnuidev7@foo.com"
                     :password "Foo bar"}))
