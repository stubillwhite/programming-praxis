(ns programming-praxis.20120127-anagram-phrases-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20120127-anagram-phrases]))

(fact
  "permutations given single item then single item"
  (permutations [1]) => [ [1] ])

(fact
  "permutations given unique items then all permutations"
  (permutations [1 2 3]) => [ [1 2 3] [1 3 2]
                              [2 1 3] [2 3 1]
                              [3 1 2] [3 2 1] ])

(fact
  "permutations given duplicate items then all permutations"
  (permutations [1 2 2]) => [ [1 2 2] [2 1 2] [2 2 1] ])
