(ns core.api.models
  (:use 
   core.db.connection
   core.auth.user.models
   core.auth.session
   core.utilities.time
   clojurewerkz.cassaforte.cql
   clojurewerkz.cassaforte.query
   clojurewerkz.cassaforte.uuids)
  (:require 
   [core.cryptography :as crypto]))


(def api-app-key-table :api_app_key)

(defn create-app [platform version]
  (insert api-app-key-table {:app_key (crypto/random-base64 32) 
                             :platform platform 
                             :version version}))

(defn create-auth-token [user-profile]
  (let [auth_token (generate-session-id)
        session-map {:auth_token auth_token
                     :session_expire_time (set-time-expiry 23)
                     :user_id (:user_id user-profile)}]
    
    (insert-user-session session-map)  
    {:status-code 200 :message "login success" :auth_token auth_token}
    ))

(defn api-login [form]
  (let [user-login (get-user-login (:username form))
        user-profile (get-user-profile user-login)]
    (if (nil? user-login)
      login-error
      (if (valid-user-password? user-profile form)
        (create-auth-token user-profile)
        login-error))))

(defn select-app-key [app-key]
  (first (select api-app-key-table (where :app_key app-key))))
