(ns programming-praxis.20120127-anagram-phrases
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Words that are formed from the same set of letters are anagrams of each other. For instance, pots, post, stop, spot,
;; opts, and tops are anagrams. We studied anagrams in a previous exercise.
;;
;; Anagrams can be extended from single words to phrases. For instance, "gin grammar prop six" and "maxim prong rasp
;; rig" are anagrams for "programming praxis."

;; Solution
;;
;; Clojure has the fantastic clojure.math.combinatorics/permutations for generating permutations, but that seems like
;; cheating so let's do this from first principles.
;;
;; I started looking into how to do this lazily and came across lexicographic permutations. There's a very clear
;; example here: http://www.nayuki.io/page/next-lexicographical-permutation-algorithm
;;
;;     Initial sequence                                       0  1  2   5  3  3  0
;;     Find longest non-increasing suffix                     0  1  2  [5  3  3  0]
;;     Identify pivot                                         0  1 (2) [5  3  3  0]
;;     Find rightmost successor to the pivot in the suffix    0  1 (2) [5  3 (3) 0]
;;     Swap with the pivot                                    0  1 (3) [5  3 (2) 0]
;;     Reverse the suffix                                     0  1  3  [0  2  3  5]
;;     Done                                                   0  1  3   0  2  3  5 

(defn- suffix-index
  ([sequence]
    (loop [i (dec (count sequence))]
      (if (or (= i 0) (< (nth sequence (dec i)) (nth sequence i)))
        i
        (recur (dec i))))))

(defn- successor-index
  ([pivot-index suffix-index sequence]
    (let [ pivot (nth sequence pivot-index)
           succ  (->> (drop suffix-index sequence)
                   (filter (fn [x] (> x pivot)))
                   (reduce min)) ]
      (loop [i (dec (count sequence))]
        (if (= succ (nth sequence i))
          i
          (recur (dec i)))))))

(defn- swap
  ([sequence a b]
    (let [s (vec sequence)]
      (assoc s
        a (s b)
        b (s a)))))

(defn- sort-suffix
  ([sequence n]
    (concat
      (take n sequence)
      (sort (drop n sequence)))))

(defn- next-permutation
  ([sequence]
    (let [suffix-index (suffix-index sequence)]
      (when (not (zero? suffix-index))
        (let [ pivot-index     (dec suffix-index)
               successor-index (successor-index pivot-index suffix-index sequence) ]
          (sort-suffix (swap sequence pivot-index successor-index) suffix-index))))))

(defn permutations
  ([sequence]
    (take-while (complement nil?)
      (iterate next-permutation (sort sequence)))))


