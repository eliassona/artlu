(ns artlu.core-test
  (:require [clojure.test :refer :all]
            [artlu.core :refer :all])
  (:use [clojure.pprint])
  (:import [artlu BitCodec]))

(deftest test-field
  (verify-type 2 "byte")
  (verify-type 256 "int")
  (verify-type (+ Integer/MAX_VALUE 1) "long")
  ;(verify-type (byte-array [1 2])  "bytearray")
  )


(defn verify-codec [value field] 
  (let [bc (BitCodec.)
        e (:encode field)
        d (:decode field)]
    (is (= (d (.toByteArray (e value bc nil)) 0 nil) value))))

(deftest test-external
  (let [u (eval (parse 
				    "external E1 {
				      byte f1;
				      int f2;
				     };
				     external E2 {
				       byte f3;
				     };"))
        e1 ((:external u) "E1")
        ]
    (verify-codec 35 (e1 "f1"))
    (verify-codec 35 (e1 "f2"))
  ))



(defn verify-type [value type]
  (verify-codec value ((eval (parse (format "%s f1;" type) :field)) "f1"))
  #_(let [f1 ((eval (parse (format "%s f1;" type) :field)) "f1")
         bc (BitCodec.)
         e (:encode f1)
         d (:decode f1)]
     (is (= (d (.toByteArray (e value bc nil)) 0 nil) value))))



