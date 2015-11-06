package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.AssetType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EnableByAssetTypePage extends FieldIDTemplatePage {

    @SpringBean
    private AssetTypeService assetTypeService;

    @SpringBean
    private AssetService assetService;

    private IModel<List<AssetType>> assetTypesList;
    private Form<Void> form;

    public EnableByAssetTypePage() {
        super();

        assetTypesList = new ListModel<AssetType>(assetTypeService.getAssetTypes());

        add(form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                List<AssetType> existingAssetTypes = assetTypeService.getAssetTypes();
                List<AssetType> updatedAssetTypes = assetTypesList.getObject();

                int index = 0;
                for(AssetType assetType: updatedAssetTypes) {
                    if(assetType.hasProcedures() != existingAssetTypes.get(index).hasProcedures()) {
                        assetTypeService.update(assetType);
                        assetService.updateActiveProcedureCount(assetType);
                    }
                    index++;
                }
                FieldIDSession.get().info(new FIDLabelModel("message.enable_by_asset_type_success").getObject());
                setResponsePage(EnableByAssetTypePage.class);
            }
        });

        form.setOutputMarkupId(true);
        form.add(new ListView<AssetType>("assetType", assetTypesList) {

            @Override
            protected void populateItem(ListItem<AssetType> item) {
                item.add(new CheckBox("enableProceduresCheck", new PropertyModel<Boolean>(item.getModel(), "hasProcedures")));
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "displayName")));
            }
        });

        form.add(new AjaxLink<Void>("all") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setAllAssetTypes(true);
                target.add(form);
            }
        });

        form.add(new AjaxLink<Void>("none") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setAllAssetTypes(false);
                target.add(form);
            }
        });

        form.add(new SubmitLink("save"));
        form.add(new BookmarkablePageLink<DashboardPage>("cancel", DashboardPage.class));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.enable_by_asset_type"));
    }

    private void setAllAssetTypes(Boolean enabled) {
          for (AssetType assetType: assetTypesList.getObject()) {
              assetType.setHasProcedures(enabled);
          }
    }
}
