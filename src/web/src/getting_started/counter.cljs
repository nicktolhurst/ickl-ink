(ns ^:figwheel-hooks getting-started.counter
  (:require
   [clj-http.client :as http]
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [goog.style :as gstyle]
   [hipo.core :as hipo]))

;; Create Logger
(defn log [& args] (apply (.-log js/console) args))
;; the URL input field. 
(def url-input (gdom/getElement "url-input"))
;; the caret
(def caret (gdom/getElement "caret"))

(def form (gdom/getElement "form"))
(def prompt (gdom/getElement "prompt"))

(def caret-pos (gstyle/getPosition caret))
(log caret-pos)

(defn set-x-pos [x] (goog.math.Coordinate. (+ (. caret-pos -x) (* x 12)) (. caret-pos -y)))

(defn update-pos [evt] (gstyle/setPosition caret (set-x-pos (.. evt -currentTarget -selectionStart))))


(defn submit-line [e] 
  (when (= e.keyCode 13) (let [el (hipo/create [:div.prompt [:span (.. e -currentTarget -value)]])]
                           (.insertBefore form el prompt) 
                           (.textContent url-input "" )
                           (let [response (http/post "https://localhost/api/redirect"
                                                     {:form-params {:url (.. e -currentTarget -value)} :content-type :json})]
                             (log response)))
                           ))


(let [client   (sync/create-client {})
      response (get client "http://localhost:8080/test" {:query-params {"abc" "def"}})]
  (println (:body response)))


(defonce is-initialized?
  (do
    (gevents/listen url-input
                    "keyup"
                    update-pos)

    (gevents/listen url-input
                    "keydown"
                    submit-line)
    
    ;; stop reloading the page when we press enter.
    (gevents/listen (gdom/getElement "form") "submit" (fn [e] (.preventDefault e)))
    true))