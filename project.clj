(defproject queue-writer "0.1.0-SNAPSHOT"
  :description "Pump data into a RabbitMQ queue for testing."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.novemberain/langohr "3.4.0"]
                 [org.clojure/data.json "0.2.6"]]
  :aot :all)
