package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class StyledAjaxPagingNavigationLink extends AjaxPagingNavigationLink {

    private boolean hideOnDisable;
    private StyledAjaxPagingLinkListener pageChangeListener;

    public StyledAjaxPagingNavigationLink(final String id, final IPageable pageable, final int pageNumber,
                                          boolean hideOnDisable,
                                          StyledAjaxPagingLinkListener pageChangeListener) {
        super(id, pageable, pageNumber);
        this.hideOnDisable = hideOnDisable;
        this.pageChangeListener = pageChangeListener;
    }

    @Override
    protected void disableLink(final ComponentTag tag) {
        if (hideOnDisable)
            tag.put("style", "display:none");
        else
            super.disableLink(tag);
    }

    public void onClick(AjaxRequestTarget target) {
        super.onClick(target);
        pageChangeListener.onCurrentPageChange(target);
    }
}
