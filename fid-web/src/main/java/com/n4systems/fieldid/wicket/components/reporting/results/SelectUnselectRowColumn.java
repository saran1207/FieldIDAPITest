package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.model.BaseEntity;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class SelectUnselectRowColumn<T extends BaseEntity> extends AbstractColumn<T> {

    private MultiIdSelection multiIdSelection;
    private PropertyModel<Boolean> pageSelectedModel;

    public SelectUnselectRowColumn(MultiIdSelection multiIdSelection, PropertyModel<Boolean> pageSelectedModel) {
        super(new Model<String>(""));
        this.multiIdSelection = multiIdSelection;
        this.pageSelectedModel = pageSelectedModel;
    }

    @Override
    public void populateItem(final Item<ICellPopulator<T>> item, String componentId, final IModel<T> rowModel) {
        item.add(new SelectUnselectCell(componentId, new ItemIsSelectedModel(rowModel)) {
            @Override
            protected void onSelectUnselect(AjaxRequestTarget target) {
                onSelectUnselectRow(target);
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

    protected void onSelectUnselectRow(AjaxRequestTarget target) { }
    protected void onSelectUnselectPage(AjaxRequestTarget target) { }

    class ItemIsSelectedModel implements IModel<Boolean> {
        private IModel<T> model;
    
        public ItemIsSelectedModel(IModel<T> model) {
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
