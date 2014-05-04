(ns core.auth.user.models
    (:use 
     core.db.connection
     core.auth.session
     core.utilities.time
     clojurewerkz.cassaforte.cql
     clojurewerkz.cassaforte.query
     clojurewerkz.cassaforte.uuids
     [core.utilities.validators :only [is-username? is-email-proxy?]])
    (:require 
     [core.cryptography :as crypto]))


(def user-account-table :user_account)
(def user-login-username-table :user_login_username)
(def user-login-email-table :user_login_email)
(def user-session-table :user_session)
(def user-session-index-table :user_session_index)

(def login-error {:status-code 422 :errors {:__all__ "Username or password error"}})


(defn insert-user-session [session-map]
  (do
    (insert user-session-table session-map)
    (insert user-session-index-table {:user_id (:user_id session-map)
                                      :auth_token (:auth_token session-map)})))


(defn create-user-session [user-account]
  (let [auth_token (generate-session-id)
        session-map {:auth_token auth_token
                     :session_expire_time (set-time-expiry 23)
                     :user_id (:user_id user-account)}]
    (do
      (insert-user-session session-map)
      (set-cookies :auth_token auth_token)
      {:status-code 200 :message "login success" :redirect "/admin/"})))

(defn get-user-session []
  (let [auth_token (get-cookies :auth_token)]
    (if (nil? auth_token)
      nil
      (first (select user-session-table (where :auth_token auth_token))))))


(defn delete-session-index [user_session]
  (delete user-session-index-table (where 
                                    :user_id (:user_id user_session) 
                                    :auth_token (:auth_token user_session))))

(defn delete-session [user_session]
  (do
    (delete user-session-table (where :auth_token (:auth_token user_session)))
    (delete-session-index user_session)))

(defn destroy-session []
  (let [user_session (get-user-session)]
    (if (nil? user_session)
      nil
      (delete-session user_session))))

(defn select-user-account [user_id]
  (select user-account-table (where :user_id user_id)))


(defn insert-user-login-username [user-map]
  (insert user-login-username-table {:user_id (:user_id user-map)
                                     :username (:username user-map)}))

(defn insert-user-login-email [user-map]
  (insert user-login-email-table {:user_id (:user_id user-map)
                                  :email (:email user-map)}))

(defn insert-user-account [user-map]
  (let [user_id (time-based)]
    (insert user-account-table {:user_id user_id
                                :username (:username user-map)
                                :email (:email user-map)
                                :first_name (:first_name user-map)
                                :last_name (:last_name user-map)
                                :active (:active user-map)
                                :password (crypto/encrypt-password (:password user-map))})
    user_id))


(defn register-user [user-map]
  (let [user_id (insert-user-account user-map)
        user-login-map {:user_id user_id
                         :email (:email user-map)
                         :username (:username user-map)}]
    (do 
      (insert-user-login-username user-login-map)
      (insert-user-login-email user-login-map)
      {:status-code 200 :message "registration success"}
      )))

(defn select-user-username [username]
  (first (select user-login-username-table (where :username username))))


(defn select-user-email [email]
  (first (select user-login-email-table (where :email email))))

(defn get-user-login [username]
  (if (is-username? username)
    (select-user-username username)
    (if (is-email-proxy? username)
      (select-user-email username)
      nil)))

(defn get-user-account [user-login]
  (if (nil? user-login)
    nil
    (first (select-user-account (:user_id user-login)))))

(defn valid-user-password? [user-account form]
    (crypto/check-password (:password form) (:password user-account)))


(defn login [form]
  (let [user-login (get-user-login (:username form))
        user-account (get-user-account user-login)]
    (if (nil? user-login)
      login-error
      (if (valid-user-password? user-account form)
        (create-user-session user-account)
        login-error))))

