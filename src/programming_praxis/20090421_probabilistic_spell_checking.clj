(ns programming-praxis.20090421-probabilistic-spell-checking
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; In a previous exercise we built a spell checker based on storing words in a trie. That spell checker was exact: the
;; spell checker reported success if and only if the checked word was in the dictionary. Today we will build a spell
;; checker that is probabilistic: it correctly reports failure if the checked word is not in the dictionary, and
;; correctly reports success if the checked word is in the dictionary, but may also incorrectly report success even if
;; the checked word is not in the dictionary. The probability of error can be made arbitrarily small, as determined by
;; the programmer.
;; 
;; We will use a bloom filter, a data structure invented by Burton Bloom in 1970 to test membership is a set. A bloom
;; filter consists of an array of m bits, plus k different hash functions that map set elements to the range 0 to m-1.
;; All the bits are initially 0. To add an element, calculate the k hash values of the element, and set each kth bit to
;; 1. To test if an element is in the set, calculate the k hash values of the element, return true if all of the kth
;; bits are 1, and false if any is 0. In this way, it is certain that the element is not in the set if any hash returns
;; 0, but it is possible that an element not in the set may be incorrectly reported as being in the set if all of the
;; hashes return 1, but one of the hashes was set by some other element.
;; 
;; The easiest way to build a large number of hash functions is to use a single hash function and "salt" the dictionary
;; words with an additional letter. For instance, to hash the word "hello" three times, use "ahelloa", "bhellob", and
;; "chelloc" and hash with a standard string-hashing function.
;; 
;; There is some considerable math involved in determining the appropriate values of m and k. For a set of n elements,
;; the probability p of a false positive is given by the formula:
;; 
;;     (1 - (1 - 1/m) ^ kn) ^ k ~ ( 1 - e ^ (kn/m)) ^ k
;; 
;; To give this a sense of scale, storing a fifty thousand word dictionary in a bloom filter of a million bits using
;; seven hash functions will result in a false positive every 5102 words, on average.
;; 
;; Your task is to build a probabilistic spell checker as described above. When you are finished, you can read or run a
;; suggested solution, or post your solution or discuss the exercise in the comments below.

;; Solution
;;
;; I'm not sure if this is the most efficient way to do this, but I haven't played with Clojure's bitwise operations and
;; this seems like a fun excuse to do so. The description above isn't too clear to me; the one from Wikipedia makes it
;; clearer, I think:
;;
;;     An empty Bloom filter is a bit array of m bits, all set to 0. There must also be k different hash functions
;;     definted, each of which maps or hashes some set element to one of the m array positions with a uniform random
;;     distribution.
;;
;;     To add an element, feed it to each of the k hash functions to get k array positions. Set the bits at all these
;;     positions to 1.
;;
;;     To query for an element (test whether it is in the set), feed it to each of the k hash functions to get k array
;;     positions. If any of the bits at these positions are 0, the element is definitely not in the set.

(def bits-per-byte 8)

(defn- int-div
  ([num div]
    (int (/ num div))))

(defn- bit-location
  ([n]
    { :byte-idx (int-div n bits-per-byte)
      :bit-idx  (mod n bits-per-byte) }))

(defn- set-byte
  ([byte-array n byte]
    (concat
      (take n byte-array)
      [byte]
      (drop (inc n) byte-array))))

(defn- set-bit
  ([byte-array n]
    (let [ {:keys [byte-idx bit-idx]} (bit-location n)
           byte (nth byte-array byte-idx) ]
      (set-byte byte-array byte-idx (bit-set byte bit-idx)))))

(defn- test-bit
  ([byte-array n]
    (let [ {:keys [byte-idx bit-idx]} (bit-location n)
           byte (nth byte-array byte-idx) ]
      (bit-test byte bit-idx))))

(defn add-item
  "Returns bloom-filter updated to contain item."
  ([{:keys [array fs] :as bloom-filter} item]
    (assoc bloom-filter :array
      (reduce
        (fn [acc x] (set-bit acc x))
        array
        (for [f fs] (f item))))))

(defn contains-item?
  "Returns false if bloom-filter definitely does not contain item, true if it probably does not contain item."
  ([{:keys [array fs]} item]
    (reduce
      (fn [acc x] (and acc (test-bit array x)))
      1
      (for [f fs] (f item)))))

(defn bloom-filter
  "Returns a bloom filter of size m with hash functions fs, where each function returns a hash for a value in the range of zero to m."
  ([m fs]
    (let [size (inc (int-div m bits-per-byte))]
      { :array (byte-array size)
        :fs    fs } )))
