package com.javacro.dslplatform.model.Accounting;

public final class Transaction implements java.io.Serializable {
    public Transaction(
            final double inflow,
            final double outflow,
            final String description,
            final org.joda.time.DateTime paymentOn) {
        setInflow(inflow);
        setOutflow(outflow);
        setDescription(description);
        setPaymentOn(paymentOn);
    }

    public Transaction() {
        this.inflow = 0.0;
        this.outflow = 0.0;
        this.description = "";
        this.paymentOn = org.joda.time.DateTime.now();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + 174931478;
        result = prime * result + (Double.valueOf(this.inflow).hashCode());
        result = prime * result + (Double.valueOf(this.outflow).hashCode());
        result = prime * result
                + (this.description != null ? this.description.hashCode() : 0);
        result = prime * result
                + (this.paymentOn != null ? this.paymentOn.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;

        if (!(obj instanceof Transaction)) return false;
        final Transaction other = (Transaction) obj;

        if (!(Double.doubleToLongBits(this.inflow) == Double
                .doubleToLongBits(other.inflow))) return false;
        if (!(Double.doubleToLongBits(this.outflow) == Double
                .doubleToLongBits(other.outflow))) return false;
        if (!(this.description.equals(other.description))) return false;
        if (!(this.paymentOn.equals(other.paymentOn))) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Transaction(" + inflow + ',' + outflow + ',' + description
                + ',' + paymentOn + ')';
    }

    private static final long serialVersionUID = 0x0097000a;

    private double inflow;

    @com.fasterxml.jackson.annotation.JsonProperty("inflow")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public double getInflow() {
        return inflow;
    }

    public Transaction setInflow(final double value) {
        this.inflow = value;

        return this;
    }

    private double outflow;

    @com.fasterxml.jackson.annotation.JsonProperty("outflow")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public double getOutflow() {
        return outflow;
    }

    public Transaction setOutflow(final double value) {
        this.outflow = value;

        return this;
    }

    private String description;

    @com.fasterxml.jackson.annotation.JsonProperty("description")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public String getDescription() {
        return description;
    }

    public Transaction setDescription(final String value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"description\" cannot be null!");
        this.description = value;

        return this;
    }

    private org.joda.time.DateTime paymentOn;

    @com.fasterxml.jackson.annotation.JsonProperty("paymentOn")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
    public org.joda.time.DateTime getPaymentOn() {
        return paymentOn;
    }

    public Transaction setPaymentOn(final org.joda.time.DateTime value) {
        if (value == null)
            throw new IllegalArgumentException(
                    "Property \"paymentOn\" cannot be null!");
        this.paymentOn = value;

        return this;
    }
}
