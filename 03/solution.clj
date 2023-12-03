(ns gear-ratios
  (:require [clojure.string :as str]))

; https://stackoverflow.com/questions/3262195/compact-clojure-code-for-regular-expression-matches-and-their-position-in-string
(defn re-pos [re s]
        (loop [m (re-matcher re s)
               res {}]
          (if (.find m)
            (recur m (assoc res (.start m) (.group m)))
            res)))

(defn has-symbols?
  [string]
  (boolean (re-find #"(\*|\%|\/|\&|\@|\$|\-|\+|\=|\#)" string)))

(defn array-to-mask
  [array reference]
  (map #(has-symbols? (subs array (get % 0) (get % 1))) reference))

(def test-array
  [".............................................................................................................................................." "....................15....904...........850.................329...................13....................................871....816....697....." "............53.497........................%....906...610.......*.............735#..&...*......558...68...............68..*......&....*........"]
)

(defn numbers-from-string-array
  [arrays]
  (let [pos-string (map #(vector (dec (get % 0)) (+ (count (get % 1)) (get % 0) 1) (get % 1)) (re-pos #"\d+" (get arrays 1)))
        masks (map #(array-to-mask % pos-string) arrays)
        mask (apply map #(or %1 %2 %3) masks)
        pos-string-mask (map #(vector %1 %2) mask pos-string)]
    (keep #(if (first %) (Integer/parseInt (last (last %)))) pos-string-mask)))

(numbers-from-string-array ["............" ".467..114..." "....*......."])

(def long-string
  (apply str (take 140 (repeat "."))))

; part 1
(->> (slurp "input.in")
     (#(str long-string "\r\n" % "\r\n" long-string "\r\n"))
     (str/split-lines)
     (map #(str "." % "."))
     (partition 3 1)
     (map vec)
     (map numbers-from-string-array)
     (flatten)
     (reduce +))

; part 2 — won't do

; look for a gear
; get everythyng around the gear:
; .......
; ...*...
; .......
; look for a number
; if the number's position is 0 and len != 3 -> don't count
; if the number's position is > than 1 from the gear -> don't count
