(ns app.profile
  (:require [reagent.core :as r]
            [app.api :refer [api-uri error-handler]]
            [app.auth :refer [get-auth-header]]
            [ajax.core :refer [POST GET DELETE json-request-format json-response-format]]))


(defonce profile-state (r/atom nil))

(defonce error-state (r/atom nil))

(defn fetch-success! [{:keys [profile]}]
  (reset! profile-state profile))

(defn fetch-error! [response]
  (reset! error-state response))

(defn fetch! [username]
  (GET (str api-uri "/profiles/" username)
       {:handler         fetch-success!
        :headers         (get-auth-header)
        :response-format (json-response-format {:keywords? true})
        :error-handler   fetch-error!}))

(comment
  (fetch! "diddy_")
  @profile-state
  @error-state)


(defn follow! [username]
  (POST (str api-uri "/profiles/" username "/follow")
        {:handler         fetch-success!
         :headers         (get-auth-header)
         :response-format (json-response-format {:keywords? true})
         :error-handler   fetch-error!}))

(defn unfollow! [username]
  (DELETE (str api-uri "/profiles/" username "/follow")
          {:handler         fetch-success!
           :headers         (get-auth-header)
           :response-format (json-response-format {:keywords? true})
           :error-handler   fetch-error!}))

;; http://localhost:4200/user/@Anah%20Bene%C5%A1ov%C3%A1

(comment
  (follow! "Anah Benešová")
  (unfollow! "Anah Benešová")
  @profile-state)
