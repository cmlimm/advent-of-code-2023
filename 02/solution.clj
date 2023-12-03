(ns cube-conundrum
  (:require [clojure.string :as str]))

(defn pair-to-int
  [pair]
  (let [[f s] pair]
   {(case s
    "red" :red
    "blue" :blue
    "green" :green) 
    (Integer/parseInt f)}))

(defn list-to-dict
  [list]
  (->> 
    (map #(pair-to-int (str/split % #" ")) list)
    (apply merge)))

(defn separate-balls
  [string]
  (->> (str/split string #"; ")
       (map #(list-to-dict (str/split % #", ")))))

; I've deleted "Game N: " from the lines manually
; part 1
(->> (slurp "input.in")
     (str/split-lines)
     (map #(apply merge-with max (separate-balls %)))
     (keep-indexed #(when 
                      (and
                        (<= (:red %2) 12)
                        (<= (:green %2) 13)
                        (<= (:blue %2) 14))
                      %))
     (map inc)
     (reduce +))

; part 2
(->> (slurp "input.in")
     (str/split-lines)
     (map #(reduce * (vals (apply merge-with max (separate-balls %)))))
     (reduce +))

