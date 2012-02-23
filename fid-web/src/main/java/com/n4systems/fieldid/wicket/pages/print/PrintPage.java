package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.SearchCriteriaModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class PrintPage<T extends SearchCriteriaModel> extends FieldIDAuthenticatedPage {

    protected IModel<T> criteria;

    private @SpringBean PersistenceService persistenceService;

    public PrintPage(IModel<T> criteria) {
        this.criteria = criteria;

        final DownloadLink downloadLink = createDownloadLink();
        add(new RenameDownloadForm("renameForm", new Model<DownloadLink>(downloadLink)));
    }

    class RenameDownloadForm extends Form<DownloadLink> {

        public RenameDownloadForm(String id, IModel<DownloadLink> downloadLink) {
            super(id, downloadLink);

            add(new TextField<String>("name", new PropertyModel<String>(downloadLink, "name")));
            add(new Button("saveAndGoToDownloadsButton"));
            add(new AjaxLink("saveAndCloseLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    target.appendJavaScript("jQuery.colorbox.close();");
                }
            });

        }

        @Override
        protected void onSubmit() {
            persistenceService.update(getModelObject());
            throw new RedirectToUrlException("/showDownloads.action");
        }

    }

    protected abstract DownloadLink createDownloadLink();

}
