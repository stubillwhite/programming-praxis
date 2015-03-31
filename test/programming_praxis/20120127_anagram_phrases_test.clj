(ns programming-praxis.20120127-anagram-phrases-test
  (:use
    [expectations]
    [programming-praxis.20120127-anagram-phrases]))

;; permutations given single item then single item
(expect [ [1] ] (permutations [1]))

;; permutations given unique items then all permutations
(expect
  [ [1 2 3] [1 3 2]
    [2 1 3] [2 3 1]
    [3 1 2] [3 2 1] ]
  (permutations [1 2 3]))

;; permutations given duplicate items then all permutations
(expect
  [ [1 2 2] [2 1 2] [2 2 1] ]
  (permutations [1 2 2]))
