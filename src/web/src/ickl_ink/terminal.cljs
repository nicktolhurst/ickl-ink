(ns ickl-ink.terminal
  (:require
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [goog.style :as gstyle]
   [hipo.core :as hipo]
   [clojure.string :as str])
   (:import [goog.events KeyCodes]
            [goog.events EventType]))

;; core elements of the terminal window.
(defonce doc
  (gdom/getDocument))
(defonce input
  (gdom/getElement "input"))
(defonce caret
  (gdom/getElement "caret"))
(defonce form
  (gdom/getElement "form"))
(defonce output
  (gdom/getElement "output"))

;; basic terminal functionality
(defn- clear-prompt [] (.reset form))
(defn- clear-output [] (.replaceChildren output ""))
(defn clear [] (clear-output))

;; make the custom caret move with the user input
(defonce caret-pos
  (gstyle/getPosition caret))
(defn- set-x-pos [x] (goog.math.Coordinate. (+ (. caret-pos -x) (* x 12)) (. caret-pos -y)))
(defn handle-caret-pos [evt] (gstyle/setPosition caret (set-x-pos (.. evt -currentTarget -selectionStart))))

;; When the key down is ENTER, evaluate the text and run a command.
(defn listenfor [command-text delegate]
  (gevents/listen input EventType.KEYDOWN
   (fn [evt] 
     (let [args (str/split (.. evt -currentTarget -value) #" ")]
       (when (= evt.keyCode KeyCodes.ENTER) 
         (when (= command-text (first args))
           (delegate (rest args))(clear-prompt)))))))

(defn respond [response] 
  (let [el (hipo/create response)]
    ;; (log/console el)
    (.appendChild output el)))

(defn as-link [url](str(hipo/create [:a {:href url} url])))

;; use escape to clear the terminal prompt.
(defn- handle-keydown [evt]
    (when (= evt.keyCode KeyCodes.ESC) (clear-prompt)))

;; when user clicks (anywhere) keep focus in input box.
(defn- handle-mouseup [evt]
  (.focus input))

;; when user submits the form, prevent the page from reloading.
(defn- handle-submit [evt]
  (.preventDefault evt))

(defn start []
  (do (gevents/listen input  EventType.KEYUP     handle-caret-pos) 
      (gevents/listen doc    EventType.MOUSEUP   handle-mouseup)
      (gevents/listen form   EventType.SUBMIT    handle-submit)  
      (gevents/listen input  EventType.KEYDOWN   handle-keydown)
      true))
