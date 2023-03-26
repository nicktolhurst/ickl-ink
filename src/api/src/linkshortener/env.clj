(ns linkshortener.env 
  (:require [clojure.edn :as edn]))

(def envvars (edn/read-string (slurp "env.edn")))

(defn env [key] 
  (or (key envvars) (System/getenv (name key))))

(comment
  (env :host)
  (env :path)
  )