(ns programming-praxis.20090306-roman-numerals-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090306-roman-numerals]))

(def single-digit-data
  { "I" 1
    "V" 5
    "X" 10
    "L" 50
    "C" 100
    "D" 500
    "M" 1000 })

(def additive-digit-data
  { "VIII" 8
    "XV"   15
    "LXXX" 80
    "CL"   150
    "DCCC" 800
    "MD"   1500 })

(def subtractive-digit-data
  { "IV"  4
    "IX"  9 })

(def sanity-check-data
  { "MCMLIV"    1954
    "MCMLXXXIV" 1984
    "MCMXC"     1990
    "MCMXCIX"   1999
    "MMXIV"     2014 })

(defn from-roman-gives-expected-decimal
  ([[r d]]
    (= (from-roman r) d)))

(defn to-roman-gives-expected-decimal
  ([[r d]]
    (= (to-roman d) r)))

(fact
  "from-roman given single-digit-data then returns expected decimal"
  (every? from-roman-gives-expected-decimal single-digit-data) => true)

(fact
  "from-roman given additive-digit-data then returns expected decimal"
  (every? from-roman-gives-expected-decimal additive-digit-data) => true)

(fact
  "from-roman given subtractive-digit-data then returns expected decimal"
  (every? from-roman-gives-expected-decimal subtractive-digit-data) => true)

(fact
  "from-roman given sanity-check-data then returns expected decimal"
  (every? from-roman-gives-expected-decimal sanity-check-data) => true)

(fact
  "to-roman given single-digit-data then returns expected decimal"
  (every? to-roman-gives-expected-decimal single-digit-data) => true)

(fact
  "to-roman given additive-digit-data then returns expected decimal"
  (every? to-roman-gives-expected-decimal additive-digit-data) => true)

(fact
  "to-roman given subtractive-digit-data then returns expected decimal"
  (every? to-roman-gives-expected-decimal subtractive-digit-data) => true)

(fact
  "to-roman given sanity-check-data then returns expected decimal"
  (every? to-roman-gives-expected-decimal sanity-check-data) => true)

(fact
  "to-roman given subtractive form number then returns additive form"
  (to-roman (from-roman "IIV")) => "III"
  (to-roman (from-roman "IIX")) => "VIII")
