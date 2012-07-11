package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;

import java.util.List;

public class SimpleDefaultDataTable<T> extends DataTable<T> {
    
    public SimpleDefaultDataTable(String id, List columns, ISortableDataProvider dataProvider, int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);		
        
        addTopToolbar(new NavigationToolbar(this) {
            @Override
            protected PagingNavigator newPagingNavigator(String navigatorId, DataTable<?> table) {
                return new CustomPagingNavigator(navigatorId, table);
            }
        });

		addTopToolbar(new HeadersToolbar(this, dataProvider));
		addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
	protected Item<T> newRowItem(final String id, final int index, final IModel<T> model)
	{
		return new OddEvenItem<T>(id, index, model);
	}
}
