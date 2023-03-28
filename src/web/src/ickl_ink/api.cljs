(ns ickl-ink.api
  (:require [cljs-http.client :as http]))

(def host "https://ickl.ick/")
(def api "api/")
(def redirect "redirect/")

(defn- to-url [& args]
  (let [url (apply str args)] url))

(defn- post [url body] (http/post url {:with-credentials? false
                                      :json-params body}))
(defn create-redirect [url]
  (post (to-url host api redirect) {:url url}))