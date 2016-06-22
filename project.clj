(defproject aidbox-monitoring "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :pedantic? true
  :warn true
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main aidbox-monitoring.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.1.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [clojurewerkz/elastisch "2.2.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [com.niquola/riemann "0.2.11-SNAPSHOT" :exclusions [org.slf4j/slf4j-api  org.jsoup/jsoup clj-http]]
                 [clj-time "0.11.0"]
                 [cheshire "5.5.0"]
                 [org.elasticsearch/elasticsearch "1.7.4"]
                 [org.apache.lucene/lucene-expressions "4.10.4"]])
