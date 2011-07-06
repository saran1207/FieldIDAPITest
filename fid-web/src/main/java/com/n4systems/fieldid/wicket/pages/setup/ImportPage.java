package com.n4systems.fieldid.wicket.pages.setup;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.renderer.AssetTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.AssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.AssetTypesWithAutoAttributeCriteria;
import com.n4systems.fieldid.wicket.model.EventTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;

public class ImportPage extends SetupPage {

    public ImportPage() {
        IModel<List<AssetType>> assetTypeListModel = new AssetTypesForTenantModel().postFetchFields("autoAttributeCriteria");
        IModel<List<EventType>> eventTypeListModel = new EventTypesForTenantModel();
        IModel<List<AssetType>> assetTypesWithCriteriaModel = new AssetTypesWithAutoAttributeCriteria(assetTypeListModel);

        add(new ImportOwnersForm("importOwnersForm"));
        add(new ImportAssetsForm("importAssetsForm"));
        add(new ImportEventsForm("importEventsForm"));
        add(new ImportAutoAttributesForm("importAutoAttributesForm", assetTypesWithCriteriaModel));
        add(new ImportUsersForm("importUsersForm"));
    }

    class ImportOwnersForm extends Form {
        public ImportOwnersForm(String id) {
            super(id);
            add(new AjaxButton("importOwnersButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/customerImportExport.action");
                }
            });
        }
    }

    class ImportAssetsForm extends Form {

        public ImportAssetsForm(String id) {
            super(id);
            add(new AjaxButton("importAssetsButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/assetImportExport.action");
                }
            });
        }
    }

    class ImportEventsForm extends Form {
    	
        public ImportEventsForm(String id) {
            super(id);
            add(new AjaxButton("importEventsButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/eventImportExport.action");
                }
            });
        }

    }

    class ImportAutoAttributesForm extends Form {

        private final AssetType assetType;

        public ImportAutoAttributesForm(String id, IModel<List<AssetType>> assetTypesWithCriteriaModel) {
            super(id);

            boolean emptyAssetList = assetTypesWithCriteriaModel.getObject().isEmpty();
            assetType = emptyAssetList ? null : assetTypesWithCriteriaModel.getObject().get(0);

            DropDownChoice<AssetType> assetTypeChoice = new DropDownChoice<AssetType>("assetTypeSelect", new PropertyModel<AssetType>(this, "assetType"), assetTypesWithCriteriaModel, new AssetTypeChoiceRenderer());
            assetTypeChoice.setNullValid(false).setVisible(!emptyAssetList);

            NonWicketLink addAssetTypeLink = new NonWicketLink("addAutoAttributeCriteriaLink", "assetTypeEdit.action");
            addAssetTypeLink.setVisible(emptyAssetList);

            EnclosureContainer selectContainer = new EnclosureContainer("autoAttributeSelectContainer", assetTypeChoice);
            selectContainer.add(assetTypeChoice);
            selectContainer.add(new AjaxButton("startImportButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/autoAttributeImportExport.action?criteriaId=" + assetType.getAutoAttributeCriteria().getId());
                }
            });
            add(selectContainer);

            EnclosureContainer linkContainer = new EnclosureContainer("addAutoAttributeLinkContainer", addAssetTypeLink);
            linkContainer.add(addAssetTypeLink);
            add(linkContainer);
        }

    }
    
    class ImportUsersForm extends Form {
        public ImportUsersForm(String id) {
            super(id);
            add(new AjaxButton("importUsersButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/userImportExport.action");
                }
            });
        }
    }
    
    

}
