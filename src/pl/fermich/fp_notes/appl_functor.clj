(ns pl.fermich.fp-notes.appl-functor
  (:require [uncomplicate.fluokitten.protocols :as fkp]
            [uncomplicate.fluokitten.core :as fkc]
            [uncomplicate.fluokitten.jvm :as fkj]))

(def persons [{:name "Jack" :born 1700 :died 1740 :ship "Black"}
              {:name "Beard" :born 1680 :died 1750 :ship "Queen"}
              {:name "Hector" :born 1680 :died 1740 :ship nil}])

(defrecord Some [v])

(defrecord None [])

(defn option [v]
  (if v (Some. v)
        (None.)))

(defn avg [& xs]
  (float (/ (apply + xs) (count xs))))

(extend-protocol fkp/Functor
  Some
  ;fmap simply applies the function g to the value inside the Functor f , which is of type Some
  (fmap [f g]
    (Some. (g (:v f))))
  None
  (fmap [_ _]
    (None.)))

(extend-protocol fkp/Applicative
  Some
  ;The pure function is a generic way to put a value inside an Applicative Functor.
  (pure [_ v] (Some. v))

  ;The fapply function will unwrap the function contained in the Applicative ag and apply it to the value contained in the applicative av .
  (fapply [ag av]
    (if-let [v (:v av)]
      (Some. ((:v ag) v))
      (None.)))

  None
  (pure [_ v]
    (Some. v))

  (fapply [ag av]
    (None.)))

(defn alift
  "Lifts a n-ary function `f` into a applicative context" [f]
  (fn [& as]
    {:pre [(seq as)]}
    (let [curried (fkj/curry f (count as))]
      (apply fkc/<*>
             (fkc/fmap curried (first as))
             (rest as)))))

(defn person-by-name [name] (->> persons (filter #(= name (:name %))) first))

(defn age [{:keys [born died]}] (- died born))

(def age-option (comp (partial fkc/fmap age) option person-by-name))

;------------------------

(defn avg<*> []
  (let [a (age-option "Jack")
        b (age-option "Beard")
        c (age-option "Hector")]
    (fkc/<*> (option (fkj/curry avg 3)) a b c)))

(defn avg-fapply []
  (let [a (age-option "Jack")
        b (age-option "Beard")
        c (age-option "Hector")]
    (fkc/fapply (fkc/fapply a b) c)))

(defn avg-alift []
  ((alift avg)
    (age-option "Jack")
    (age-option "Beard")
    (age-option "Hector")))

(defn -main []
  (println (avg<*>))
  (println (fkc/fapply (option avg) (age-option "Jack")))

  ;(println (avg-alift))
  )
