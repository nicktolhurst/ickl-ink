(ns linkshortener.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql.helpers :refer :all]
            [honey.sql :as sql]
            [linkshortener.env :refer [env]]))

;; define mysql database connection using environment variables (or edn).
(def mysql-db {:host (env :DB_HOST)
                 :dbtype "mysql"
                 :dbname "linkshortener"
                 :user (env :DB_USER)
                 :password (env :DB_PSWD)})

;; basic query / insert functions.
(defn query [q] 
  (j/query mysql-db q))

(defn insert [q]
  (j/db-do-prepared mysql-db q))

;; create new redirect
(defn insert-redirect! [slug url] 
  (insert (-> (insert-into :redirects)
              (columns :url :slug)
              (values [[url slug]])
              (sql/format))))

;; get url from slug
(defn get-url [slug]
  (-> (query (-> (select :*)
             (from :redirects)
             (where := :slug slug)
             (sql/format)))
      first
      :url))

(comment
  
  )