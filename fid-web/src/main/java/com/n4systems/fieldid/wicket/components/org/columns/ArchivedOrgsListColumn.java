package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class ArchivedOrgsListColumn extends PropertyColumn<SecondaryOrg> {

    public ArchivedOrgsListColumn() {
        super(Model.of(""), "");
    }

    @Override
    public void populateItem(Item<ICellPopulator<SecondaryOrg>> item, String componentId, IModel<SecondaryOrg> rowModel) {
        item.add(new ArchivedOrgsListCell(componentId, rowModel) {
            @Override
            protected void onError(AjaxRequestTarget target, String message) {
                ArchivedOrgsListColumn.this.onError(target, message);
            }
        });
    }

    protected void onError(AjaxRequestTarget target, String message) {}
}
