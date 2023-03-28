(ns ickl-ink.terminal
  (:require
   [ickl-ink.log :as log]
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
(defonce prompt
  (gdom/getElement "prompt"))


;; basic terminal functionality
(defn- clear-prompt [] (.reset form))
(defn- clear-output [] (.replaceChildren output ""))
(defn clear [] (clear-output))

;; make the custom caret move with the user input
(defonce caret-pos
  (gstyle/getPosition caret))
(defn- set-x-pos [ss] 
  (let [x (if (= ss 0) 4 (+ 4 (* ss 13.6)))
        y "2px"]
    (goog.math.Coordinate. x y)))
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
    (.appendChild output el)))

;; use escape to clear the terminal prompt.
(defn- handle-keydown [evt]
    (when (= evt.keyCode KeyCodes.ESC) (clear-prompt)))

;; when user clicks (anywhere) keep focus in input box.
(defn- handle-mouseup [evt]
  (.focus input))

(def suggestions ["shorten <url> [<slug>*] *max 6 chars" "help" "clear "])

;; when user submits the form, prevent the page from reloading.
(defn- handle-submit [evt]
  (.preventDefault evt))


(defonce el_suggestion(let [el(hipo/create [:span.suggestion])](.appendChild prompt el)))

(defn set-suggestion [text](hipo/reconciliate! el_suggestion [:span.suggestion text]))

(defn strlen-below? [s n]
  (> n (count s)))

(defn- handle-suggestions [evt]
  (let [text (.. evt -currentTarget -value)]
    (if (some #(str/includes? % text) suggestions) 
      (let [results (filter #(str/includes? % text) suggestions)
            suggestion (first results)]
        (set-suggestion (.toString suggestion)))
      (when (strlen-below? text 1) (set-suggestion "")))))

(defn start []
  (do (gevents/listen input  EventType.KEYUP     handle-caret-pos) 
      (gevents/listen input  EventType.MOUSEUP   handle-caret-pos)
      (gevents/listen input  EventType.KEYUP     handle-suggestions)
      (gevents/listen doc    EventType.MOUSEUP   handle-mouseup)
      (gevents/listen form   EventType.SUBMIT    handle-submit)  
      (gevents/listen input  EventType.KEYDOWN   handle-keydown)
      true))
