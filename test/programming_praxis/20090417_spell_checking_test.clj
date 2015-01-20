(ns programming-praxis.20090417-spell-checking-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090417-spell-checking]))

(def test-trie
  (-> (trie)
    (add-to-trie (seq "cat"))
    (add-to-trie (seq "cart"))
    (add-to-trie (seq "dog"))
    (add-to-trie (seq "dogs"))))

(fact
  "trie-contains? given added to trie then true"
  (trie-contains? test-trie (seq "cat")) => true
  (trie-contains? test-trie (seq "cart")) => true
  (trie-contains? test-trie (seq "dog")) => true
  )

(fact
  "trie-contains? given not added to trie then false"
  (trie-contains? test-trie (seq "car")) => false
  (trie-contains? test-trie (seq "do")) => false
  (trie-contains? test-trie (seq "cats")) => false
  (trie-contains? (trie) (seq "car")) => false)

(def test-spell-checker
  (spell-checker "this is a test string of words"))

(fact
  "spelled-correctly? given word in corpus then true"
  (spelled-correctly? test-spell-checker "this") => true
  (spelled-correctly? test-spell-checker "string") => true)

(fact
  "spelled-correctly? given word not in corpus then false"
  (spelled-correctly? test-spell-checker "thi") => false
  (spelled-correctly? test-spell-checker "off") => false)
