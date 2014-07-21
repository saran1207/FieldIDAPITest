package com.n4systems.fieldid.wicket.pages.loto.copy;

import com.n4systems.fieldid.wicket.components.loto.ProcedureListPanel;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.loto.PublishedListAllPage;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class CopyProceduresList extends PublishedListAllPage {

    protected IModel<Asset> assetModel;

    public CopyProceduresList(PageParameters params) {
        Long assetId = params.get("uniqueID").toLong();
        this.assetModel = new EntityModel<Asset>(Asset.class, assetId).withLocalization(true);
    }

    public CopyProceduresList( IModel<Asset> assetModel) {
        this.assetModel = assetModel;
    }

    public CopyProceduresList(Asset asset) {
        this.assetModel = new EntityModel<Asset>(Asset.class, asset.getId()).withLocalization(true);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId).setVisible(false));
    }

    @Override
    protected AbstractColumn getActionsColumn(ProcedureListPanel procedureDefinitionListPanel) {
        return new AbstractColumn<ProcedureDefinition>(Model.of("")) {
            @Override
            public void populateItem(Item<ICellPopulator<ProcedureDefinition>> cellItem, String componentId, IModel<ProcedureDefinition> rowModel) {
                cellItem.add(new CopyActionCell(componentId, rowModel,  assetModel));
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.select_existing_to_copy", assetModel.getObject().getIdentifier()));
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new CancelActionGroup(actionGroupId, assetModel);
    }

}
