(ns programming-praxis.20100629-world-cup-prognostication
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; On Saturday morning, inspired by Andrew Moylan’s article on Wolfram’s Blog, I sat down to work out a simulation of
;; the knockout stage of the World Cup competition. I used the bracket shown at right, and found elo ratings of the
;; sixteen teams, as of that morning, at Wikipedia:
;; 
;;  1 BRA Brazil        2082
;;  2 ESP Spain         2061
;;  3 NED Netherlands   2045                        Uruguay                   Argentina
;;  4 ARG Argentina     1966                        Korea                        Mexico
;;  5 ENG England       1945                        
;;  6 GER Germany       1930                        United States               Germany
;;  7 URU Uruguay       1890                        Ghana                       England
;;  8 CHI Chile         1883                                                  
;;  9 POR Portugal      1874                                                  
;; 10 MEX Mexico        1873                        Netherlands                Paraguay
;; 15 USA United States 1785                        Slovakia                      Japan
;; 19 PAR Paraguay      1771                                                  
;; 25 KOR Korea         1746                        Brazil                        Spain
;; 26 JPN Japan         1744                        Chile                      Portugal
;; 32 GHA Ghana         1711
;; 45 SVK Slovakia      1654
;; 
;; The table shows that there are forty-four national teams with ratings higher than Slovakia’s rating of 1654; they are
;; lucky to be in the tournament.
;; 
;; The likelihood that a team will win its match can be computed from the elo rankings of the team and its opponent
;; according to the formula:
;;
;;     1 / (1 + 10^( (their-rating - our-rating)/400 ))
;;
;; Thus, the United States had a 60.5% expectation of winning its match against Ghana this afternoon, and Ghana had a
;; 39.5% expectation of defeating the United States. Harrumph!
;; 
;; Every time a match is played, the elo rating of a team changes. The amount of the change is based on the actual
;; result as compared to the expected result. If a team wins when they have a high expectation of winning, their elo
;; rating goes up by a small amount, since they were expected to win. However, if a team wins when they have a low
;; expectation of winning, their elo rating goes up by a large amount. The formula is:
;;
;;     new-rating = old-rating + KG(W - We)
;;
;; where K is a weighting for the importance of the game (K is 60 for the World Cup), G is a parameter based on the goal
;; differential (we’ll assume that all games are won by a single goal, so G = 1), W is 1 for a win and 0 for a loss, and
;; We is the winning expectation calculated by the formula given above.
;; 
;; Your task is to use the data and formulas described above to simulate the knockout stage of the World Cup a million
;; times and report the number of times each nation wins. When you are finished, you are welcome to read or run a
;; suggested solution, or to post your own solution or discuss the exercise in the comments below.

;; Solution
;;
;; I've done ELO calculations before and this looked like fun. We can represent the tournament as a tree, in which case
;; the evaluation of the brackets is just a depth-first walk.

(defn- participant
  ([name rating]
    { :name   name
      :rating rating }))

(defn- winning-expectation
  ([a b k g]
    (let [ rating-a (double (:rating a))
           rating-b (double (:rating b)) ]
      (/ 1 (+ 1 (Math/pow 10.0 (/ (- rating-b rating-a) 400)))))))

(defn- new-rating
  ([a b w k g]
    (let [ rating-a    (:rating a)
           winning-exp (winning-expectation a b k g) ]
      (+ rating-a (* k g (- w winning-exp))))))

(def participants
  (let [data [ "BRA" "Brazil"        2082
               "ESP" "Spain"         2061
               "NED" "Netherlands"   2045
               "ARG" "Argentina"     1966
               "ENG" "England"       1945
               "GER" "Germany"       1930
               "URU" "Uruguay"       1890
               "CHI" "Chile"         1883
               "POR" "Portugal"      1874
               "MEX" "Mexico"        1873
               "USA" "United States" 1785
               "PAR" "Paraguay"      1771
               "KOR" "Korea"         1746
               "JPN" "Japan"         1744
               "GHA" "Ghana"         1711
               "SVK" "Slovakia"      1654 ]]
    (reduce
      (fn [acc [code name rating]] (assoc acc (keyword code) (participant name (double rating))))
      {}
      (partition 3 data))))

(def brackets
  [ [ [ [:URU :KOR]
        [:USA :GHA] ]
      [ [:NED :SVK]
        [:BRA :CHI] ] ]
    [ [ [:ARG :MEX]
        [:GER :ENG] ]
      [ [:PAR :JPN]
        [:ESP :POR] ] ] ])

(def tournament
  { :participants participants
    :brackets     brackets })

(defn- summarise-participant
  ([x]
    (format "%s [%.0f]" (:name x) (:rating x))))

(defn- summarise-result
  ([a b winner loser]
    (printf "%-20s vs     %-20s : %s eliminates %s \n"
      (summarise-participant a)
      (summarise-participant b)
      (summarise-participant winner)
      (summarise-participant loser))))

(defn- simulate-match
  ([[a b]]
    (let [ r          (rand)
           p          (winning-expectation a b 60 1)
           winner     (if (< r p) a b)
           loser      (if (= winner a) b a)
           new-winner (assoc-in winner [:rating] (new-rating winner loser 1 60 1))
           new-loser  (assoc-in loser  [:rating] (new-rating loser winner 0 60 1)) ]
      (summarise-result a b new-winner new-loser)
      new-winner)))

(defn simulate-tournament
  ([tournament]
    (let [ participants     (:participants tournament)
           simulate-bracket (fn [x] (if (keyword? x) (get participants x) (simulate-match x))) ]
      (println "Winner: "
        (summarise-participant
          (clojure.walk/postwalk simulate-bracket (:brackets tournament)))))))

;(simulate-tournament tournament)


