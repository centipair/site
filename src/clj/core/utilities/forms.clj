(ns core.utilities.forms
  (:use core.auth.session
        core.utilities.appresponse
        ))

(defn valid-csrf? [form]
  (if (nil? (:csrfmiddlewaretoken form))
    false
    (if (= (get-session :csrfmiddlewaretoken) (:csrfmiddlewaretoken form))
      true
      false)))

(defn validate-form [check form proceed]
  (let [errors (check form)]
    (if (empty? errors)
      ;;valid form
      (proceed form)
      {:status-code (response-code :form-error)
       :message "Invalid data submitted"
       :errors errors})))

(defn valid-form? [check form proceed]
  (if (valid-csrf? form)
    (validate-form check form proceed)
    {:status-code (response-code :forbidden) :message "CSRF: Invalid Request"}))


(defn to-data [request]
  (let [form (:params request)]
    form
    ))
