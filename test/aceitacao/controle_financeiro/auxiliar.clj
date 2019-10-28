(ns controle-financeiro.auxiliar
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [controle-financeiro.handler :refer [app]]
            [clj-http.client :as http]))

(def port 3002)
(def url "http://localhost:")
(def server (atom nil))

(defn get-route [route]
  (str url port route))

(def req-get (comp http/get get-route))

(defn get-content [route]
  (:body (req-get route)))

(defn start-server [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server []
  (.stop @server))
