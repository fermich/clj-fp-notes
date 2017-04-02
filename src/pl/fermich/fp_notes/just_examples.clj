(ns pl.fermich.fp-notes.just-examples
  (:use [uncomplicate.fluokitten core jvm]))


; ---- applicative ----
(def add1 (partial + 1))

(def add5 (partial + 5))

(defn applicative-add [p]
  (fapply (just add5) (just p)))

(defn applicative-multi-add [p]
  (<*> (just add5) (just p)))


; ---- monad ----
(defn half [x]
  (if (even? x)
    (just (quot x 2))
    nil))

(defn monad-call [p]
  (bind (just p) half))

(defn monad-multi-call [p]
  (>>= (just p) half half))


; ---- main ----
(defn -main []

  (fapply (just println) (applicative-add 2))
  (fapply (just println) (applicative-multi-add 2))

  (fapply (just println) (monad-call 4))
  (fapply (just println) (monad-multi-call 20))
  )
