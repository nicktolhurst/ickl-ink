(ns ickl-ink.cmd
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [ickl-ink.api :as api]
            [ickl-ink.terminal :as terminal]
            [cljs.core.async :refer [<!]]))

(defn clear [] (terminal/clear-output))

(defn shorten [args]
  (go 
    (let [url (nth args 0)
          ;; slug (nth args 1 "No slug :(")
          redirect (<! (api/create-redirect url))
          slug (:body redirect)
          link (str "https://" (.. js/window -location -hostname) "/" slug "/")
          response [:p 
                    [:span.secondary url] 
                    [:span.white " <-- "] 
                    [:a.primary {:href link} link]]]
      (terminal/respond response))))

