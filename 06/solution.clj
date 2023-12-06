(ns wait-for-it
  (:require [clojure.math :as math]))

(def time-distance-test
  `((... ...) (... ...) (... ...) (... ...)))

; find zeros for:
; (time - hold_time)*hold_time = distance
; find difference between them
(defn win-hold-times
  [[time distance]]
  (let [discr (- (* time time) (* 4 distance))
        first (/ (+ (- time) (math/sqrt discr)) (- 2))
        second (/ (- (- time) (math/sqrt discr)) (- 2))]
  (inc (- (math/ceil (dec second)) (math/floor (inc first))))))

; part 1
(reduce * (map win-hold-times time-distance-test))

; part 2
(win-hold-times `(... ...))
