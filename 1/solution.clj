(ns trebuchet
  (:require [clojure.string :as string]))

(defn digit? 
  [c] 
  (and (>= 0 (compare \0 c)) 
       (>= 0 (compare c \9))))

(defn first-last
  [numbers]
  (if (zero? (count numbers))
    '(0 0)
    [(first numbers) (last numbers)]))

(def part1
  (->> (slurp "input.in")
       (clojure.string/split-lines)
       (map #(Integer/parseInt (apply str (first-last (filter digit? %)))))
       (reduce +)))

part1

(defn str-digit-to-int
  [digit]
  (case digit
    "one" 1
    "two" 2
    "three" 3
    "four" 4
    "five" 5
    "six" 6
    "seven" 7
    "eight" 8
    "nine" 9
    (Integer/parseInt digit)))

(defn get-digits
  [str]
  (map #(str-digit-to-int (last %)) (re-seq #"(?=(one|two|three|four|five|six|seven|eight|nine|\d))" str)))

(def part2
  (->> (slurp "input.in")
       (clojure.string/split-lines)
       (map #(Integer/parseInt (apply str (first-last (get-digits %)))))
       (reduce +)))
