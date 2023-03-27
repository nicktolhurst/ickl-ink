(ns ^:figwheel-hooks ickl-ink.core
  (:require [ickl-ink.cmd :as cmd]
            [ickl-ink.terminal :as terminal]))


(terminal/start)

(terminal/listenfor "clear" cmd/clear)
(terminal/listenfor "shorten" cmd/shorten)
