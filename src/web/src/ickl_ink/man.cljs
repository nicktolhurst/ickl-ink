(ns ickl-ink.man
  (:require
   [ickl-ink.log :as log]
   [ickl-ink.elements :as el]
   [clojure.string :as str]
   [goog.string :as gstring]))

(def clear [:span.suggestion ""])

(defn example [text] [:span.example [:br] "  " (gstring/unescapeEntities "&#x21B3;") " " text])

(defn schema [text] [:em [:strong text]])

(def suggestions
  {:clear [:span.suggestion
           (schema "clear")
           [:span.summary
            [:br]
            [:br] "clears the terminal output"
            [:br]
            [:br] "EXAMPLES"
            (example "clear")]]

   :help [:span.suggestion
          (schema "help [<cmd>]")
          [:span.summary
           [:br]
           [:br] "displays help text"
           [:br] "optionally provide a command for more detailed usage"
           [:br]
           [:br] "EXAMPLES"
           (example "help")
           (example "help shorten")
           (example "help clear")
           (example "help me")]]
   
   :shorten [:span.suggestion
          (schema "shorten <url> [<slug>]")
          [:span.summary
           [:br]
           [:br] "shorten a url into a redirect"
           [:br] "optionally provide a slug with a max of 6 characters, if the slug is available"
           [:br] "then it will be used, if not you will be prompted to try again"
           [:br]
           [:br] "EXAMPLES"
           (example "shorten https://www.example.com/")
           (example "shorten https://www.example.com/ myslug")]]})

(defn- text-from [event] (str/lower-case (.. event -currentTarget -value)))

(defn- suggestions-exist-with? [text]
  (some #(str/includes? % text) (mapv name (keys suggestions))))

(defn- suggestions-from [text]
  (filter #(str/includes? % text) (mapv name (keys suggestions))))

(defn splain [args] 
  (el/set-suggestion 
   [:span.suggestion
    (schema "Help Page!")
    [:span.summary
     [:br]
     [:br] "Wants to help. Doesn't know how."
     [:br]]]))

(defn handler [event]
  (let [text (first (str/split (text-from event) #" "))]
    (if (str/blank? text)
      (el/set-suggestion clear)
      (if (suggestions-exist-with? text)
        (let [results (suggestions-from text)]
          (el/set-suggestion (suggestions (keyword (first results)))))
        (el/set-suggestion clear)))))