(ns ^:figwheel-hooks getting-started.http
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

;; request
(go (let [response (<! (http/get "https://api.github.com/users"
                                ;; parameters
                                 {:with-credentials? false
                                  :query-params {"since" 135}}))]
      (prn  (:body response)))) ;; print the response's body in the console