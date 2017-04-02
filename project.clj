(defproject odds-router "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [uncomplicate/fluokitten "0.3.0"]
                 [uncomplicate/clojurecl "0.7.0"]
                 [funcool/cats "2.0.0"]]

  :profiles {:functor {:main pl.fermich.fp-notes.functor}
             :applfunctor {:main pl.fermich.fp-notes.appl-functor}
             :monad {:main pl.fermich.fp-notes.monad}
             :justex {:main pl.fermich.fp-notes.just-examples}
             :catsex {:main pl.fermich.fp-notes.cats-examples}}
  )
