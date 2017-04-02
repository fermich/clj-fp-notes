(ns pl.fermich.fp-notes.functor
  (:require [uncomplicate.fluokitten.protocols :as fkp]
            [uncomplicate.fluokitten.core :as fkc]
            [uncomplicate.fluokitten.jvm :as fkj]))

(def persons [{:name "Jack" :born 1700 :died 1740 :ship "Black"}
              {:name "Beard" :born 1680 :died 1750 :ship "Queen"}
              {:name "Hector" :born 1680 :died 1740 :ship nil}])

(defn person-by-name [name] (->> persons (filter #(= name (:name %))) first))

(defn age [{:keys [born died]}] (- died born))


(defrecord Some [v])

(defrecord None [])

(defn option [v]
  (if v (Some. v) (None.)))

(extend-protocol fkp/Functor
  Some
  ;fmap simply applies the function g to the value inside the Functor f , which is of type Some
  (fmap [f g]
    (Some. (g (:v f))))
  None
  (fmap [_ _]
    (None.)))


(defn -main []
  (->> (option (person-by-name "Davy"))
       (fkc/fmap age)
       (println)))
