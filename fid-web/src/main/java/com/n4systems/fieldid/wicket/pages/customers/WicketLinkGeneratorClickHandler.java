package com.n4systems.fieldid.wicket.pages.customers;

import java.io.Serializable;


public abstract class WicketLinkGeneratorClickHandler implements Serializable {

    /**
     * Callback to handle the link being clicked.
     */
    abstract public void onClick();
}
