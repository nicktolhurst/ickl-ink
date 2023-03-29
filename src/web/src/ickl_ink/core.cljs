(ns ^:figwheel-hooks ickl-ink.core
  (:require [ickl-ink.cmd :as cmd]
            [ickl-ink.terminal :as terminal]))

(terminal/start)

;; listens for input text, takes the first word 
;; and executes a delegate.
(terminal/listenfor "clear" cmd/clear)
(terminal/listenfor "shorten" cmd/shorten)
(terminal/listenfor "help" cmd/help)
