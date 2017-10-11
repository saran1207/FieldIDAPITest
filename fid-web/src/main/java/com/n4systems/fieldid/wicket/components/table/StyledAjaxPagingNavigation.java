package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;

public class StyledAjaxPagingNavigation extends AjaxPagingNavigation {

    private StyledAjaxPagingLinkListener pageChangeListener;

    public StyledAjaxPagingNavigation(final String id, final IPageable pageable, StyledAjaxPagingLinkListener pageChangeListener)
    {
        this(id, pageable, null, pageChangeListener);
    }

    public StyledAjaxPagingNavigation(final String id, final IPageable pageable,
                                      final IPagingLabelProvider labelProvider, StyledAjaxPagingLinkListener pageChangeListener)
    {
        super(id, pageable, labelProvider);
        this.pageChangeListener = pageChangeListener;
    }

    @Override
    protected Link<?> newPagingNavigationLink(String id, IPageable pageable, int pageIndex)
    {
        return new StyledAjaxPagingNavigationLink(id, pageable, pageIndex, false, pageChangeListener);
    }

}
