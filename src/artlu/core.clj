(ns artlu.core
  (:use [clojure.pprint])
  (:require [instaparse.core :as insta]
            [commentclean.core :as comment]))

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
     x#))


(def parser (insta/parser (clojure.java.io/resource "artlu.bnf")))



(defn to-map [type]
  (fn [name & args] (with-meta {name args} {:type type})))

(defn merge-maps [& args]
  (reduce 
    (fn [acc m] 
      (let [type (-> m meta :type)]
        (update acc type  #(merge % m))        
        ))
    {} args)
       
  )

(def ast->clj-map
  {:charValue identity
   :strLit (partial apply str)
   :octalLit (fn [& args] (str "0" (apply str args)))
   :octalDigit identity
   :hexLit (fn [& args] (str "0x" (apply str args)))
   :hexDigit identity
   :decimalDigit identity
   :decimalLit (fn [& args] (apply str args))
   :intLit read-string
   :aplExpression identity
   :aplBooleanExpression identity
   :addition (fn [& args] `(+ ~@args))
   :subtraction (fn [& args] `(- ~@args))
   :multiplication (fn [& args] `(* ~@args))
   :division (fn [& args] `(/ ~@args))
   :bit-and (fn [& args] `(bit-and ~@args))
   :bit-or (fn [& args] `(bit-or ~@args))
   :right-shift (fn [& args] `(bit-shift-right ~@args))
   :left-shift (fn [& args] `(bit-shift-left ~@args))
   :equals (fn [& args] `(= ~@args))
   :not-equals (fn [& args] `(not= ~@args))
   :gt (fn [& args] `(> ~@args))
   :gte (fn [& args] `(>= ~@args))
   :lt (fn [& args] `(< ~@args))
   :lte (fn [& args] `(<= ~@args))
   :if-expr (fn [condition if-value else-value] `(if ~condition ~if-value ~else-value))
   :ident (fn [& args] (apply str args))
   :dynamicSizeExpr identity
   :external (to-map :external)
   :in_map (to-map :in_map)
   :out_map (to-map :out_map)
   :decoder (to-map :decoder)
   :encoder (to-map :encoder)
   :artlu merge-maps 
   })

(defn ast->clj [ast]
  (insta/transform ast->clj-map ast))


(defn parse [text] (-> text comment/clean (parser :start :artlu) ast->clj))

(defn get-failure [ast] (-> ast insta/get-failure print-str))

(defn decode [ast decoder-name]
  (dbg ast)
  (fn [data]
    [{"recordLength" (int 25), 
      "arrayLength" (byte 20), 
      "arrayField" (byte-array (range 1 21))}]))



