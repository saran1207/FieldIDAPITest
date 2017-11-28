package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class DownloadExportNotificationPage extends FieldIDAuthenticatedPage {

    IModel<DownloadLink> downloadLink;

    public DownloadExportNotificationPage(IModel<DownloadLink> downloadLink) {
        this.downloadLink = downloadLink;
        add(new RenameDownloadForm("renameForm", downloadLink));
    }

    class RenameDownloadForm extends Form<DownloadLink> {
        private boolean gotoDownloads=true;

        public RenameDownloadForm(String id, IModel<DownloadLink> downloadLink) {
            super(id, downloadLink);

            add(new TextField<String>("name", new PropertyModel<String>(downloadLink, "name")));
            add(new AjaxButton("saveAndGoToDownloadsButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript("window.parent.location = '/fieldid/showDownloads.action';");
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });
            add(new AjaxSubmitLink("saveAndCloseLink") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript("window.parent.jQuery.colorbox.close();");
                    gotoDownloads = false;
                }

                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.appendJavaScript("window.parent.jQuery.colorbox.close();");
                    gotoDownloads = false;
                }
            });

        }

        @Override
        protected void onSubmit() {
            System.out.println("RenameDownloadForm.onSubmit clicked");
            DownloadLinkSaver downloadLinkSaver = new DownloadLinkSaver();
            downloadLinkSaver.update(downloadLink.getObject());
        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/downloads.css");
        response.renderCSSReference("style/legacy/fieldid.css");
    }

}
