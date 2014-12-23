(ns programming-praxis.20090306-roman-numerals-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090306-roman-numerals]))

(fact
  "from-roman given single digit then returns decimal"
  (from-roman "I") => 1
  (from-roman "V") => 5
  (from-roman "X") => 10
  (from-roman "L") => 50
  (from-roman "C") => 100
  (from-roman "D") => 500
  (from-roman "M") => 1000)

(fact
  "from-roman given additive digits then returns decimal"
  (from-roman "VIII") => 8
  (from-roman "XV")   => 15
  (from-roman "LXXX") => 80
  (from-roman "CL")   => 150
  (from-roman "DCCC") => 800
  (from-roman "MD")   => 1500)

(fact
  "from-roman given subtractive digits then returns decimal"
  (from-roman "IIV") => 3
  (from-roman "IV")  => 4
  (from-roman "IIX") => 8
  (from-roman "IX")  => 9)

(fact
  "from-roman given sanity check then returns decimal"
  (from-roman "MCMLIV")    => 1954
  (from-roman "MCMLXXXIV") => 1984
  (from-roman "MCMXC")     => 1990
  (from-roman "MCMXCIX")   => 1999
  (from-roman "MMXIV")     => 2014)
