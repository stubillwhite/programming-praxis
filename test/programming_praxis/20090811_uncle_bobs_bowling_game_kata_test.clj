(ns programming-praxis.20090811-uncle-bobs-bowling-game-kata-test
  (:use
    [clojure.test]
    [midje.sweet]
    [programming-praxis.20090811-uncle-bobs-bowling-game-kata]))

(fact
  "score-game given gutter game then calculates correct score"
  (score-game (repeat 20 0)) => 0)

(fact
  "score-game given all ones then calculates correct score"
  (score-game (repeat 20 1)) => 20)

(fact
  "score-game given spare then calculates correct score"
  (score-game 
    (take 20 (concat [5 5 3] (repeat 0)))) => 16)

(fact
  "score-game given strike then calculates correct score"
  (score-game
    (take 20 (concat [10 3 4] (repeat 0)))) => 24)

(fact
  "score-game given perfect game then calculates correct score"
  (score-game (repeat 12 10)) => 300)

