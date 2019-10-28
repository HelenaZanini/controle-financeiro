(ns controle-financeiro.db-test
  (:require [midje.sweet :refer :all]
            [controle-financeiro.db :as db]))

(facts "Registra 1 transação"
  (against-background
    [(before :facts (db/clear-data-base!))
     (after :facts (db/clear-data-base!))]
    (fact "Transações iniciais é vazio" :unitarios
      (count (db/get-all-transactions!)) => 0)
    (fact "1ª transação"
      (db/registrer! {:valor 10 :tipo "receita"})
      => {:id 1 :valor 10 :tipo "receita"}
      (count (db/get-all-transactions!)) => 1)))


(facts "Registra 1 transação de receita e despesa"
  (against-background
    [(before :facts (db/clear-data-base!))
     (after :facts (db/clear-data-base!))]
    (fact "Transações iniciais é vazio" :unitarios
      (count (db/get-all-transactions!)) => 0)
    (fact "1ª transação receita" :unitarios
      (db/registrer! {:valor 10 :tipo "receita"})
      => {:id 1 :valor 10 :tipo "receita"}
      (count (db/get-receitas!)) => 1)
    (fact "1ª transação despesa" :unitarios
      (db/registrer! {:valor 100 :tipo "despesa"})
      => {:id 1 :valor 100 :tipo "despesa"}
      (count (db/get-despesas!)) => 1)))