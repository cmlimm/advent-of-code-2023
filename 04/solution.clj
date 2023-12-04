(ns scratchcards
  (:require [clojure.string :as str])
  (:require [clojure.set :as set])
  (:require [clojure.math :as math]))

(defn string-list-to-numbers
  [string-list]
  (set (map #(Integer/parseInt %) string-list)))

; manually deleted "Game N: " and double spaces
(def input
  (->> 
    (slurp "input.in")
    str/split-lines
    (map #(str/split % #" \| "))
    flatten
    (map #(string-list-to-numbers (str/split % #" ")))
    (partition 2)))

input

(def part-1
  (->>
    input
    (map #(count (apply set/intersection %)))
    (keep #(if (not= 0 %) (dec %)))
    (map #(math/pow 2 %))
    (reduce +)))

part-1

(defn get-new-cards
  [cards from]
  (let [tail-from (drop from cards)
        n (last (first tail-from))]
  (take n (rest tail-from))))

(defn collect-cards
  ([initial-cards] (collect-cards initial-cards initial-cards))
  ([initial-cards current-cards]
   (if (empty? current-cards)
     0
     (reduce + (map #(inc (collect-cards initial-cards (get-new-cards initial-cards (first %)))) current-cards)))
   ))

(def part-2
  (->>
    input
    (map #(count (apply set/intersection %)))
    (map-indexed #(vector %1 %2))
    collect-cards))

part-2