(ns programming-praxis.20090417-spell-checking-test
  (:use
    [expectations]
    [programming-praxis.20090417-spell-checking]))

(def test-trie
  (-> (trie)
    (add-to-trie (seq "cat"))
    (add-to-trie (seq "cart"))
    (add-to-trie (seq "dog"))
    (add-to-trie (seq "dogs"))))

;; trie-contains? given added to trie then true
(expect true (trie-contains? test-trie (seq "cat")))
(expect true (trie-contains? test-trie (seq "cart")))
(expect true (trie-contains? test-trie (seq "dog")))

;; trie-contains? given not added to trie then false
(expect false (trie-contains? test-trie (seq "car")))
(expect false (trie-contains? test-trie (seq "do")))
(expect false (trie-contains? test-trie (seq "cats")))
(expect false (trie-contains? (trie) (seq "car")))

(def test-spell-checker
  (spell-checker "this is a test string of words"))

;; spelled-correctly? given word in corpus then true
(expect true (spelled-correctly? test-spell-checker "this"))
(expect true (spelled-correctly? test-spell-checker "string"))

;; spelled-correctly? given word not in corpus then false
(expect false (spelled-correctly? test-spell-checker "thi")) 
(expect false (spelled-correctly? test-spell-checker "off"))
