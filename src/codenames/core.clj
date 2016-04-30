(ns codenames.core
  (:require [codenames.wordservice :as ws])
  (:require [clojure.set :as set])
  (:gen-class))

(defn -main
  [& args]
  (println (into () (apply set/intersection (map ws/get-relations args)))))



