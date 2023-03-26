(ns ^:figwheel-hooks ickl-ink.core
  (:require
   [ickl-ink.log :as log]
   [ickl-ink.terminal :as terminal]))

(def isStarted? (terminal/start))

(when isStarted? 
  (log/console isStarted?))


