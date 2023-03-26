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

;; when user submits form (line), insert and entry for it.
(defn- handle-keydown [evt]
  (-> 
   (when (= evt.keyCode KeyCodes.ENTER)
     (let [el (hipo/create [:div.prompt [:span (.. evt -currentTarget -value)]])] 
       (.insertBefore form el prompt) 
       (.reset form)))
   (when (= evt.keyCode KeyCodes.ESC))))

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