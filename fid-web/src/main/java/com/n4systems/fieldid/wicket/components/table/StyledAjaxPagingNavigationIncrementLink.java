package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class StyledAjaxPagingNavigationIncrementLink extends AjaxPagingNavigationIncrementLink {

    private StyledAjaxPagingLinkListener pageChangeListener;

    public StyledAjaxPagingNavigationIncrementLink(final String id, final IPageable pageable,
                                                   final int increment, StyledAjaxPagingLinkListener pageChangeListener) {
        super(id, pageable, increment);
        this.pageChangeListener = pageChangeListener;
    }

    @Override
    protected void disableLink(final ComponentTag tag) {
        tag.put("style", "display:none");
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        super.onClick(target);
        pageChangeListener.onCurrentPageChange(target);
    }
}
