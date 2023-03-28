(ns ickl-ink.terminal
  (:require
   [ickl-ink.man :as man]
   [ickl-ink.elements :as el]
   [goog.events :as gevents]
   [hipo.core :as hipo]
   [clojure.string :as str])
   (:import [goog.events KeyCodes]
            [goog.events EventType]))

;; basic terminal functionality
(defn clear-prompt [] (.reset el/form))
(defn focus-input  [] (.focus el/input))
(defn clear-output [] (.replaceChildren el/output ""))

;; When the key down is ENTER, evaluate the text and run a command.
(defn listenfor [command-text delegate]
  (gevents/listen el/input EventType.KEYDOWN
   (fn [evt] 
     (let [args (str/split (.. evt -currentTarget -value) #" ")]
       (when (= evt.keyCode KeyCodes.ENTER) 
         (when (= command-text (first args))
           (delegate (rest args))(clear-prompt)))))))

(defn respond [response] 
  (let [el (hipo/create response)]
    (.appendChild el/output el)))

;; use escape to clear the terminal prompt.
(defn- handle-keydown [event] (when (= event.keyCode KeyCodes.ESC) (clear-prompt)))

(defn- handle-submit [event] (.preventDefault event))

(defn- handle-mouseup [_] (focus-input))

(defn start []
  (do (gevents/listen el/input  EventType.KEYUP     el/handle-caret) 
      (gevents/listen el/input  EventType.MOUSEUP   el/handle-caret)
      (gevents/listen el/input  EventType.KEYUP     man/handler)
      (gevents/listen el/doc    EventType.MOUSEUP   handle-mouseup)
      (gevents/listen el/form   EventType.SUBMIT    handle-submit)  
      (gevents/listen el/input  EventType.KEYDOWN   handle-keydown)
      true))
