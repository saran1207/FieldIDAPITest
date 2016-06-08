package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LimitedEditAsset extends FieldIDTemplatePage {

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private AssetService assetService;

    private Asset asset;

    public LimitedEditAsset(PageParameters params) {
        Long id = params.get("id").toLongObject();
        asset = assetService.findById(id);

        add(new EditAssetCustomerInformationForm("editCustomerInformationForm", Model.of(asset)));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.edit_asset"));
    }

    class EditAssetCustomerInformationForm extends Form<Asset> {

        private OrgLocationPicker locationPicker;

        public EditAssetCustomerInformationForm(String id, final IModel<Asset> model) {
            super(id, model);

            final PropertyModel<BaseOrg> ownerModel = new PropertyModel(model,"owner");
            //Owner Picker
            add(new OrgLocationPicker("owner", ownerModel) {
                @Override
                protected void onChanged(AjaxRequestTarget target) {
                    if(getTextString() != null && getTextString().equals("")) {
                        locationPicker.setLocationOwner(null);
                    } else {
                        locationPicker.setLocationOwner(getOwner());
                    }
                }
            }.withAutoUpdate());

            //Location Picker
            final PropertyModel<Location> locationModel = new PropertyModel<Location>(model, "advancedLocation");
            final PropertyModel<PredefinedLocation> predefinedLocationModel = new PropertyModel<PredefinedLocation>(model, "advancedLocation.predefinedLocation");

            BaseOrg temp = ownerModel.getObject();

            locationPicker = new OrgLocationPicker("location", Model.of(temp), predefinedLocationModel){
                @Override
                public String getWatermarkText() {
                    return new FIDLabelModel("message.locationpicker_watermark").getObject();
                }
            }.withLocations();
            add(locationPicker);

            //Freeform Location
            add(new TextField<String>("freeformLocation", new PropertyModel<String>(locationModel, "freeformLocation")));

            add(new TextField<String>("referenceNumber", new PropertyModel<String>(model, "customerRefNumber")));
            add(new TextField<String>("purchaseOrder", new PropertyModel<String>(model, "purchaseOrder")));

            add(new SubmitLink("saveLink"));
            add(new BookmarkablePageLink<Void>("cancelLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(model.getObject().getId())));
        }

        @Override
        protected void onSubmit() {
            Asset asset = getModel().getObject();
            persistenceService.update(asset);
            setResponsePage(AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId()));
        }
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new Link<Void>(linkId) {
            @Override
            public void onClick() {
                setResponsePage(new AssetSummaryPage(asset));
            }
        }.add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.assetsummary").getObject())));
    }
}
