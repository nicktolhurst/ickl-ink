(ns ickl-ink.log)

(defn console [& args] (apply (.-log js/console) args))