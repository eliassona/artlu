(ns artlu.core
  (:use [clojure.pprint])
  (:require [instaparse.core :as insta]
            [commentclean.core :as comment]
            [clojure.string :refer [capitalize]])
  (:import [artlu BitCodec]))

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
     x#))

(defn my-update
  ([m k f]
   (assoc m k (f (get m k))))
  ([m k f x]
   (assoc m k (f (get m k) x)))
  ([m k f x y]
   (assoc m k (f (get m k) x y)))
  ([m k f x y z]
   (assoc m k (f (get m k) x y z)))
  ([m k f x y z & more]
   (assoc m k (apply f (get m k) x y z more))))


(def parser (insta/parser (clojure.java.io/resource "artlu.bnf")))



(defn to-map [type]
  (fn [name & args] (with-meta {name args} {:type type})))
(defn to-map-map [type]
  (fn [name m] (with-meta {name m} {:type type})))

(defn merge-maps [& args]
  (reduce 
    (fn [acc m] 
      (let [type (-> m meta :type)]
        (my-update acc type  #(merge % m))        
        ))
    {} args)
       
  )

(defn add-encode-name-of [type]
  (symbol (format ".add%sEncode" (capitalize type))))

(defn decode-name-of [type]
  (symbol (format "BitCodec/%sDecode" type)))

(def type->bit-size 
  {"byte" 8
   "int" 32
   "long" 64
   })

(defn field-with-arg-of [type name bit-size signed]
  {name {:encode `(fn [value# bit-codec# context#]  
                    (~(add-encode-name-of type) bit-codec# value# ~bit-size 0 ~signed)), 
         :decode `(fn [ba# bit-offset# context#] 
                    (~(decode-name-of type) ba# bit-offset# ~bit-size ~signed))}})

(defn spec->bit-size [m]
  (cond
    (contains? m :bit_size) (:bit_size m)
    (contains? m :static_size) (* 8 (:static_size m))))

(defn field-of 
  ([type name args] 
    (field-with-arg-of type name (spec->bit-size args) (contains? args :signed))) 
  ([type name] 
    (field-with-arg-of type name (type->bit-size type) false)))

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
   :external (to-map-map :external)
   :in_map (to-map :in_map)
   :out_map (to-map :out_map)
   :decoder (to-map :decoder)
   :encoder (to-map :encoder)
   :import (fn [& args] (with-meta 
                          (let [p (String/join "." args)]
                            {p p}) {:type :import}))
   :type identity
   :field field-of
   :externalBlock (fn [& args] (apply merge args))
   :field-properties (fn [& args] (into {} (map (fn [[v1 v2]] (if (nil? v2) [v1 v1] [v1 v2])) args)))
   :artlu merge-maps 
   })

(defn ast->clj [ast]
  (insta/transform ast->clj-map ast))


(defn parse 
  ([text] (parse text :artlu))
  ([text start] (-> text comment/clean (parser :start start) ast->clj)))

(defn get-failure [ast] (-> ast insta/get-failure print-str))





