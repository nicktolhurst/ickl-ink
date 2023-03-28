(ns ickl-ink.man
  (:require
   [ickl-ink.elements :as el]
   [clojure.string :as str]))

(def clear [:span.suggestion ""])

(def splain
  {:clear [:span.suggestion
           "clear"
           [:br]
           [:strong "clears the output"]]

   :help [:span.suggestion
          "help [<cmd>]"
          [:br]
          [:strong "displays help text around each command"]]

   :shorten [:span.suggestion
             "shorten <url> [<slug>*]"
             [:br]
             [:strong "*max 6 chars"]
             [:br]]})


(defn- text-from [event] (.. event -currentTarget -value))

(defn- suggestions-exist-with? [text]
  (some #(str/includes? % text) (mapv name (keys splain))))

(defn- suggestions-from [text]
  (filter #(str/includes? % text) (mapv name (keys splain))))

(defn handler [event]
  (let [text (text-from event)]
    (if (suggestions-exist-with? text)
      (let [results (suggestions-from text)]
        (el/set-suggestion (splain (keyword (first results)))))
      (el/set-suggestion clear))))

