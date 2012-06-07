package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.fieldid.wicket.components.search.results.SelectionStatusPanel;
import com.n4systems.fieldid.wicket.data.ListableSortableDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SimpleDataTable<T> extends Panel {
	
    private DataTable<T> table;
    private MultiIdSelection multiIdSelection;
    private SelectionStatusPanel selectionStatusPanel;
    private boolean displayPagination = true;
    private String cssClass = "list";

    public SimpleDataTable(String id, final List<IColumn<T>> columns,
        ISortableDataProvider<T> dataProvider, int rowsPerPage) {
        this(id, columns, dataProvider, rowsPerPage, new MultiIdSelection());
    }

    public SimpleDataTable(String id, final List<IColumn<T>> columns,
        ISortableDataProvider<T> dataProvider, int rowsPerPage, MultiIdSelection selection) {
        this(id, columns, dataProvider, rowsPerPage, "label.noresults", "message.emptysearch", selection);
    }

    public SimpleDataTable(String id, final List<IColumn<T>> columns,
		final ISortableDataProvider<T> dataProvider, int rowsPerPage,
        String emptyResultsTitleKey, String emptyResultsMessageKey, MultiIdSelection selection) {
		super(id);

        multiIdSelection = selection;

        setOutputMarkupId(true);

        table = new DataTable<T>("table", columns, dataProvider, rowsPerPage) {
            @Override
            protected Item<T> newRowItem(String id, int index, IModel<T> rowModel) {
                Item<T> rowItem = super.newRowItem(id, index, rowModel);
                rowItem.setOutputMarkupId(true);
                rowItem.add(new HighlightIfSelectedBehavior(rowModel, multiIdSelection));
                onRowItemCreated(rowItem, rowModel);
                return rowItem;
            }

            @Override
            protected Item<IColumn<T>> newCellItem(String id, int index, IModel<IColumn<T>> tiModel) {
                Item<IColumn<T>> cellItem = super.newCellItem(id, index, tiModel);
                cellItem.setOutputMarkupId(true);
                return cellItem;
            }

        };
        table.setOutputMarkupPlaceholderTag(true);
        table.add(new AttributeAppender("class", new PropertyModel<String>(this, "cssClass"), " "));

		table.addTopToolbar(new HeadersToolbar(table, dataProvider) {
            @Override
            public boolean isVisible() {
                return table.getRowCount() > 0;
            }

            @Override
            protected WebMarkupContainer newSortableHeader(String headerId, final String property, ISortStateLocator locator) {
                return new OrderByBorder(headerId, property, locator) {
                    @Override
                    protected void onSortChanged() {
                        getTable().setCurrentPage(0);

                        ISortState sortState = dataProvider.getSortState();
                        SortOrder propertySortOrder = sortState.getPropertySortOrder(property);
                        SortDirection sortDirection = propertySortOrder == SortOrder.ASCENDING ? SortDirection.ASC : SortDirection.DESC;

                        SimpleDataTable.this.onSortChanged(property, sortDirection);
                    }
        		};
            }
        });

        if (dataProvider instanceof ListableSortableDataProvider) {
            add(selectionStatusPanel = new SelectionStatusPanel("selectionStatus", table, selection, (ListableSortableDataProvider)dataProvider) {
                @Override protected void onSelectionChanged(AjaxRequestTarget target) {
                    SimpleDataTable.this.onSelectionChanged(target);
                }
                @Override public boolean isVisible() {
                    return true;
                }
            });
        } else {
            add(new WebMarkupContainer("selectionStatus").setVisible(false));
        }

        add(table);

        add(new AttributeAppender("class", getPagedModel()));
        
        addEmptyResultsDisplay(emptyResultsTitleKey, emptyResultsMessageKey, table);

//        add(new Label("totalResults", createTotalModel()).setOutputMarkupId(true).setVisible(isTotalVisible()));
//        add(numSelectedLabel = new Label("numSelected", createSelectedModel()).setOutputMarkupId(true).setVisible(isSelectedVisible()));
    }

    protected IModel<Integer> createSelectedModel() {
        return new Model<Integer>(0);
    }

    protected IModel<Integer> createTotalModel() {
        return new Model<Integer>(0);
    }

    protected boolean isTotalVisible() {
        return true;
    }

    protected boolean isSelectedVisible() {
        return true;
    }

    protected IModel<?> getPagedModel() {
		return new Model<String>() {
			@Override public String getObject() {
				return table.getPageCount()>1 ? "paged-table" : "";
			}			
		};
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

    public void reset() {
        table.setCurrentPage(0);
    }

    public DataTable<T> getTable() {
        return table;
    }

    public void justSelectedPageWithElements(int itemsJustSelected) {
        selectionStatusPanel.justSelectedPageWithElements(itemsJustSelected);
    }

    protected void onPageChanged(AjaxRequestTarget target) { }

    protected void onSelectionChanged(AjaxRequestTarget target) { }

    protected void onSortChanged(String sortProperty, SortDirection sortDirection) {}

    protected void onRowItemCreated(Item<T> rowItem, IModel<T> rowModel) {}

    public void updateSelectionStatus(AjaxRequestTarget target) {
        target.add(selectionStatusPanel);
    }

    public void setDisplayPagination(boolean displayPagination) {
        this.displayPagination = displayPagination;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/selectionNew.js");
    }

}
