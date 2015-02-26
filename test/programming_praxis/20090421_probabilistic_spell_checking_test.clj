(ns programming-praxis.20090421-probabilistic-spell-checking-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090421-probabilistic-spell-checking]))

(defn bin-to-str
  ([x]
    (apply str (reverse (for [bit (range 0 bits-per-byte)] (if (bit-test x bit) "1" "0"))))))

(defn get-array-as-str
  ([bf]
    (map bin-to-str (:array bf))))

(defn linear-hash-functions
  ([m]
     (for [n (range m)] (fn [_] n))))

(fact
  "add-item then sets bits indicated by hash functions"
  (let [ m  12 ]
    (-> (bloom-filter m (linear-hash-functions m))
      (add-item :foo)
      (get-array-as-str))) =>  ["11111111" "00001111"])

(defn salted-hash-functions
  ([m]
    (for [n (range m)]
      (let [salt (char (+ 65 n))]
        (fn [x] (mod (hash (str salt x salt)) m))))))

(fact
  "contains-item? given item present then true"
  (let [ m  12 ]
    (-> (bloom-filter m (salted-hash-functions m))
      (add-item :foo)
      (contains-item? :foo)) => true))

(fact
  "contains-item? given item missing then false"
  (let [ m  12 ]
    (-> (bloom-filter m (salted-hash-functions m))
      (add-item :foo)
      (contains-item? :bar)) => false))
