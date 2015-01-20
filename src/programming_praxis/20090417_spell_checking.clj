(ns programming-praxis.20090417-spell-checking
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Spell checkers are ubiquitous. Word processors have spell checkers, as do browser-based e-mail clients. They all work
;; the same way: a dictionary is stored in some data structure, then each word of input is submitted to a search in the
;; data structure, and those that fail are flagged as spelling errors. There is a certain art to building the word list
;; on which a spell checker is based; Exxon isn’t a word in anybody’s dictionary, but it is likely included in the word
;; list, but cwm (a geological structure), which is certainly a word, is most likely omitted from the word list, on the
;; grounds it is more likely an error than a legitimate spelling (unless you are a geologist).
;;
;; There are many appropriate data structures to store the word list, including a sorted array accessed via binary
;; search, a hash table, or a bloom filter. In this exercise you are challenged to store the word list
;; character-by-character in a trie. Consider this sample trie that stores the words CAR, CART, CAT and DOG:
;;
;;                                              .
;;                                            c/ \d 
;;                                           a/   \o
;;                                          r/\t   \g 
;;                                         t/
;;
;; To see that CAT is a valid spelling, follow the C branch, then the A branch, then the T branch, where you find a
;; filled circle, indicating a word. To see that CAB is not a valid spelling, follow the C branch, then the A branch,
;; and fail when there is no B branch.
;;
;; Tries can be very fast; for instance, to see that QARTER (a misspelling of QUARTER) is not a word, follow the Q
;; branch, then fail when there is no A branch. This is even faster than hashing, which must read all six letters of
;; QARTER just to compute the hash value. And tries can also be space-efficient, since space is shared between common
;; prefixes.
;;
;; Your task is to build a spell checker based on tries.

(defn trie
  ([] {}))

(defn add-to-trie
  ([trie x]
    (assoc-in trie (concat x [:leaf]) true)))

(defn trie-contains?
  ([trie x]
    (true? (get-in trie (concat x [:leaf])))))

(defn spell-checker
  ([word-list]
    (reduce
      add-to-trie
      (trie)
      (map seq (clojure.string/split word-list #" ")))))

(defn spelled-correctly?
  ([checker word]
    (trie-contains? checker (seq word))))

(defn execute-solution
  ([]
    (let [ url       "https://raw.githubusercontent.com/first20hours/google-10000-english/master/google-10000-english.txt"
           word-list (clojure.string/replace (slurp url) #"\n" " ")
           checker   (spell-checker word-list) ]
      (assert (spelled-correctly? checker "this"))
      (assert (spelled-correctly? checker "rehabilitation"))
      (assert (not (spelled-correctly? checker "beable"))))))
