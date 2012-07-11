package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

public class CustomPagingNavigator extends PagingNavigator{

    public CustomPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    public CustomPagingNavigator(final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
        super(id,pageable,labelProvider);
    }
}
