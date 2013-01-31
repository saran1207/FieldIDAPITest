package com.n4systems.fieldid.wicket.pages;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Quantity;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AmountWithString<Q extends Quantity> implements Serializable {
    // might need a custom user type to store just the BigDecimal underlying this?  depends on whether you want the unit stored with it.  dont think so.
    private Amount<Q> amount;
    private String text;

    public AmountWithString() {
    }

    public AmountWithString(String text, Amount<Q> measure) {
        setText(text);
        setAmount(measure);
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
}
