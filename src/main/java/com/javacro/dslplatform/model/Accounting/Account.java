package com.javacro.dslplatform.model.Accounting;

public final class Account implements java.io.Serializable {
    public Account(
            final String IBAN,
            final String currency,
            final java.util.List<com.javacro.dslplatform.model.Accounting.Transaction> transactions) {
        setIBAN(IBAN);
        setCurrency(currency);
        setTransactions(transactions);
    }

    public Account() {
        this.IBAN = "";
        this.currency = "";
        this.transactions = new java.util.ArrayList<com.javacro.dslplatform.model.Accounting.Transaction>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + 767759945;
        result = prime * result
                + (this.IBAN != null ? this.IBAN.hashCode() : 0);
        result = prime * result
                + (this.currency != null ? this.currency.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (!(obj instanceof Account)) return false;
        final Account other = (Account) obj;

        if (!(this.IBAN.equals(other.IBAN))) return false;
        if (!(this.currency.equals(other.currency))) return false;
        if (!((this.transactions == other.transactions || this.transactions != null
                && this.transactions.equals(other.transactions))))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Account(" + IBAN + ',' + currency + ',' + transactions + ')';
    }

    private static final long serialVersionUID = 0x0097000a;

    private String IBAN;

    @com.fasterxml.jackson.annotation.JsonProperty("IBAN")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public String getIBAN() {
        return IBAN;
    }

    public Account setIBAN(final String value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"IBAN\" cannot be null!");
        this.IBAN = value;

        return this;
    }

    private String currency;

    @com.fasterxml.jackson.annotation.JsonProperty("currency")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public String getCurrency() {
        return currency;
    }

    public Account setCurrency(final String value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"currency\" cannot be null!");
        this.currency = value;

        return this;
    }

    private java.util.List<com.javacro.dslplatform.model.Accounting.Transaction> transactions;

    @com.fasterxml.jackson.annotation.JsonProperty("transactions")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public java.util.List<com.javacro.dslplatform.model.Accounting.Transaction> getTransactions() {
        return transactions;
    }

    public Account setTransactions(
            final java.util.List<com.javacro.dslplatform.model.Accounting.Transaction> value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"transactions\" cannot be null!");
        com.javacro.dslplatform.model.Guards.checkNulls(value);
        this.transactions = value;

        return this;
    }
}
