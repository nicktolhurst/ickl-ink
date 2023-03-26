(ns ickl-ink.terminal
  (:require
;;    [ickl-ink.log :as log]
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [goog.style :as gstyle]
   [hipo.core :as hipo]
   [ickl-ink.log :as log])
   (:import [goog.events KeyCodes]))

;; core elements of the terminal window.
(defonce input
  (gdom/getElement "input"))
(defonce caret
  (gdom/getElement "caret"))
(defonce form
  (gdom/getElement "form"))
(defonce prompt
  (gdom/getElement "prompt"))

;; make the custom caret move with the user input
(defonce caret-pos
  (gstyle/getPosition caret))
(defn- set-x-pos [x] (goog.math.Coordinate. (+ (. caret-pos -x) (* x 12)) (. caret-pos -y)))
(defn- handle-caret-pos [evt] (gstyle/setPosition caret (set-x-pos (.. evt -currentTarget -selectionStart))))


(defn- add-prompt-to-terminal [text]
  (let [el (hipo/create [:div.prompt [:span text]])] 
       (.insertBefore form el prompt)))


;; when user submits form (line), insert and entry for it.
(defn- handle-keydown [evt]
  (let [text (.. evt -currentTarget -value)]

   ;; when the ENTER key is pressed, we want to submit the form
   ;; and return a response.
    (when (= evt.keyCode KeyCodes.ENTER)
      (add-prompt-to-terminal text)
      (.reset form))

   ;; when the ESCAPE key is pressed, clear the terminal.
    (when (= evt.keyCode KeyCodes.ESC) (.reset form)))
  )

;; when user clicks (anywhere) keep focus in input box.
(defn- handle-click [evt]
  (.focus input))

;; when user submits the form, prevent the page from reloading.
(defn- handle-submit [evt]
  (.preventDefault evt))

(defn start []
  (do (gevents/listen input  "keyup"     handle-caret-pos) 
      (gevents/listen input  "click"     handle-click)  
      (gevents/listen form   "submit"    handle-submit)  
      (gevents/listen input  "keydown"   handle-keydown)
      true))