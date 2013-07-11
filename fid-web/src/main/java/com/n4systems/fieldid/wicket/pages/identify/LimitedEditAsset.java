package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.ModalLocationPicker;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

public class LimitedEditAsset extends FieldIDFrontEndPage {

    @SpringBean
    private PersistenceService persistenceService;

    public LimitedEditAsset(PageParameters params) {
        Long id = params.get("id").toLongObject();

        add(new EditAssetCustomerInformationForm("editCustomerInformationForm", new EntityModel<Asset>(Asset.class, id)));
    }

    class EditAssetCustomerInformationForm extends Form<Asset> {
        public EditAssetCustomerInformationForm(String id, IModel<Asset> model) {
            super(id, model);

            final ModalLocationPicker locationPicker = new ModalLocationPicker("locationPicker", ProxyModel.of(model, on(Asset.class).getAdvancedLocation()));
            add(locationPicker);
            locationPicker.setOwner(model.getObject().getOwner());

            AutoCompleteOrgPicker ownerPicker = new AutoCompleteOrgPicker("ownerPicker", ProxyModel.of(model, on(Asset.class).getOwner())) {
                { withAutoUpdate(true); }
                @Override
                protected void onUpdate(AjaxRequestTarget target, String hiddenInput, String fieldInput) {
                    locationPicker.setOwner(getModelObject());
                }
            };
            add(ownerPicker);

            add(new TextField<String>("referenceNumber", ProxyModel.of(model, on(Asset.class).getCustomerRefNumber())));
            add(new TextField<String>("purchaseOrder", ProxyModel.of(model, on(Asset.class).getPurchaseOrder())));

            add(new BookmarkablePageLink<Void>("cancelLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(model.getObject().getId())));
        }
    }

}
