(ns programming-praxis.20090306-roman-numerals
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Roman numerals are a number-notation system developed in classical Rome, chiefly used today to indicate the year in
;; which a motion picture was made, or the sequence number of a Super Bowl.
;; 
;; Roman numerals use letters of the alphabet to indicate numerical value, according to the following code:
;; 
;;     I 1    V 5    X 10    L 50    C 100    D 500    M 1000
;; 
;; For example, the number 1732 is represented by Roman numerals as MDCCXXXII, and the number 1956 is represented by
;; Roman numerals as MDCCCCLVI. Letter symbols are normally written from the largest symbol to the smallest, left to
;; right, so the numeric values are additive. However, in order to conserve space, it is permissible to replace four of
;; the same symbol written all in a row in a subtractive manner to the left of a higher-value symbol, so that 1956 may
;; also be represented as MCMLVI, where the CM symbol, with C before M, indicates that C is subtracted from M, and thus
;; indicates the numeric value 900. Wikipedia and MathWorld explain the common usage of Roman numerals.
;; 
;; Your task is to write a function that takes two roman numerals (character strings) as input and returns their sum as
;; a roman numeral as output. Be sure that input can be given in either the additive or subtractive forms of Roman
;; numerals; give output using the subtractive form. What is add-roman("CCCLXIX", "CDXLVIII")?

;; Solution
;;  - Convert the digits to their decimal equivalent
;;  - Reverse the digits so the least significant digit is first
;;  - For each digit
;;     - If the digit is more than the maximum then add it to the sum
;;     - If the digit is less than the maximum then deduct it from the sum

(def from-roman-digits
  { \I 1
    \V 5
    \X 10
    \L 50
    \C 100
    \D 500
    \M 1000 })

(defn- accumulate-digits
  ([{:keys [sum max-val] :as acc} digit]
    (-> acc
      (assoc :sum     (if (>= digit max-val) (+ sum digit) (- sum digit)))
      (assoc :max-val (max max-val digit)))))

(defn from-roman
  ([roman-number]
    (let [ digits (reverse (map from-roman-digits (seq roman-number))) ]
      (:sum
        (reduce
          accumulate-digits
          { :sum 0 :max-val 0 }
          digits)))))

(def to-roman-digits
  [ [1000 "M"]
    [900  "CM"]
    [500  "D"]
    [400  "CD"]
    [100  "C"]
    [90   "XC"]
    [50   "L"]
    [40   "XL"]
    [10   "X"]
    [9    "IX"]
    [5    "V"]
    [4    "IV"]
    [1    "I"] ])

(defn first-val
  ([x]
    (first (drop-while (fn [[decimal-digit roman-digit]] (> decimal-digit x)) to-roman-digits))))

(defn to-roman
  ([decimal-number]
    (loop [ decimal decimal-number
            roman   nil ]
      (let [[decimal-digit roman-digit] (first-val decimal)]
        (if (= 0 decimal)
          roman
          (recur
            (- decimal decimal-digit)
            (str roman roman-digit)))))))

(defn add-roman
  ([a b]
    (+ (from-roman a) (from-roman b))))

(defn execute-solution
  ([]
    (to-roman (add-roman "CCCLXIX" "CDXLVIII"))))
