package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class SimpleDataTable<T> extends Panel {

    public SimpleDataTable(String id, final IColumn<T>[] columns,
        ISortableDataProvider<T> dataProvider, int rowsPerPage) {
        this(id, columns, dataProvider, rowsPerPage, "label.noresults", "message.emptysearch");
    }

	public SimpleDataTable(String id, final IColumn<T>[] columns,
		ISortableDataProvider<T> dataProvider, int rowsPerPage,
        String emptyResultsTitleKey, String emptyResultsMessageKey) {
		super(id);

        DataTable<T> table = new DataTable<T>("table", columns, dataProvider, rowsPerPage);
		table.addTopToolbar(new HeadersToolbar(table, dataProvider));

        add(table);

        add(new JumpableNavigationBar("topPagination", table));
        add(new JumpableNavigationBar("bottomPagination", table));

        addEmptyResultsDisplay(emptyResultsTitleKey, emptyResultsMessageKey, table);
	}

    private void addEmptyResultsDisplay(String emptyResultsTitleKey, String emptyResultsMessageKey, final DataTable<T> table) {
        WebMarkupContainer emptyResultsContainer = new WebMarkupContainer("emptyResultsContainer") {
            @Override
            public boolean isVisible() {
                return table.getDataProvider().size() == 0;
            }
        };

        emptyResultsContainer.add(new Label("emptyResultsTitle", new FIDLabelModel(emptyResultsTitleKey)));
        emptyResultsContainer.add(new Label("emptyResultsMessage", new FIDLabelModel(emptyResultsMessageKey)));
        add(emptyResultsContainer);
    }

}
