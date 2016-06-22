(ns aidbox-monitoring.core-test
  (:require [clojure.test :refer :all]
            [aidbox-monitoring.parser :refer :all]
            [aidbox-monitoring.core :refer :all]))

(def nginx-access
  [["87.249.45.131 - - [29/Mar/2016:12:33:08 +0000] \"GET /boxes HTTP/1.1\" 200 2325 \"https://aidbox.io/ui\" \"Mozilla/5.0  (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/49.0.2623.87 Chrome/49.0.2623.87 Safari/537.36\""
    {:ip "87.249.45.131" :date "29/Mar/2016:12:33:08 +0000" :method "GET" :url "/boxes" :statuscode "200" :bytessent "2325" :refferer "https://aidbox.io/ui" :useragent "Mozilla/5.0  (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/49.0.2623.87 Chrome/49.0.2623.87 Safari/537.36"} ]

   ["87.249.45.131 - - [29/Mar/2016:12:33:08 +0000] \"POST /boxes/12/ui HTTP/1.1\" 400 2325 \"https://aidbox.io/ui/users\" \"Mozilla/5.0  (X11; Linux x86_64) AppleWebKit/537.36\""
    {:ip "87.249.45.131" :date "29/Mar/2016:12:33:08 +0000" :method "POST" :url "/boxes/12/ui" :statuscode "400" :bytessent "2325" :refferer "https://aidbox.io/ui/users" :useragent "Mozilla/5.0  (X11; Linux x86_64) AppleWebKit/537.36"} ] ])

(def nginx-error
  [["2016/03/23 16:11:57 [error] 30770#0: *3 connect() failed (111: Connection refused) while connecting to upstream, client: 87.249.45.131, server: ~^(?<sub>.+)\\.dev.aidbox.io$, request: \"GET /favicon.ico HTTP/1.1\", upstream: \"http://127.0.0.1:8080/favicon.ico\", host: \"dev.aidbox.io\", referrer: \"http://dev.aidbox.io/ui\"" 
  {:date "2016/03/23 16:11:57" :status "error" :errorcode "30770#0" :errmessage "*3 connect() failed (111: Connection refused) while connecting to upstream, client: 87.249.45.131, server: ~^(?<sub>.+)\\.dev.aidbox.io$, request: \"GET /favicon.ico HTTP/1.1\", upstream: \"http://127.0.0.1:8080/favicon.ico\", host: \"dev.aidbox.io\", referrer: \"http://dev.aidbox.io/ui\"" }]

  ["2016/03/23 16:25:49 [notice] 1447#0: signal process started"
   {:date "2016/03/23 16:25:49" :status "notice" :errorcode "1447#0" :errmessage "signal process started"}]])

(def app-log
  [["16-03-30 13:20:49.051 INFO [worker-1] default simply.core || :get   /test-exception"
    {:date "16-03-30 13:20:49.051" :status "INFO" :worker "worker-1" :context "default" :logger "simply.core" :message ":get   /test-exception"  } ]
   ["16-03-30 13:20:49.068 WARN [worker-1] default simply.db || DEPRECATED db/q-one*" 
    {:date "16-03-30 13:20:49.068" :status "WARN" :worker "worker-1" :context "default" :logger "simply.db" :message "DEPRECATED db/q-one*"  }]
   ["16-03-30 14:26:26.448 ERROR [worker-1] default simply.core || java.lang.Exception: Test exception
     at dashboard.core$$test_exception.invoke (core.clj:58)
        clojure.lang.Var.invoke (Var.java:379)
        simply.core$dispatch$fn__23818.invoke (core.clj:103)"
    {:date "16-03-30 14:26:26.448" :status "ERROR" :worker "worker-1" :context "default" :logger "simply.core" :message "java.lang.Exception: Test exception
     at dashboard.core$$test_exception.invoke (core.clj:58)
        clojure.lang.Var.invoke (Var.java:379)
        simply.core$dispatch$fn__23818.invoke (core.clj:103)"  } ] ])

(deftest nginx-access-log
  (testing "Nginx access log"
    (doseq [[req res] nginx-access]
      (is (= (parse-nginx-access req)  res))))
  (testing "Nginx error log"
    (doseq [[req res] nginx-error]
      (is (= (parse-nginx-error req)  res))))
  (testing "App log"
    (doseq [[req res] app-log]
      (is (= (parse-app req)  res)))))

(run-tests)
