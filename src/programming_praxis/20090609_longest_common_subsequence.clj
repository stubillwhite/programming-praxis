(ns programming-praxis.20090606-longest-common-subsequence
  (:require
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

;; Finding the longest common subsequence of two sequences is a classic computer science problem with an equally classic
;; solution that dates to the folklore of computing. The longest common subsequence is the longest set of elements that
;; appear in order (not necessarily contiguous) in two sequences; for instance, the longest common subsequence of
;; PROGRAMMING and PRAXIS is PRAI:
;; 
;; P R   O G R   A   M M   I   N G
;; | |           |         |
;; P R           A    X    I    S
;; 
;; The classic solution uses dynamic programming. A matrix is prepared with one sequence in the rows and the other
;; sequence in the columns, giving in each cell the minimum edit distance between the two, where the edit distance is
;; the least number of adds, deletes and changes that can convert one sequence to the other. For instance:
;; 
;;     P R O G R A M M I N G
;;   0 0 0 0 0 0 0 0 0 0 0 0
;; P 0 1 1 1 1 1 1 1 1 1 1 1
;; R 0 1 2 2 2 2 2 2 2 2 2 2
;; A 0 1 2 2 2 2 3 3 3 3 3 3
;; X 0 1 2 2 2 2 3 3 3 3 3 3
;; I 0 1 2 2 2 2 3 3 3 4 4 4
;; S 0 1 2 2 2 2 3 3 3 4 4 4
;; 
;; If we represent the rows as Xi and the columns as Yj, the matrix can be filled top-to-bottom, left-to-right using the
;; formula:
;; 
;;            { 0,                           if i=0 or j=0
;;            {
;; LCS(i,j) = { LCS(i-1,j-1) + 1,            if X(i) = Y(j)
;;            {
;;            { max(LCS(i-1,j), LCS(i,j-1)), otherwise
;; 
;; Intuitively, the two sequences are scanned in parallel. If the current two cells are identical, the length of the
;; longest common subsequence increases by one; otherwise, there are two possibilities to consider recursively, after
;; deleting the current cell from either one input sequence or the other, and the length of the longest common
;; subsequence is simply the greater of the two possibilities.
;; 
;; Once the matrix of longest common subsequence lengths has been calculated, the longest common subsequence itself can
;; be recovered by noting each point where the length “bumps” to the next lower value along the diagonal, starting at
;; the lower right-hand corner: from 4 to 3 when both sequences are at I, from 3 to 2 when both sequences are at A, from
;; 2 to 1 when both sequences are at R, and from 1 to 0 when both sequences are at P.
;; 
;; Note that the longest common sequence is not necessarily unique. For instance, given the two sequences ABC and BAC,
;; there are two longest common subsequences, AC and BC. Either answer is correct.
;; 
;; Your task is to write a function that takes two sequences and returns their longest common subsequence. When you are
;; finished, you are welcome to read or run a suggested solution, or to post your solution or discuss the exercise in
;; the comments below.
