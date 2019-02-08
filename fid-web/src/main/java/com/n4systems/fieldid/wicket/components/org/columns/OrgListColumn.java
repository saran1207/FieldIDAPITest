package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class OrgListColumn extends PropertyColumn<SecondaryOrg> {

    public OrgListColumn() {
        super(Model.of(""), "");
    }

    @Override
    public void populateItem(Item<ICellPopulator<SecondaryOrg>> item, String componentId, IModel<SecondaryOrg> rowModel) {
        item.add(new OrgListCell(componentId, rowModel));
    }
}
