(ns ickl-ink.cmd
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [ickl-ink.log :as log]
            [ickl-ink.api :as api]
            [ickl-ink.terminal :as terminal]
            [cljs.core.async :refer [<!]]))

(defn clear [] (terminal/clear))

(defn shorten [args]
  (go 
    (let [url (nth args 0)
          ;; slug (nth args 1 "No slug :(")
          redirect (<! (api/create-redirect url))
          slug (:body redirect)
          result (str "https://localhost/" slug "/")
          link (terminal/as-link result)
          response (str url " --> " link)]
      (log/console response)
      (terminal/respond response))))
