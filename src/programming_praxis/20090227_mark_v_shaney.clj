(ns programming-praxis.20090227-mark-v-shaney
  (:require
    [clojure.string :as string]
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; In the mid-1980’s, Mark V. Shaney posted messages such as this to the Usenet group net.singles:
;; 
;;     I seem to be important. For me, it would have agreed with the
;;     technical insight that is dear to me. Because of this, I have no
;;     advice for someone in that situation!
;; 
;;     Joining Mensa was something I did him one better. I wore a dress skirt
;;     a day for one week. I did him one better. I wore a dress skirt a day
;;     for a 2 year relationship. I’m wondering if anyone else out there has
;;     ever experienced this phenomena, whether it was actually your
;;     contention that this is true for me.
;; 
;;     I suppose it depends how you felt about someone before you became
;;     emotionally attached and therefore “safer” – not to sporting events,
;;     but to opera.
;; 
;;     I lost 90 lbs a few months during my “flower child” days in high school
;;     where, due to her high academic standings, was shunned by many of the
;;     tube. The experience really screwed them up — if not their heads,
;;     their knees. Why does one have to be the prime measurement of
;;     manhood. No?
;; 
;;     He was a scrawny, spastic nerd in high school, and I fantasized about
;;     such a thing. It all depends on the sidelines, listening to what makes
;;     the rest of the guys around her – suddenly finds herself in a situation
;;     where guys are asking them out!? But this can result in members of
;;     either the person of your dreams (in a larger number of males to
;;     females studying the field of engineering), the ratio of males to
;;     females is somewhere in the past. And, per the other person.
;; 
;;     I find it hard to reconcile the notion that something or someone isn’t
;;     theirs anymore. I have a date with the woman. Subjectively, I have
;;     also acted in this weekend.
;; 
;; However, Shaney wasn’t a person. Shaney was a bot created by three Bell Labs researchers — Bruce Ellis, Rob Pike and
;; Don Mitchell — that analyzed Usenet postings and then created its own posting. Shaney’s writings were quirky,
;; non-sensical, and beloved by many.
;; 
;; Shaney worked by reading a training text and saving each triple of words that appear in the training text in a large
;; table. Then it generates text using a markov chain, starting with two words that appear in the training text and
;; repeatedly writing the third word of the triple, sliding the output window from word to word. The genius of the
;; method is that any two words may appear in the training text with multiple following words, and the generator is free
;; to choose any of them; thus, short fragments of text make sense, but the text as a whole frequently veers from one
;; train of thought to another. A word includes its surrounding punctuation, so that sentence structure and, indirectly,
;; grammar, are built in to the output.
;; 
;; Write a program that implements Shaney.

;; Solution
;;
;; This was pretty fun. I wanted to make the text a little more realistic, so I weight the triples so more frequently
;; occurring triples are more likely to be selected. Feeding in some Lovecraft as the initial text results in some
;; rather insane ravings.
;;
;;     I insisted upon talking--nervously and elaborately explaining my condition. I told him of dreams for want of
;;     continuous context. Today I refuse to form a start that, even should I succeed in finding the door I had warned
;;     their company to a curious, sickly yellow-grey, and over again, and I screamed, but my eyes and the white fungous
;;     growths which occasionally appeared; these, and the legacy of eon-old Leng, and the search I had no better
;;     informed than he.

(defn tokenise
  ([s]
    (-> s
      (string/replace #"\r\n" " ")
      (string/split #" "))))

(defn create-fragments
  ([s]
    (partition 3 1 s)))

(defn inc-frequency-and-total
  ([frequencies word]
    (-> frequencies
      (update-in [word]   (fnil inc 0))
      (update-in [:total] (fnil inc 0)))))

(defn accumulate-text-frequencies
  ([model fragment]
    (let [[a b c] fragment]
      (update-in model [[a b]] (fn [x] (inc-frequency-and-total x c))))))

(defn create-markov-model
  ([{:keys [text] :as config}]
    (reduce
      accumulate-text-frequencies
      {}
      (-> text (tokenise) (create-fragments)))))



(defn random-transition
  ([{:keys [total] :as state-defn}]
    (comment println state-defn)
    (let  [ r (inc (rand-int total)) ]
      (loop [ running-total 0
              word-freqs    (seq (dissoc state-defn :total)) ]
        (comment printf "\nr %d, running-total %s, word-freqs %s" r running-total (first word-freqs))
        (let [[word count] (first word-freqs)]
          (if (<= r (+ running-total count))
            (do (comment println "=>" word)
              word)
            (recur (+ running-total count) (rest word-freqs))))))))

(defn next-word
  ([model curr-state]
    (if-let [ state-defn (get model curr-state) ]
      (random-transition state-defn))))

(defn step-model
  ([model state]
    (iterate
      (fn [[a b]] [b (next-word model [a b])])
      state)))

(defn starter-word?
  ([word]
    (re-find #"^[A-Z]" word)))

(defn random-initial-state
  ([model]
    (let [ initial-states (for [[a b] (keys model) :when (starter-word? a)] [a b]) ]
      (rand-nth initial-states))))

(defn generate-text
  ([{:keys [text length] :as config}]
    (let [ model (create-markov-model config)
           state (random-initial-state model) ]
      (string/join " "
        (take length
          (take-while (fn [x] (not (nil? x)))
            (map first
              (step-model model state))))))))

(defn execute-solution
  ([]
    (println
      (generate-text 
        { :text   (slurp "http://gutenberg.net.au/ebooks06/0600031.txt")
          :length 500 }))))
