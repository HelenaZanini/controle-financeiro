(ns controle-financeiro.saldo-aceitacao-test
  (:require [midje.sweet :refer :all]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [controle-financeiro.auxiliar :refer :all]
            [controle-financeiro.db :as db]))

(def mock-transactions
  '({:valor 7.0M :tipo "despesa" :rotulos "comida"}
    {:valor 88.0M :tipo "despesa" :rotulos "comida"}
    {:valor 106.0M :tipo "despesa" :rotulos "sapatos"}
    {:valor 8000.0M :tipo "receita" :rotulos "trabalho"}))

(against-background
  [(before :facts [(start-server port)
                   (db/clear-data-base!)])
   (after :facts (stop-server))]
  (fact "Saldo inicial = 0" :aceitacao
    (json/parse-string (get-content "/saldo") true) => {:saldo 0})
  (fact "Novo saldo = 10" :aceitacao
    (http/post (get-route "/transaction")
               {:content-type :json
                :body (json/generate-string {:valor 10 :tipo "receita" :rotulos "venda"})})
    (json/parse-string (get-content "/saldo") true) => {:saldo 10}))

(against-background
  [(before :facts [(start-server port)
                   (doseq [transaction mock-transactions]
                    (db/registrer! transaction))])
   (after :facts [(db/clear-data-base!)
                  (stop-server)])]
  (fact "Existem 3 despesas" :aceitacao
    (count (json/parse-string
             (get-content "/transactions/despesas") true)) => 3)
  (fact "Existe 1 receita" :aceitacao
    (count (json/parse-string
             (get-content "/transactions/receitas") true)) => 1)
  (fact "Existem 4 transações" :aceitacao
    (count (json/parse-string
             (get-content "/transactions/all") true)) => 4))

(against-background
  [(before :facts [(start-server port)
                   (doseq [transaction mock-transactions]
                     (db/registrer! transaction))])
   (after :facts [(db/clear-data-base!)
                  (stop-server)])]
  (fact "Existem 3 despesas" :aceitacao
    (count (json/parse-string
             (get-content "/transactions/despesas?rotulos=comida&rotulos=viajem") true)) => 2))