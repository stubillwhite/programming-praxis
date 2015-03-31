(ns programming-praxis.20090227-mark-v-shaney-test
  (:use
    [expectations]
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

(defn approximately
  ([x]
    (fn [y] (< (Math/abs (- x y)) (* 0.1 x)))))

;; generate-text returns text matching corpus frequencies.
(expect-let [ text         "One two three. One two three. One two three. One two four. "
              example-text (take 1000 (repeatedly #(execute-text-generator text 3))) ]
  (more->
    (approximately 750) (get "One two three.")
    (approximately 250) (get "One two four."))
  (count-texts example-text))








