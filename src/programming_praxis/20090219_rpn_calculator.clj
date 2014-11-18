(ns programming-praxis.20090219-rpn-calculator
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Implement an RPN calculator that takes an expression like 19 2.14 + 4.5 2 4.3 / - * which is usually expressed as (19
;; + 2.14) * (4.5 - 2 / 4.3) and responds with 85.2974. The program should read expressions from standard input and
;; print the top of the stack to standard output when a newline is encountered. The program should retain the state of
;; the operand stack between expressions.

;; Solution
;;
;; I haven't bothered with the reading from stdin part of the problem. It's pretty trivial to code but screws up my REPL
;; in Emacs.

(defn create-calculator
  "Returns a new calculator."
  ([]
    { :operand-stack []
      :operators     { "+" +
                       "-" -
                       "/" /
                       "*" * }}))

(defn- operator?
  ([{:keys [operators]} token]
    (contains? operators token)))

(defn- newline?
  ([token]
    (= token "\n")))

(defn- evaluate-expression
  ([{:keys [operand-stack operators] :as calc} token]
    (let [ [a b]  (take 2 operand-stack)
           f      (get operators token)
           result (f b a)]
      (update-in calc [:operand-stack] (fn [x] (cons result (drop 2 operand-stack)))))))

(defn- display-top-operand
  ([{:keys [operand-stack] :as calc}]
    (printf "%.4f" (first operand-stack))
    calc))

(defn- push-operand
  ([calc operand]
    (update-in calc [:operand-stack] (fn [x] (cons (Double. operand) x)))))

(defn- process-token
  ([calc token]
    (cond
      (operator? calc token) (evaluate-expression calc token)
      (newline? token)       (display-top-operand calc)
      :else                  (push-operand calc token))))

(defn- tokenise
  ([s]
    (clojure.string/split s #" ")))

(defn calculate-result
  "Returns calculator calc updated with the result of evaluating expression expr."
  ([calc expr]
    (reduce
      (fn [calc x] (process-token calc x))
      calc
      (tokenise expr))))
