(ns programming-praxis.20090219-rpn-calculator-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090219-rpn-calculator]))

(defn calculate-result-and-capture-stdout
  ([expr]
    (with-out-str
      (calculate-result (create-calculator) expr))))

(fact
  "calculate-result given test expression then prints test result"
  (calculate-result-and-capture-stdout "19 2.14 + 4.5 2 4.3 / - * \n") => "85.2974")

(fact
  "calculate-result given newline and unevaluated operands then prints top operand"
  (calculate-result-and-capture-stdout "2 3 \n") => "3.0000")

(fact
  "calculate-result given newline and evaluated operands then prints top operand"
  (calculate-result-and-capture-stdout "2 3 + \n") => "5.0000")

(fact
  "calculate-result given newline and no operands then prints null"
  (calculate-result-and-capture-stdout "\n") => "null")
