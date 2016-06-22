(ns aidbox-monitoring.parser
  (:require [clojure.string :as str]))

(def nginx-access-keys
  [:ip :date :method :url :statuscode :bytessent :refferer :useragent ])
(def nginx-access-regex 
  #"^(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}) - - \[(.+?)\] \"(GET|POST|PUT|DELETE) (.+) HTTP/1\.1\" (\d{1,3}) (\d+) \"(.+)\" \"(.+)\"")

(def nginx-error-keys
  [:date :status :errorcode :errmessage])
(def nginx-error-regex 
  #"^(.+? .+?) \[(.+?)\] (.+?): (.+)$")

(def app-keys
  [:date :status :worker :context :logger :message]  )
(def app-regex 
  #"^(.+? .+?) (.+?) \[(.+?)\] (.+?) (.+?)$")


(defn mk-parser [keys regex]
  (fn [msg] 
    (zipmap keys (rest (re-find regex msg)))))

(def parse-nginx-access
  (mk-parser nginx-access-keys nginx-access-regex))

(def parse-nginx-error 
  (mk-parser nginx-error-keys nginx-error-regex))

(def app-log-parser
  (mk-parser app-keys app-regex))

(defn parse-app [message]
  (let [[status msg] (str/split message #"\|\|")]
    (assoc (app-log-parser (str/trim status))
           :message (str/trim msg))) )
  
(def na-log
    "87.249.45.131 - - [29/Mar/2016:12:33:08 +0000] \"GET /boxes HTTP/1.1\" 200 2325 \"https://aidbox.io/ui\" \"Mozilla/5.0  (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/49.0.2623.87 Chrome/49.0.2623.87 Safari/537.36\"")

(def ne-log 
  "2016/03/23 16:11:57 [error] 30770#0: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 87.249.45.131, server: ~^(?<sub>.+)\\.dev.aidbox.io$, request: \"GET /favicon.ico HTTP/1.1\", upstream: \"http://127.0.0.1:8080/favicon.ico\", host: \"dev.aidbox.io\", referrer: \"http://dev.aidbox.io/ui\"")

(def a-log
  "16-03-30 14:26:26.448 ERROR [worker-1] default simply.core || java.lang.Exception: Test exception
     at dashboard.core$$test_exception.invoke (core.clj:58)
        clojure.lang.Var.invoke (Var.java:379)
        simply.core$dispatch$fn__23818.invoke (core.clj:103)
  ")

(comment 
  (parse-nginx-access na-log)
  (parse-nginx-error ne-log)
  (parse-app a-log))
