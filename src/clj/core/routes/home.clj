(ns core.routes.home
  (:use compojure.core
        core.auth.user.forms
        core.auth.user.models
        core.utilities.forms
        core.utilities.appresponse)
  (:require [core.views.layout :as layout]
            [noir.response :as response]
            ))


(defn home-page []
  (layout/render "home.html"))

(defn about-page []
  (layout/render "about.html"))

(defn register-page []
  (layout/render "register.html"))

(defn register-submit [request]
  (send-response (valid-form? register-form (to-data request) register-user)))

(defn login-page []
  (layout/render "login.html"))

(defn login-submit [request]
  (send-response (valid-form? login-form (to-data request) login)))

(defn logout []
  (destroy-session)
  (response/redirect "/"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/register" [] (register-page))
  (POST "/register/submit" request (register-submit request))
  (GET "/login" [] (login-page))
  (POST "/login/submit" request (login-submit request))
  (GET "/logout" [] (logout))
  )
