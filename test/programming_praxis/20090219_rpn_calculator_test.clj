(ns programming-praxis.20090219-rpn-calculator-test
  (:use
    [expectations]
    [programming-praxis.20090219-rpn-calculator]))

(defn calculate-result-and-capture-stdout
  ([expr]
    (with-out-str
      (calculate-result (create-calculator) expr))))

(expect
  "85.2974"
  (calculate-result-and-capture-stdout "19 2.14 + 4.5 2 4.3 / - * \n"))

(expect
  "3.0000"
  (calculate-result-and-capture-stdout "2 3 \n"))

(expect
  "5.0000"
  (calculate-result-and-capture-stdout "2 3 + \n"))

(expect
  "null"
  (calculate-result-and-capture-stdout "\n"))
