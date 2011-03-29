package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class DownloadLinkAction extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DownloadLinkAction.class);
	
	private Long fileId;
	private DownloadLink downloadLink;
	private List<DownloadLink> downloads;
	private String downloadLinkName;
	
	public DownloadLinkAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public static String buildDownloadUrl(AbstractAction action) {
		return action.createActionURI("showDownloads") + "?fileId=";
	}

	@Override
	protected boolean initializeDownload() {
		if (fileId == null) {
			return false;
		}
		
		downloadLink = loadDownloadLink();
		
		if (downloadLink == null) {
			logger.warn(String.format("Download requested for non-existant, expired or unloadable link [%d]", fileId));
			addFlashErrorText("error.download_failed");
			return false;
		}
		
		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		logger.warn(String.format("Could not open file [%s], requested by download [%s]", getFile(), downloadLink));
		addFlashErrorText("error.download_failed");
		return ERROR;
	}
	
	@Override
	public String getFileName() {
		return downloadLink.prepareFileName();
	}
	
	@Override
	public String getContentType() {
		return downloadLink.getContentType().getMimeType();
	}
	
	@Override
	public File getFile() {
		return downloadLink.getFile();
	}
	
	protected DownloadLink loadDownloadLink() {
		FilteredIdLoader<DownloadLink> linkLoader = getLoaderFactory().createFilteredIdLoader(DownloadLink.class);
		linkLoader.setId(fileId);
		
		return linkLoader.load();
	}
	
	@SkipValidation
	public String doShow() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		return SUCCESS;		
	}
	
	@SkipValidation
	public String doCancel() {
		return SUCCESS;	
	}
	
	public String doSave() {
		downloadLink = loadDownloadLink();
		downloadLink.setName(downloadLinkName);
		new DownloadLinkSaver().update(downloadLink); 
		return SUCCESS;	
	}
	
	@SkipValidation
	public String doDelete() {
		new DownloadLinkSaver().remove(loadDownloadLink());
		fileId = null;
		return SUCCESS;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
	public Long getFileId() {
		return fileId;
	}
	
	public List<DownloadLink> getDownloads() {
		if (downloads == null) {
			downloads = getLoaderFactory().createDownloadLinkListLoader().load();
		}
		return downloads;
	}
		
	public String getExpiresText(Date created) {
		Integer expireTTL = getConfigContext().getInteger(ConfigEntry.DOWNLOAD_TTL_DAYS);

		// add the TTL and truncate back to midnight
		Date expiresOn = DateHelper.truncate(DateHelper.addDaysToDate(created, expireTTL.longValue()), DateHelper.DAY);

		// this won't overflow unless the ttl is set to about 6 billion years
		int daysLeft = DateHelper.getDaysFromToday(expiresOn).intValue();
		
		// This shouldn't happen but if it does (say if the delete job didn't run for some reason)
		// we'll continue to show today.
		if (daysLeft < 0) {
			daysLeft = 0;
		}
		
		String expiresText;
		switch (daysLeft) {
			case 0:
				expiresText = getText("label.today");
				break;
			case 1:
				expiresText = getText("label.tomorrow");
				break;
			default:
				expiresText = getText("label.n_days", new String[] {String.valueOf(daysLeft)});
				break;
		}
		return expiresText;
	}

	public DownloadLink getDownloadLink() {
		if(downloadLink == null) {
			downloadLink = loadDownloadLink();
		}
		return downloadLink;
	}
	
	
	public String getDownloadLinkName() {
		return downloadLinkName;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.required")
	public void setDownloadLinkName(String name) {
		this.downloadLinkName = name;
	}

}
