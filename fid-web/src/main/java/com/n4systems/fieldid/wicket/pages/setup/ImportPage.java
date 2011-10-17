package com.n4systems.fieldid.wicket.pages.setup;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.model.AssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.EventTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;

public class ImportPage extends SetupPage {

    public ImportPage() {
        IModel<List<AssetType>> assetTypeListModel = new AssetTypesForTenantModel();
        IModel<List<EventType>> eventTypeListModel = new EventTypesForTenantModel();

        add(new ImportOwnersForm("importOwnersForm"));
        add(new ImportAssetsForm("importAssetsForm"));
        add(new ImportEventsForm("importEventsForm"));
        add(new ImportAutoAttributesForm("importAutoAttributesForm"));
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

        public ImportAutoAttributesForm(String id) {
            super(id);

            add(new AjaxButton("startImportButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getResponse().redirect("/fieldid/autoAttributeImportExport.action");
                }
            });

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
