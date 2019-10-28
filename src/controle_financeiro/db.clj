(ns controle-financeiro.db)

(def data-base (atom []))

(defn ^:private by-type [type transaction ]
  (= (:tipo transaction) type))

(def by-receita (partial by-type "receita"))
(def by-despesa (partial by-type "despesa"))

(defn ^:private get-all! [] @data-base)

(defn get-all-transactions! [] (get-all!))

(defn get-receitas! []
  (filter by-receita (get-all!)))

(defn get-despesas! []
  (filter by-despesa (get-all!)))

(defn clear-data-base! [] (reset! data-base []))

(defn registrer! [transaction]
  (let [up-to-date-transactions (swap! data-base conj transaction)]
  (merge transaction {:id (count up-to-date-transactions)})))
