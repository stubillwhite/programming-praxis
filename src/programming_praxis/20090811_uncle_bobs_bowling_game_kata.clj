(ns programming-praxis.20090811-uncle-bobs-bowling-game-kata
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Someone mentioned Uncle Bob’s Bowling Game Kata to me a few days ago; it is apparently famous as a tutorial on
;; object-oriented programming and test-driven development, though I had never heard of either Uncle Bob or his Bowling
;; Game Kata.
;; 
;; A game of tenpins bowling lasts ten frames, in each of which the bowler makes one or two attempts to knock down ten
;; pins arranged in a triangle. If the bowler knocks down all ten pins on the first attempt (that’s called a “strike”),
;; he scores ten pins plus the number of pins knocked down on his next two rolls. If the bowler knocks down all ten pins
;; after two attempts (that’s called a “spare”), he scores ten pins plus the number of pins knocked down on his next
;; roll. If the bowler fails to knock down all ten pins (that’s called an “open frame”), he scores the number of pins he
;; knocked down. The scores accumulate through all ten frames. At the last frame, if necessary, the pins are reset for
;; one or two additional rolls to count the final bonus. The sample scoresheet below shows how the calculations work:
;; 
;;      1 4 | 4 5 | 6 / | 5 / |   X | 0 1 | 7 / | 6 / |   X | 2 / 6
;;        5 |  14 |  29 |  49 |  60 |  61 |  77 |  97 | 117 | 133
;; 
;; For instance, the score in the second frame is 9, the sum of the two balls in the open frame. The score in the third
;; frame is 15, which is 10 for the spare plus 5 on the next ball. The score in the ninth frame is 20, which is 10 for
;; the strike plus 10 for the spare on the first two balls of the tenth frame. The score in the tenth frame is 16, which
;; is 10 for the spare plus 6 for the extra ball, which is a bonus ball not really part of any frame (the two balls of
;; the tenth frame have already been rolled).
;; 
;; Your task is to write a function that calculates the score of a tenpins bowling game. When you are finished, you are
;; welcome to read or run a suggested solution, or to post your solution or discuss the exercise in the comments below.

;; Scoring bowling
;;
;; The game consists of 10 frames as shown above. In each frame the player has two opportunities to knock down 10 pins.
;; The score for the frame is the total number of pins knocked down, plus bonuses for strikes and spares.
;; 
;; A spare is when the player knocks down all 10 pins in two tries. The bonus for that frame is the number of pins
;; knocked down by the next roll. So in frame 3 above, the score is 10 (the total number knocked down) plus a bonus of 5
;; (the number of pins knocked down on the next roll.)
;; 
;; A strike is when the player knocks down all 10 pins on his first try. The bonus for that frame is the value of the
;; next two balls rolled.
;; 
;; In the tenth frame a player who rolls a spare or strike is allowed to roll the extra balls to complete the frame.
;; However no more than three balls can be rolled in tenth frame.
