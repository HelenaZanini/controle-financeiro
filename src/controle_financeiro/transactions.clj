(ns controle-financeiro.transactions
  (:require [controle-financeiro.db :as db]))

(defn ^:private despesa? [transaction]
  (= (:tipo transaction) "despesa"))

(defn ^:private saldo-since-always [acc transaction]
  (let [value (:valor transaction)]
    (if (despesa? transaction)
      (- acc value)
      (+ acc value))))

(defn ^:private format-filters [filters]
  (-> (conj [] filters)
      (flatten)
      (set)))

(defn ^:private filter-transactions [filters trasacions]
  (filter #(contains? filters (:rotulos %)) trasacions))

(defn ^:private get-transactions-with-filters! [filters get-fn!]
  (if (nil? filters)
    (get-fn!)
    (filter-transactions (format-filters filters) (get-fn!))))

(defn get-saldo-now! [] (reduce saldo-since-always 0 (db/get-all-transactions!)))

(defn get-transactions! [filters type]
  (cond (= type "all") (get-transactions-with-filters! filters db/get-all-transactions!)
        (= type "receita") (get-transactions-with-filters! filters db/get-receitas!)
        (= type "despesa") (get-transactions-with-filters! filters db/get-despesas!)
        :else '()))
