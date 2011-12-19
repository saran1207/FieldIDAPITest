package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AbstractBehavior;
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

    public SelectUnselectRowColumn(MultiIdSelection multiIdSelection, PropertyModel<Boolean> pageSelectedModel) {
        super(new Model<String>(""));
        this.multiIdSelection = multiIdSelection;
        this.pageSelectedModel = pageSelectedModel;
    }

    @Override
    public void populateItem(final Item<ICellPopulator<RowView>> item, String componentId, final IModel<RowView> rowModel) {
        final String rowId = item.getParent().getParent().getMarkupId();
        SelectUnselectCell selectUnselectCell = new SelectUnselectCell(componentId, new ItemIsSelectedModel(rowModel)) {
            @Override
            protected void onSelectUnselect(AjaxRequestTarget target) {
                onSelectUnselectRow(target);
            }
        };
        item.add(selectUnselectCell);

        final AjaxCheckBox selectCheckbox = selectUnselectCell.getSelectCheckbox();

        selectUnselectCell.getSelectCheckbox().add(new AbstractBehavior() {
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);
                String checkboxSelector = "$('#" + selectCheckbox.getMarkupId() + "')";
                response.renderOnDomReadyJavascript(checkboxSelector + ".bind('click', function() {  showRowSelectionStatus(" + checkboxSelector + ", '" + rowId + "', '" + dataTable.getMarkupId() + "') } )");
            }
        });
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

    protected void onSelectUnselectRow(AjaxRequestTarget target) { }
    protected void onSelectUnselectPage(AjaxRequestTarget target) { }

    class ItemIsSelectedModel implements IModel<Boolean> {
        private IModel<RowView> model;
    
        public ItemIsSelectedModel(IModel<RowView> model) {
            this.model = model;
        }

        @Override
        public Boolean getObject() {
            return multiIdSelection.containsId(model.getObject().getId());
        }

        @Override
        public void setObject(Boolean selected) {
            if (selected) {
                multiIdSelection.addId(model.getObject().getId());
            } else {
                multiIdSelection.removeId(model.getObject().getId());
            }
        }

        @Override
        public void detach() {
        }
    }

}
