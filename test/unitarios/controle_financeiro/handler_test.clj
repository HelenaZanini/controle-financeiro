(ns controle-financeiro.handler-test
  (:require [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]
            [controle-financeiro.handler :refer :all]
            [controle-financeiro.transactions :refer :all]
            [controle-financeiro.db :as db]))


(facts "Get rota raiz"
  (let [response (app (mock/request :get "/"))]
    (fact "status = 200" :unitarios
      (:status response) => 200)
    (fact "resultado = 0" :unitarios
      (:body response) => "Olar")))

(facts "Get rota /saldo"
  (against-background [(json/generate-string {:saldo 0}) => "{\"saldo\":0}"
                      (get-saldo-now!) => 0])
  (let [response (app (mock/request :get "/saldo"))]
    (fact "status = 200" :unitarios
      (:status response) => 200)
    (fact "o formato é 'application/json'" :unitarios
      (get-in response [:headers "Content-Type"])
      => "application/json; charset=utf-8")
    (fact "resultado = '{:saldo 0}'" :unitarios
      (:body response) => "{\"saldo\":0}")))

(facts "Get rota /transactions/all"
  (let [response (app (mock/request :get "/transactions/all"))]
    (fact "status = 200" :unitarios
      (:status response) => 200)
    (fact "o formato é 'application/json'" :unitarios
      (get-in response [:headers "Content-Type"])
      => "application/json; charset=utf-8")
    (fact "resultado = []" :unitarios
      (:body response) => "[]")))

(facts "Get rota /transactions/receitas"
  (let [response (app (mock/request :get "/transactions/receitas"))]
    (fact "status = 200" :unitarios
      (:status response) => 200)
    (fact "o formato é 'application/json'" :unitarios
      (get-in response [:headers "Content-Type"])
      => "application/json; charset=utf-8")
    (fact "resultado = []" :unitarios
      (:body response) => "[]")))

(facts "Get rota /transactions/despesas"
  (let [response (app (mock/request :get "/transactions/despesas"))]
    (fact "status = 200" :unitarios
      (:status response) => 200)
    (fact "o formato é 'application/json'" :unitarios
      (get-in response [:headers "Content-Type"])
      => "application/json; charset=utf-8")
    (fact "resultado = []" :unitarios
      (:body response) => "[]")))

(facts "Faz uma transação de 200$"
  (against-background (db/registrer! {:valor 200 :tipo "receita" :rotulos "sapatos"})
                      => {:id 1 :valor 200 :tipo "receita" :rotulos "sapatos"})
  (let [response (app (-> (mock/request :post "/transaction")
                          (mock/json-body {:valor 200 :tipo "receita" :rotulos "sapatos"})))]
    (fact "o status da resposta é 201" :unitarios
      (:status response) => 201)
    (fact "o texto do corpo é um JSON com o conteúdo enviado e um id" :unitarios
      (:body response) => "{\"id\":1,\"valor\":200,\"tipo\":\"receita\",\"rotulos\":\"sapatos\"}")))

(facts "Get rota invalida"
  (fact "status = 404" :unitarios
    (let [response (app (mock/request :get "/sei-la"))]
      (:status response) => 404)))
