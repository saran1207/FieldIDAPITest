package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class SelectUnselectRowColumn extends AbstractColumn<RowView> {

    private MultiIdSelection multiIdSelection;
    private PropertyModel<Boolean> pageSelectedModel;
    private DataTable dataTable;
    int index = 0;

    public SelectUnselectRowColumn(MultiIdSelection multiIdSelection, PropertyModel<Boolean> pageSelectedModel) {
        super(new Model<String>(""));
        this.multiIdSelection = multiIdSelection;
        this.pageSelectedModel = pageSelectedModel;
    }

    @Override
    public void populateItem(final Item<ICellPopulator<RowView>> item, String componentId, final IModel<RowView> rowModel) {
        final String rowId = item.getParent().getParent().getMarkupId();
        item.add(new AttributeAppender("class", " select-column"));

        int currentPage = dataTable.getCurrentPage();
        int itemsPerPage = dataTable.getItemsPerPage();
        int currentResultIndex = (currentPage * itemsPerPage) + (index++);

        SelectUnselectCell selectUnselectCell = new SelectUnselectCell(componentId, new ItemIsSelectedModel(rowModel, currentResultIndex)) {
            @Override
            protected void onSelectUnselect(AjaxRequestTarget target) {
                onSelectUnselectRow(target);
            }

            @Override
            public void renderHead(IHeaderResponse response) {
                String checkboxSelector = "$('#" + selectCheckbox.getMarkupId() + "')";
                response.renderOnDomReadyJavaScript(checkboxSelector + ".bind('click', function() {  showRowSelectionStatus(" + checkboxSelector + ", '" + rowId + "', '" + dataTable.getMarkupId() + "') } )");
            }
        };
        item.add(selectUnselectCell);
    }

    @Override
    public Component getHeader(String componentId) {
        return new SelectUnselectCell(componentId, pageSelectedModel) {
            @Override
            protected void onSelectUnselect(AjaxRequestTarget target) {
                onSelectUnselectPage(target);
            }
        };
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    public void detach() {
        super.detach();
        index = 0;
    }

    protected void onSelectUnselectRow(AjaxRequestTarget target) { }
    protected void onSelectUnselectPage(AjaxRequestTarget target) { }

    class ItemIsSelectedModel implements IModel<Boolean> {
        private IModel<RowView> model;
        private int index;

        public ItemIsSelectedModel(IModel<RowView> model, int index) {
            this.model = model;
            this.index = index;
        }

        @Override
        public Boolean getObject() {
            return multiIdSelection.containsIndex(index);
        }

        @Override
        public void setObject(Boolean selected) {
            if (selected) {
                multiIdSelection.addId(index, model.getObject().getId());
            } else {
                multiIdSelection.removeIndex(index);
            }
        }

        @Override
        public void detach() {
        }
    }

}
