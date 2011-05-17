package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.renderer.AssetTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.model.AssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.AssetTypesWithAutoAttributeCriteria;
import com.n4systems.fieldid.wicket.model.EventTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ImportPage extends SetupPage {

    public ImportPage() {
        IModel<List<AssetType>> assetTypeListModel = new AssetTypesForTenantModel(getSecurityFilter()).postFetchFields("autoAttributeCriteria");
        IModel<List<EventType>> eventTypeListModel = new EventTypesForTenantModel(getSecurityFilter());
        IModel<List<AssetType>> assetTypesWithCriteriaModel = new AssetTypesWithAutoAttributeCriteria(assetTypeListModel);

        add(new ImportOwnersForm("importOwnersForm"));
        add(new ImportAssetsForm("importAssetsForm", assetTypeListModel));
        add(new ImportEventsForm("importEventsForm", eventTypeListModel));
        add(new ImportAutoAttributesForm("importAutoAttributesForm", assetTypesWithCriteriaModel));
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

        private AssetType assetType;

        ImportAssetsForm(String id, IModel<List<AssetType>> assetTypeListModel) {
            super(id);
            boolean emptyAssetList = assetTypeListModel.getObject().isEmpty();
            assetType = emptyAssetList ? null : assetTypeListModel.getObject().get(0);

            DropDownChoice<AssetType> assetTypeChoice = new DropDownChoice<AssetType>("assetTypeSelect", new PropertyModel<AssetType>(this, "assetType"), assetTypeListModel, new AssetTypeChoiceRenderer());
            assetTypeChoice.setNullValid(false).setVisible(!emptyAssetList);

            NonWicketLink addAssetTypeLink = new NonWicketLink("addAssetTypeLink", "assetTypeEdit.action");
            addAssetTypeLink.setVisible(emptyAssetList);

            EnclosureContainer selectContainer = new EnclosureContainer("assetTypeSelectContainer", assetTypeChoice);
            selectContainer.add(assetTypeChoice);
            selectContainer.add(new AjaxButton("startImportButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/assetImportExport.action?assetTypeId=" + assetType.getId());
                }
            });
            add(selectContainer);

            EnclosureContainer linkContainer = new EnclosureContainer("addAssetTypeLinkContainer", addAssetTypeLink);
            linkContainer.add(addAssetTypeLink);
            add(linkContainer);
        }

    }

    class ImportEventsForm extends Form {

        private EventType eventType;

        ImportEventsForm(String id, IModel<List<EventType>> eventTypeListModel) {
            super(id);
            boolean emptyEventList = eventTypeListModel.getObject().isEmpty();
            eventType = emptyEventList ? null : eventTypeListModel.getObject().get(0);

            DropDownChoice<EventType> eventTypeChoice = new DropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(this, "eventType"), eventTypeListModel, new EventTypeChoiceRenderer());
            eventTypeChoice.setNullValid(false).setVisible(!emptyEventList);

            NonWicketLink addEventTypeLink = new NonWicketLink("addEventTypeLink", "eventTypeAdd.action");
            addEventTypeLink.setVisible(emptyEventList);

            EnclosureContainer selectContainer = new EnclosureContainer("eventTypeSelectContainer", eventTypeChoice);
            selectContainer.add(eventTypeChoice);
            selectContainer.add(new AjaxButton("startImportButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/eventImportExport.action?uniqueID=" + eventType.getId());
                }
            });
            add(selectContainer);

            EnclosureContainer linkContainer = new EnclosureContainer("addEventTypeLinkContainer", addEventTypeLink);
            linkContainer.add(addEventTypeLink);
            add(linkContainer);
        }

    }

    class ImportAutoAttributesForm extends Form {

        private AssetType assetType;

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

}
