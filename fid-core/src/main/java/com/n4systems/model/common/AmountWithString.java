package com.n4systems.model.common;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Quantity;
import java.io.Serializable;

public class AmountWithString<Q extends Quantity> implements Serializable {
    // might need a custom user type to store just the BigDecimal underlying this?  depends on whether you want the unit stored with it.  dont think so.
    private Amount<Q> amount;
    private String text;

    public AmountWithString() {
        setText("");
        setAmount((Amount<Q>) Amount.valueOf("0ft"));
    }

    public AmountWithString(String text, Amount<Q> amount) {
        setText(text);
        setAmount(amount);
    }

    public AmountWithString(String text) {
        setText(text);
        setAmount((Amount<Q>) Amount.valueOf(text));
    }

    public Amount<Q> getAmount() {
        return amount;
    }

    public void setAmount(Amount<Q> amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AmountWithString)) return false;

        AmountWithString that = (AmountWithString) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
