(ns codenames.wordservice
  (:require [clj-http.client :as client])
  (:require [clojure.data.json :as json])
  (:require [codenames.config :as config])
  (:gen-class))

(declare one-word?)
(declare get-all-rels)
(declare fetch-rels)

(def headers {"X-Mashape-Key" config/token, "Accept" "application/json"})
(def link "https://wordsapiv1.p.mashape.com/words/")


(defn get-relations
  [word]
  (def word-file (str "_data/" word ".txt"))
  
  (def exists (.exists (clojure.java.io/as-file word-file)))
  
  (if (not exists)
    (spit word-file (fetch-rels word)))

  (read-string (slurp word-file))) 

    
(defn one-word?
  [word]
  (not (.contains word " ")))

(defn get-all-rels
  [definition]
  (concat
    (definition "synonyms")
    (definition "typeOf")
    (definition "verbGroup")
    (definition "partOf")
    (definition "also")
    (definition "hasCategories")
    (definition "hasInstances")
    (definition "hasMembers")
    (definition "hasParts")
    (definition "hasSubstances")
    (definition "hasTypes")
    (definition "hasUsages")
    (definition "inCategory")
    (definition "inRegion")
    (definition "instanceOf")
    (definition "memberOf")
    (definition "pertainsTo")
    (definition "regionOf")
    (definition "similarTo")
    (definition "substanceOf")
    (definition "usageOf")
    (definition "entails")))

(defn fetch-rels 
  [word]
  (println "pulling data from server")
  (def results ((json/read-str ((client/get (str link word) {:as :json, :headers headers}) :body)) "results"))
  (set (filter one-word? (reduce concat (map get-all-rels results)))))
