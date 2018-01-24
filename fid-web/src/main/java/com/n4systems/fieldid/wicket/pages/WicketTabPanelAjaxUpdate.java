package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Intended to be added to Wicket tabbed panel so implementers can catch the Ajax update event
 */

public interface WicketTabPanelAjaxUpdate {

    void onWicketTabAjaxUpdate(AjaxRequestTarget target);

}

