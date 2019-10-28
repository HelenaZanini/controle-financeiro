(ns controle-financeiro.controller
  (:require [cheshire.core :as json]
            [controle-financeiro.db :as db]
            [controle-financeiro.transactions :refer :all]))

(defn ^:private parse-params [params]
  (json/generate-string params))

(defn ^:private make-response [content & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string content)})

(defn ^:private isValid? [params]
  (and (contains? params :valor)
       (number? (:valor params))
       (pos? (:valor params))
       (contains? params :rotulos)
       (contains? params :tipo)
       (or (= "despesa" (:tipo params))
           (= "receita" (:tipo params)))))

(defn handle-get [] "Olar")

(defn handle-get-saldo! []
  (make-response {:saldo (get-saldo-now!)}))

(defn generate-new-transaction! [req]
  (if (isValid? (:body req))
    (-> (db/registrer! (:body req))
        (make-response 201))
    (make-response {:mensagem "Sorry, vc passou os parametros errados"} 422)))

(defn ^:private handle-get-transactions! [params type]
  (let [filters (:rotulos params)]
  (-> (get-transactions! filters type)
      (make-response))))

(defn handle-get-all-transactions! [params]
  (handle-get-transactions! params "all"))

(defn handle-get-receita-transactions! [params]
  (handle-get-transactions! params "receita"))

(defn handle-get-despesa-transactions! [params]
  (handle-get-transactions! params "despesa"))

(defn sorry-not-found [] "Foi mal, não tem nada nesse endereço")