package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.flow.RedirectToUrlException;

public class ImportPage extends SetupPage {

    public ImportPage() {
        add(new ImportOwnersForm("importOwnersForm"));
        add(new ImportAssetsForm("importAssetsForm"));
        add(new ImportEventsForm("importEventsForm"));
        add(new ImportAutoAttributesForm("importAutoAttributesForm"));
        add(new ImportUsersForm("importUsersForm"));
    }

    class ImportOwnersForm extends Form {
        public ImportOwnersForm(String id) {
            super(id);
            add(createRedirectingAjaxButton("importOwnersButton", "/customerImportExport.action"));
        }
    }

    class ImportAssetsForm extends Form {
        public ImportAssetsForm(String id) {
            super(id);
            add(createRedirectingAjaxButton("importAssetsButton", "/assetImportExport.action"));
        }
    }

    class ImportEventsForm extends Form {
        public ImportEventsForm(String id) {
            super(id);
            add(createRedirectingAjaxButton("importEventsButton", "/eventImportExport.action"));
        }
    }

    class ImportAutoAttributesForm extends Form {
        public ImportAutoAttributesForm(String id) {
            super(id);
            add(createRedirectingAjaxButton("startImportButton", "/autoAttributeImportExport.action"));
        }
    }
    
    class ImportUsersForm extends Form {
        public ImportUsersForm(String id) {
            super(id);
            add(createRedirectingAjaxButton("importUsersButton", "/userImportExport.action"));
        }
    }

    protected AjaxButton createRedirectingAjaxButton(final String id, final String url) {
        return new AjaxButton(id) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                throw new RedirectToUrlException(url);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        };
    }

}
