(ns app.auth
  (:require [reagent.core :as r]
            [app.api :refer [api-uri error-handler get-auth-header]]
            [ajax.core :refer [POST GET PUT json-request-format json-response-format]]
            [reitit.frontend.easy :as rfe]))

(defonce auth-state (r/atom nil))
(defonce error-state (r/atom nil))

(defn auth-success! [{{:keys [token] :as user} :user}]
  (.setItem js/localStorage "auth-user-token" token)
  (reset! auth-state user)
  (when (seq @error-state)
    (reset! error-state nil))
  (rfe/push-state :routes/home))

(defn auth-error! [{{:keys [errors]} :response}]
  (reset! error-state errors))

(defn login! [input] ;; {:email "" :password ""}
  (POST (str api-uri "/users/login")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler auth-error!}))

(comment (login!  {:email "learnuidev3@foo.com"
                   :password "Foo bar"}))

(defn register! [input]
  (POST (str api-uri "/users")
        {:params {:user input}
         :handler auth-success!
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})
         :error-handler auth-error!}))

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

(comment (register! {:username "learnuidev7@foo.com"
                     :email "learnuidev7@foo.com"
                     :password "Foo bar"}))

;; (reset! error-state nil)

(comment @error-state)

;;;;;;;;
;; me ;;
;;;;;;;;


(defn get-me-success! [{user :user}]
  (reset! auth-state user))

(defn get-me-error! [error]
  (rfe/push-state :routes/home))

(defn me []
  (GET (str api-uri "/user")
       {:handler get-me-success!
        :headers (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler get-me-error!}))

;; save user
(defn save-user! [input] ;; {:email "" :password ""}
  (PUT (str api-uri "/user")
        {:params {:user input}
         :handler get-me-success!
         :error-handler get-me-error!
         :headers (get-auth-header)
         :format (json-request-format)
         :response-format (json-response-format {:keywords? true})}))

(def example-user {:image    ""
                :username ""
                :bio      ""
                :email    ""
                :password ""})

(comment (save-user! {:url "www.learnuidev.com"
                      :bio "Some biography. I teach clojurescript"}))


(comment (me))
