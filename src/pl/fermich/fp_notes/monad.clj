(ns pl.fermich.fp-notes.monad
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

; --------------------------------

(extend-protocol fkp/Functor
  Some
  ;fmap simply applies the function g to the value inside the Functor f , which is of type Some
  (fmap [f g]
    (Some. (g (:v f))))
  None
  (fmap [_ _]
    (None.)))

; --------------------------------

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

; --------------------------------

(extend-protocol fkp/Monad
  Some
  ; The bind function of monads takes a function g as its second argument.
  ; This function receives as input the value contained in mv and returns another Monad containing its result.
  ; This is a crucial part of the contract: g has to return a Monad.
  (bind [mv g] (g (:v mv)))

  None
  (bind [_ _] (None.)))

; --------------------------------

(def opt-ctx (None.))

(defn age [{:keys [born died]}] (- died born))

(defn person-by-name [name] (->> persons (filter #(= name (:name %))) first))

(def age-option (comp (partial fkc/fmap age) option person-by-name))

(defn sum-pure []
  (fkc/bind (age-option "Jack")
            (fn [a]
              (fkc/bind (age-option "Beard")
                        (fn [b]
                          (fkc/bind (age-option "Hector")
                                    (fn [c]
                                      (fkc/pure opt-ctx (+ a b c)))))))))

(defn sum-mdo []
  (fkc/mdo [a (age-option "Jack")
            b (age-option "Beard")
            c (age-option "Hector")]
           (fkc/pure opt-ctx (+ a b c))))

(defn -main []
  (println (sum-mdo))
  (println (sum-pure)))
