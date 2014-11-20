(ns programming-praxis.20141114-dawkins-weasel
  (:require
    [taoensso.timbre :as timbre]))

;; In his book The Blind Watchmaker, Richard Dawkins says:
;;
;;    I don’t know who it was first pointed out that, given enough time, a monkey bashing away at random on a typewriter
;;    could produce all the works of Shakespeare. The operative phrase is, of course, given enough time. Let us limit
;;    the task facing our monkey somewhat. Suppose that he has to produce, not the complete works of Shakespeare but
;;    just the short sentence ‘Methinks it is like a weasel’, and we shall make it relatively easy by giving him a
;;    typewriter with a restricted keyboard, one with just the 26 (capital) letters, and a space bar. How long will he
;;    take to write this one little sentence?
;;
;; (All of our quotes are shamelessly stolen from Wikipedia.) Then Dawkins suggests that this is a bad analogy for
;; evolution, and proposes this alternative:
;;
;;    We again use our computer monkey, but with a crucial difference in its program. It again begins by choosing a
;;    random sequence of 28 letters, just as before … it duplicates it repeatedly, but with a certain chance of random
;;    error – ‘mutation’ – in the copying. The computer examines the mutant nonsense phrases, the ‘progeny’ of the
;;    original phrase, and chooses the one which, however slightly, most resembles the target phrase, METHINKS IT IS
;;    LIKE A WEASEL.
;;
;; Then he discusses a computer program that simulates his monkey, which he says took half an hour to run because it was
;; written BASIC; when he rewrote his program in Pascal the runtime dropped to eleven seconds. His program used this
;; algorithm:
;;
;; - Start with a random string of 28 characters.
;; - Make 100 copies of the string (reproduce).
;; - For each character in each of the 100 copies, with a probability of 5%, replace (mutate) the character with a new
;;   random character.
;; - Compare each new string with the target string “METHINKS IT IS LIKE A WEASEL”, and give each a score (the number of
;;   letters in the string that are correct and in the correct position).
;; - If any of the new strings has a perfect score (28), halt. Otherwise, take the highest scoring string, and go to
;;   step 2.

;; Solution

;; Represent the string as a vector for easy access

(defn create-individual
  ([s]
    (vec (clojure.string/upper-case s))))

;; Configuration

(def ideal (create-individual "Methinks it is like a weasel"))
(def genes (concat (list \space) (map char (range 65 91))))
(def mutation-probability 0.05)
(def max-generations 1000)

;; Creating the population

(defn random-gene
  ([]
    (rand-nth genes)))

(defn create-random-individual
  ([]
    (for [x (range (count ideal))] (rand-nth genes))))

(defn create-population
  ([count]
    (for [x (range count)] (create-random-individual))))

;; Calculating fitness

(defn fitness
  ([ind]
    (reduce +
      (for [x (range (count ideal))] (if (= (nth ind x) (nth ideal x)) 1 0)))))

(defn fittest-individual
  ([population]
    (last (sort-by fitness population))))

;; Mutating the population

(defn mutate-individual
  ([ind]
    (let [ should-mutate? (fn [] (< (/ (rand-int 100) 100) mutation-probability))
           mutate-gene    (fn [x] (if (should-mutate?) (rand-nth genes) x)) ]
      (map mutate-gene ind))))

(defn mutate-population
  ([population]
    (let [ fittest (fittest-individual population) ]
      (repeatedly (count population) (fn [] (mutate-individual fittest))))))

;; Running the simulation

(defn run-simulation
  ([population f]
    (loop [ gen 0
            pop population ]
      (let [ fittest (fittest-individual pop) ]
        (printf "Generation %d, fittest [%s], fitness %d\n" gen (apply str fittest) (fitness fittest))
        (when (and (< gen max-generations) (< (fitness fittest) (fitness ideal)))
          (recur (inc gen) (f pop)))))))

(defn execute-solution
  ([]
    (run-simulation (create-population 100) mutate-population)))



