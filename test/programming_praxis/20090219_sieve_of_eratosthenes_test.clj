(ns programming-praxis.20090219-sieve-of-eratosthenes-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090219-sieve-of-eratosthenes]))

(fact
  (sieve-of-eratosthenes 2) => [2])

(fact
  (sieve-of-eratosthenes 3) => [2 3])

(fact
  (sieve-of-eratosthenes 5) => [2 3 5])

(fact
  (sieve-of-eratosthenes 30) => [2 3 5 7 11 13 17 19 23 29])
