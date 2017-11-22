package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

abstract public class DownloadExportNotificationPanel extends Panel {

    private LoaderFactory loaderFactory;

    public DownloadExportNotificationPanel(String id, LoaderFactory loaderFactory, IModel<Long> fileIdModel,  IModel<String> downloadFileNameModel) {
        super(id);

        this.loaderFactory = loaderFactory;
        Form form = new Form("form");
        add(form);

        TextField<String> reportName = new TextField<String>("fileName", downloadFileNameModel);
        form.add(reportName);
        form.add(new AjaxButton("saveDownload") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                saveDownloadLink(fileIdModel.getObject(), downloadFileNameModel.getObject());
                closeWindow(target);
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler("fieldid/showDownloads.action"));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }

        });

        form.add(new AjaxLink("saveCloseMessage") {
            public void onClick(AjaxRequestTarget target) {
                saveDownloadLink(fileIdModel.getObject(), downloadFileNameModel.getObject());
                closeWindow(target);
            }
        });

    }


    private void saveDownloadLink(Long fileId, String downloadLinkName) {
        DownloadLink  downloadLink = loadDownloadLink(fileId);
        downloadLink.setName(downloadLinkName);
        DownloadLinkSaver downloadLinkSaver = new DownloadLinkSaver();
        downloadLinkSaver.update(downloadLink);
    }

    protected DownloadLink loadDownloadLink(Long fileId) {
        FilteredIdLoader<DownloadLink> linkLoader = getLoaderFactory().createFilteredIdLoader(DownloadLink.class);
        linkLoader.setId(fileId);

        return linkLoader.load();
    }

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }

    abstract protected void closeWindow(AjaxRequestTarget target);
}
