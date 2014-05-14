package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.ModalLocationPicker;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

public class LimitedEditAsset extends FieldIDTemplatePage {

    @SpringBean
    private PersistenceService persistenceService;

    public LimitedEditAsset(PageParameters params) {
        Long id = params.get("id").toLongObject();
        Asset asset = persistenceService.find(Asset.class, id);

        add(new EditAssetCustomerInformationForm("editCustomerInformationForm", Model.of(asset)));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.edit_asset"));
    }


    class EditAssetCustomerInformationForm extends Form<Asset> {

        private ModalLocationPicker locationPicker;
        private OrgLocationPicker ownerPicker;

        public EditAssetCustomerInformationForm(String id, final IModel<Asset> model) {
            super(id, model);

            locationPicker = new ModalLocationPicker("locationPicker", ProxyModel.of(model, on(Asset.class).getAdvancedLocation()));
            add(locationPicker);
            locationPicker.setOwner(model.getObject().getOwner());

            ownerPicker = new OrgLocationPicker("ownerPicker", new PropertyModel(model, "owner")) {
                @Override protected void onChanged(AjaxRequestTarget target) {
                    locationPicker.setOwner(getOwner());

                    target.add(locationPicker);
                }

                @Override protected void onError(AjaxRequestTarget target, RuntimeException e) { }
            }.withAutoUpdate();
            add(ownerPicker.setRequired(true).setLabel(new FIDLabelModel("label.owner")));


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

}
