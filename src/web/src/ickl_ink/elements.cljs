(ns ickl-ink.elements
  (:require [hipo.core :as hipo]
            [goog.dom :as gdom]
            [goog.style :as gstyle]))

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
(defonce caret-pos
  (gstyle/getPosition caret))
(defonce suggestion
  (let [el (hipo/create [:span.suggestion ""])]
    (.appendChild prompt el)))

(defn handle-caret [event] 
  (let [ss (.. event -currentTarget -selectionStart)
        x (if (= ss 0) 4 (+ 4 (* ss 13.6)))
        y "2px"
        coordinates (goog.math.Coordinate. x y)]
     (gstyle/setPosition caret coordinates)))

(defn set-suggestion [html] (hipo/reconciliate! suggestion html))
  