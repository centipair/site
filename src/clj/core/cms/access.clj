(ns core.cms.access
  (:use core.cms.models))

(defn site-admin-access [request]
  (is-admin?))
