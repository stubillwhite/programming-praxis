(ns programming-praxis.20090421-probabilistic-spell-checking-test
  (:use
    [expectations]
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

;; add-item then sets bits indicated by hash functions
(expect
  ["11111111" "00001111"]
  (-> (bloom-filter 12 (linear-hash-functions 12))
    (add-item :foo)
    (get-array-as-str)))

(defn salted-hash-functions
  ([m]
    (for [n (range m)]
      (let [salt (char (+ 65 n))]
        (fn [x] (mod (hash (str salt x salt)) m))))))

(expect
  true
  (-> (bloom-filter 12 (salted-hash-functions 12))
    (add-item :foo)
    (contains-item? :foo)))

(expect
  false
  (-> (bloom-filter 12 (salted-hash-functions 12))
    (add-item :foo)
    (contains-item? :bar)))
