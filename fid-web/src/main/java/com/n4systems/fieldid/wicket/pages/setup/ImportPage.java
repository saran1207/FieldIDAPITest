package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.wicket.pages.asset.AssetImportPage;
import com.n4systems.fieldid.wicket.pages.customers.CustomerActionsPage;
import com.n4systems.fieldid.wicket.pages.customers.CustomerDivisionsListPanel;
import com.n4systems.fieldid.wicket.pages.event.EventImportPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.flow.RedirectToUrlException;

public class ImportPage extends SetupPage {

    public ImportPage() {
        add(new AjaxLink<CustomerActionsPage>("importOwnersButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(CustomerActionsPage.class);
            }
        });
        add(new AjaxLink<AssetImportPage>("importAssetsButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(AssetImportPage.class);
            }
        });
        add(new AjaxLink<EventImportPage>("importEventsButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(EventImportPage.class);
            }
        });
        add(new ImportAutoAttributesForm("importAutoAttributesForm"));
        add(new ImportUsersForm("importUsersForm"));
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
