(ns couch-atom.core
  (:refer-clojure :exclude [swap! compare-and-set! reset!])
  (:use couch-atom.iatom
        com.ashafa.clutch))

(deftype couch-atom* [id]
  clojure.lang.IDeref
  (deref [this] (get-document id))
  IAtom
  (compare-and-set! [this o n]
    (try
      (update-document o n)
      true
      (catch java.io.IOException _ false))))

(defn couch-atom [id]
  (couch-atom*. id))
