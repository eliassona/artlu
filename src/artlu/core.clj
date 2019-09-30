(ns artlu.core
  (:require [instaparse.core :as insta]))

(defmacro dbg [body]
  `(let [x# ~body]
     (println "dbg:" '~body "=" x#)
     x#))


(def parser (insta/parser (clojure.java.io/resource "artlu.bnf")))





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
   :gt (fn [& args] `(> ~@args))
   :gte (fn [& args] `(>= ~@args))
   :lt (fn [& args] `(< ~@args))
   :lte (fn [& args] `(<= ~@args))
   :if-expr (fn [condition if-value else-value] `(if ~condition ~if-value ~else-value))
   :ident (fn [& args] (apply str args))
   })

(defn ast->clj [ast]
  (insta/transform ast->clj-map ast))
