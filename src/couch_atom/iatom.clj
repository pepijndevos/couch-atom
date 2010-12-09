(ns couch-atom.iatom
  (:refer-clojure :exclude [swap! compare-and-set! reset!]))

(defprotocol IAtom
  (compare-and-set! [this o n]))

(extend-type clojure.lang.Atom
  IAtom
  (compare-and-set! [this o n] (clojure.core/compare-and-set! this o n)))

(defn swap! [a f & args]
  (loop [old @a
         nw (apply f old args)]
    (if-not (compare-and-set! a old nw)
      (let [old @a
            nw (apply f old args)]
        (recur old nw))
      nw)))

(defn reset! [a v]
  (swap! a (constantly v)))
