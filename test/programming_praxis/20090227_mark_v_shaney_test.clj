(ns programming-praxis.20090227-mark-v-shaney-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090227-mark-v-shaney]))

(defn execute-text-generator
  ([text length]
    (generate-text { :text   text
                     :length length })))

(defn count-texts
  ([texts]
    (reduce
      (fn [acc t] (update-in acc [t] (fnil inc 0)))
      {}
      texts)))

(defn close?
  ([x y]
    (< (Math/abs (- x y)) (* 0.1 x))))

(fact
  "generate-text returns text matching corpus frequencies."
  (let [ text    "One two three. One two three. One two three. One two four. "
         results (count-texts (take 1000 (repeatedly #(execute-text-generator text 3)))) ]
    (close? (get results "One two three.") 750) => true
    (close? (get results "One two four.")  250) => true))








