package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.wicket.components.org.columns.ArchivedOrgsListColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

public class ArchivedOrgsListPage extends OrgsListPage {

    @Override
    protected Model<OrgListFilterCriteria> getOrgListFilterCriteria() {
        return Model.of(new OrgListFilterCriteria(true));
    }

    @Override
    protected List<IColumn<SecondaryOrg>> getOrgTableColumns() {
        List<IColumn<SecondaryOrg>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<SecondaryOrg>(new FIDLabelModel("label.organizationalunits"), "name", "name") {
            @Override
            public void populateItem(Item<ICellPopulator<SecondaryOrg>> cellItem, String componentId, IModel<SecondaryOrg> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new ArchivedOrgsListColumn() {
            @Override
            protected void onError(AjaxRequestTarget target, String message) {
                error(message);
                target.add(feedbackPanel);
            }
        });
        return columns;
    }
}
