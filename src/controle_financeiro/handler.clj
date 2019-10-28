(ns controle-financeiro.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [controle-financeiro.controller :refer :all]))

(defroutes app-routes
           (GET "/" [] (handle-get))
           (GET "/saldo" [] (handle-get-saldo!))
           (GET "/transactions/all" {params :params} (handle-get-all-transactions! params))
           (GET "/transactions/receitas" {params :params} (handle-get-receita-transactions! params))
           (GET "/transactions/despesas" {params :params} (handle-get-despesa-transactions! params))
           (POST "/transaction" req (generate-new-transaction! req))
           (route/not-found (sorry-not-found)))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
