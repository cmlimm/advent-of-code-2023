(ns seeds
  (:require [clojure.string :as str]))

(defn get-next-number
  [number start-1 start-2]
  (+ start-2 (abs (- number start-1))))

(defn in-range?
  [number start-1 n-range]
  (and (>= number start-1) (< number (+ start-1 n-range))))

(defn find-map
  [number maps]
  (reduce 
   #(when 
      (in-range? number ((comp first rest) %2) (last %2))
      (reduced %2))
   nil
   maps))

(defn get-map
  [number maps]
  (let [possible-map (find-map number maps)]
    (if (nil? possible-map)
      `(0 0 0)
      possible-map)))

(defn proceed-number
  [number maps]
  (let [[start-2 start-1 _] (get-map number maps)]
    (get-next-number number start-1 start-2)))

; (def seeds `(79 14 55 13))

; (def seed-to-soil
;   `((50 98 2) (52 50 48)))

; (def soil-to-fertilizer
;   `((0 15 37) (37 52 2) (39 0 15)))

; (def fertilizer-to-water
;   `((49 53 8) (0 11 42) (42 0 7) (57 7 4)))

; (def water-to-light
;   `((88 18 7) (18 25 70)))

; (def light-to-temperature
;   `((45 77 23) (81 45 19) (68 64 13)))

; (def temperature-to-humidity
;   `((0 69 1) (1 0 69)))

; (def humidity-to-location
;   `((60 56 37) (56 93 4)))

(def seeds `(1263068588 44436703 1116624626 2393304 2098781025 128251971 2946842531 102775703 2361566863 262106125 221434439 24088025 1368516778 69719147 3326254382 101094138 1576631370 357411492 3713929839 154258863))

(defn read-maps
  [filename]
  (->> 
    (slurp filename)
    (str/split-lines)
    (map #(map (fn [x] (parse-long x)) (str/split % #" ")))))

(def seed-to-soil
  (read-maps "seed-to-soil.in"))

(def soil-to-fertilizer
  (read-maps "soil-to-fertilizer.in"))

(def fertilizer-to-water
  (read-maps "fertilizer-to-water.in"))

(def water-to-light
  (read-maps "water-to-light.in"))

(def light-to-temperature
  (read-maps "light-to-temperature.in"))

(def temperature-to-humidity
  (read-maps "temperature-to-humidity.in"))

(def humidity-to-location
  (read-maps "humidity-to-location.in"))

(def list-maps
  (vector seed-to-soil soil-to-fertilizer fertilizer-to-water water-to-light light-to-temperature temperature-to-humidity humidity-to-location))

; part 1
(apply min (map #(reduce proceed-number % list-maps) seeds))

; part 2

; because range is lazy we can use reduce to consume less memory
; on the first iteration reduce takes location corresponding to the first seed
; and the second seed
; then reduce calculates the location of the second seed and takes the minimal between two
; and so on
; in the end we get the minimum for this range
; 
; we use parallel map to do that to every range
; and then choose the minimal among them

(apply min (pmap 
             (fn [[start len]]
               (reduce 
                 (fn [f s] (min f (reduce proceed-number s list-maps)))
                 (reduce proceed-number start list-maps)
                 (range (inc start) (+ start len))))
             (partition 2 seeds)))

; (defn parallel-generate-seeds
;   [seeds]
;   (apply concat (pmap (fn [[start len]] (range start (+ start len))) (partition 2 seeds))))

; (parallel-generate-seeds seeds)

; (apply min (map #(reduce proceed-number % list-maps) (parallel-generate-seeds seeds)))
