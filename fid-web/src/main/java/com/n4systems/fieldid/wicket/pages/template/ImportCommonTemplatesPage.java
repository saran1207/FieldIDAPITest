package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.service.catalog.PublishedCatalogService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.Tenant;
import com.n4systems.util.ListingPair;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Created by agrabovskis on 2019-01-08.
 */
public class ImportCommonTemplatesPage extends FieldIDFrontEndPage {

    @SpringBean
    private PublishedCatalogService publishedCatalogService;

    public ImportCommonTemplatesPage(PageParameters params) {
        super(params);
        addComponents();
    }

    public ImportCommonTemplatesPage() {
        super();
        addComponents();
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.import_common_templates"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        //response.renderCSSReference("style/legacy/pageStyles/quick_setup_wizard.css");
        super.renderHead(response);
    }

    private void addComponents() {

       final List<AjaxCheckBox> checkBoxes = new ArrayList<>();

        WebMarkupContainer selections = new WebMarkupContainer("assetTypeSelections");
        //selections.setOutputMarkupId(true);

        AjaxLink selectAll = new AjaxLink("selectAll") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                for (AjaxCheckBox ajaxCheckBox : checkBoxes) {
                    ajaxCheckBox.getModel().setObject(true);
                    target.add(ajaxCheckBox);
                }
                System.out.println("set all to true");
            }
        };
        add(selectAll);

        AjaxLink selectNone = new AjaxLink("selectNone") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                for (AjaxCheckBox ajaxCheckBox : checkBoxes) {
                    ajaxCheckBox.getModel().setObject(false);
                    target.add(ajaxCheckBox);
                }
                System.out.println("Set all to false");
            }
        };
        add(selectNone);

        add(selections);

        final DataView<AssetTypeGroup> assetTypeGroupList =
                new DataView<AssetTypeGroup>("publishedAssetTypeGroups", new ListDataProvider<AssetTypeGroup>() {
                    @Override
                    protected List<AssetTypeGroup> getData() {
                        Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                        return new ArrayList(publishedCatalogService.getPublishedAssetTypeGroups(houseAccountTenant));
                    }
                }) {
                    @Override
                    protected void populateItem(Item<AssetTypeGroup> item) {
                        final String groupName = item.getModelObject().getName();
                        item.add(new Label("groupName", groupName));
                        final DataView<ListingPair> assetTypeList = new DataView<ListingPair>("publishedAssetTypeList", new ListDataProvider<ListingPair>(){
                            @Override
                            protected List<ListingPair> getData() {
                                Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                                return publishedCatalogService.getPublishedAssetTypesForGroup(houseAccountTenant, groupName);
                            }
                        }) {
                            @Override
                            protected void populateItem(Item<ListingPair> item) {
                                populateAssetTypeSelection(checkBoxes, item);
                            }
                        };
                        item.add(assetTypeList);
                    }
                };
        selections.add(assetTypeGroupList);

        final DataView<ListingPair> assetTypeUngroupedList = new DataView<ListingPair>("publishedAssetTypeUngroupedList", new ListDataProvider<ListingPair>() {
            @Override
            protected List<ListingPair> getData() {
                Tenant houseAccountTenant = publishedCatalogService.getHouseAccountTenant();
                return publishedCatalogService.getPublishedAssetTypesUngrouped(houseAccountTenant);
            }
        }) {
            @Override
            protected void populateItem(Item<ListingPair> item) {
               populateAssetTypeSelection(checkBoxes, item);
            }
        };

        selections.add(assetTypeUngroupedList);
    }

    private void populateAssetTypeSelection(List<AjaxCheckBox> checkBoxes, Item<ListingPair> item) {

        String assetTypeId = item.getModelObject().getId().toString();
        IModel<Boolean> checkboxModel = Model.of(false);
        AjaxCheckBox ajaxCheckBox = new AjaxCheckBox("publishedAssetTypeSelection", checkboxModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                System.out.println("checkbox state changed for " + assetTypeId);
            }
        };
        checkBoxes.add(ajaxCheckBox);
        item.add(ajaxCheckBox);
        item.add(new Label("publishedAssetTypeName", item.getModelObject().getName()));
    }

}
