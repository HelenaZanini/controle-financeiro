(ns controle-financeiro.transactions-test
  (:require [midje.sweet :refer :all]
            [controle-financeiro.db :as db]
            [controle-financeiro.transactions :refer :all]))

(def mock-transactions
  '({:valor 100 :tipo "receita"}
    {:valor 100 :tipo "receita"}
    {:valor 100 :tipo "receita"}
    {:valor 20 :tipo "despesa"}
    {:valor 100 :tipo "despesa"}
    {:valor 10 :tipo "despesa"}))


(facts "Verifica saldo sem transações"
  (against-background
    [(before :facts (db/clear-data-base!))]
    (fact "1ª transação com saldo = 0"
      (get-saldo-now!) => 0)))

(facts "Retorna o saldo atual"
  (against-background
    [(before :facts (db/clear-data-base!))
     (after :facts (db/clear-data-base!))]
    (fact "Saldo Positivo" :unitarios
      (db/registrer! {:valor 10 :tipo "receita"})
      (db/registrer! {:valor 30 :tipo "receita"})
      (db/registrer! {:valor 70 :tipo "receita"})
      (get-saldo-now!) => 110)
    (fact "Saldo Negativo" :unitarios
      (db/registrer! {:valor 10 :tipo "despesa"})
      (db/registrer! {:valor 30 :tipo "despesa"})
      (db/registrer! {:valor 30 :tipo "despesa"})
      (get-saldo-now!) => -70)
    (fact "Saldo Positivo" :unitarios
      (db/registrer! {:valor 110 :tipo "receita"})
      (db/registrer! {:valor 30 :tipo "despesa"})
      (db/registrer! {:valor 70 :tipo "despesa"})
      (get-saldo-now!) => 10)))

(facts "Retorna a quantidade"
  (against-background
  [(before :facts [(db/clear-data-base!)
                     (doseq [transaction mock-transactions]
                       (db/registrer! transaction))])
     (after :facts [(db/clear-data-base!)])]
    (fact "Total de transações = 6" :unitarios
      (count (get-transactions! nil "all")) => 6)
    (fact "Total de transações tipo receita = 3" :unitarios
      (count (get-transactions! nil "receita")) => 3)
    (fact "Total de transações tipo despesa = 3" :unitarios
      (count (get-transactions! nil "despesa")) => 3)))