package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Intended to be added to Wicket panel so implementers can catch an Ajax update event
 */

public interface WicketPanelAjaxUpdate {

    void onWicketTabAjaxUpdate(AjaxRequestTarget target);

}

