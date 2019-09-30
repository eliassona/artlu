(ns artlu.core
  (:require [instaparse.core :as insta]))



(def parser (insta/parser (clojure.java.io/resource "artlu.bnf")))
