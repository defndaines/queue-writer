(ns queue-writer.core
  (:gen-class)
  (:require [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.basic :as lb]
            [clojure.data.json :as json]))

(def ^{:const true}
  default-exchange-name "")

(def msg {:routing_key "routing.key.here"
          :item_type "item-type"
          :item_id "54e74bceaf412"
          :operation "operation-here"
          :signature nil
          :sub_property "eventual"
          :sent "2015-02-20 06:59:26"
          :status "queue"
          :body {:__parameters {:id "6655321"}
                 :__user "1337"
                 :__context nil}})

(defn- day [n]
  (if (< n 10)
    (str 0 n)
    n))

(defn- unique-by-day
  "Inject n as the day into the sent field, and modify the id"
  [n]
  (-> msg
      (assoc :sent (str "2015-09-" (day n) " 06:59:26"))
      (assoc-in [:body :__parameters :id] (str 6655321 n))))

(defn- day-and-user [n]
  (json/write-str (unique-by-day n)))

(defn- diff-op
  "Inject n as the day and change the item_type"
  [n]
  (json/write-str (assoc (unique-by-day n) :item_type "not-specified-yet")))

(defn -main [& args]
  (let [qname (first args)
        conn (rmq/connect)
        ch (lch/open conn)]
    (doseq [x (range 1 21)]
      (lb/publish ch default-exchange-name qname (day-and-user x) {:content-type "text/plain"})
      (lb/publish ch default-exchange-name qname (diff-op x) {:content-type "text/plain"}))
    (rmq/close ch)
    (rmq/close conn)))
