package com.n4systems.fieldid.actions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import org.apache.commons.lang.StringUtils;

public class DownloadNameAction extends AbstractAction {

    private Long fileId;
    private String reportName;

    public DownloadNameAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    protected DownloadLink loadDownloadLink() {
        FilteredIdLoader<DownloadLink> linkLoader = getLoaderFactory().createFilteredIdLoader(DownloadLink.class);
        linkLoader.setId(fileId);

        return linkLoader.load();
    }

    public String doConfirmDownloadName() {
        DownloadLink downloadLink = loadDownloadLink();

        if(!reportName.equals(downloadLink.getName()) && !StringUtils.isBlank(reportName)) {
            downloadLink.setName(reportName);
            new DownloadLinkSaver().update(downloadLink);
        }

        return SUCCESS;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
