package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class DownloadExportNotificationWindow extends ModalWindow {

    public DownloadExportNotificationWindow(String id, LoaderFactory loaderFactory, IModel<Long> fileIdModel, IModel<String> downloadFileNameModel) {
        super(id);

        setInitialWidth(520);
        setInitialHeight(210);
        setTitle(new FIDLabelModel("message.downloadbeinggenerated").getObject());

        // Set the content panel, implementing the abstract methods
        setContent(new DownloadExportNotificationPanel(this.getContentId(), loaderFactory, fileIdModel, downloadFileNameModel) {

            @Override
            protected void closeWindow(AjaxRequestTarget target) {
                DownloadExportNotificationWindow.this.close(target);
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        // Override caption height set by Wicket modal.css
        response.renderCSS(".w_captionText {padding-left: 12px; padding-top: 5px; padding-bottom: 5px; font-size: 1.5em;}", null);
        response.renderCSS(".w_caption {height: 2.5em !important;}", null);
        response.renderCSS(".formActions {padding-top: 10px; border-top: 1px solid #D0DAFD;}", null);
    }
}
