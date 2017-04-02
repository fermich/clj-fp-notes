(ns pl.fermich.fp-notes.cats-examples
  (:require [cats.core :as m]
            [cats.builtin]
            [cats.monad.maybe :as maybe]))

; http://funcool.github.io/cats/latest/

(defn -main []
  (println
    "Semigroup: "
    ; semigroup with mappend operation:
    (m/mappend (maybe/just [1 2 3])
               (maybe/just [4 5 6]))

    "\nMonoid:"
    ; A Monoid is a Semigroup with an identity element (mempty).
    (m/mappend (maybe/just [1 2 3])
               (maybe/nothing)
               (maybe/just [4 5 6])
               (maybe/nothing))

    "\nFunctor:"
    ; The Functor represents some sort of "computational context", and the abstraction consists of one unique function: fmap.
    (m/fmap inc (maybe/just 1))

    "\nApplicative:"
    ; The Applicative Functor represents some sort of "computational context" like a plain Functor, but with the ability to execute a function wrapped in the same context.
    ; The Applicative Functor abstraction consists of two functions: fapply and pure.
    (m/fapply (maybe/just (fn [name] (str "Hello " name))) (maybe/just "Alex"))

    "\nMonad:"
    ; bind works much like a Functor but with inverted arguments. The main difference is that in a monad, the function is responsible for wrapping a returned value in a context.
    (m/bind (maybe/just 1) (fn [v] (m/return (inc v)))))
  )
