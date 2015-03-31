(ns programming-praxis.20090811-uncle-bobs-bowling-game-kata-test
  (:use
    [expectations]
    [programming-praxis.20090811-uncle-bobs-bowling-game-kata]))

;; score-game given gutter game then calculates correct score
(expect 
  0
  (score-game (repeat 20 0)))

;; score-game given all ones then calculates correct score
(expect 
  20
  (score-game (repeat 20 1)))

;; score-game given spare then calculates correct score
(expect 
  16
  (score-game (take 20 (concat [5 5 3] (repeat 0)))))

;; score-game given strike then calculates correct score
(expect 
  24
  (score-game (take 20 (concat [10 3 4] (repeat 0)))))

;; score-game given perfect game then calculates correct score
(expect 
  300
  (score-game (repeat 12 10)))
