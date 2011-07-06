package com.n4systems.fieldid.wicket.data;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;

import java.util.List;

public interface ListableSortableDataProvider<T> extends ISortableDataProvider<T> {

    public List<Long> getIdList();

}
