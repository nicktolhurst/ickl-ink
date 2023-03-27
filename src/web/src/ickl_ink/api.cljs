(ns ickl-ink.api
  (:require [cljs-http.client :as http]))

(def host "https://localhost/")
(def api "api/")
(def redirect "redirect/")

(defn- to-url [& args]
  (let [url (apply str args)
        urlmap (http/parse-url url)]
    url))

(defn- post [url body] (http/post url {:with-credentials? false
                                      :json-params body}))

(defn create-redirect [url]
  (post (to-url host api redirect) {:url url}))