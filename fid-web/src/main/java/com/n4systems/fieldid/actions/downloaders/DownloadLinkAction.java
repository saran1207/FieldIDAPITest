package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.downloadlink.DownloadsByDownloadIdLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.TemplateMailMessage;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class DownloadLinkAction extends AbstractDownloadAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DownloadLinkAction.class);
	
	private Long fileId;
	private DownloadLink downloadLink;
	private DownloadLink publicDownloadLink;
	private List<DownloadLink> downloads;
	private String downloadLinkName;
	private DownloadLinkSaver downloadLinkSaver;
	private String recipients;
	private String subject;
	private String message;
	private String downloadId;
	
	public DownloadLinkAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		downloadLinkSaver = new DownloadLinkSaver();
	}

	public static String buildDownloadUrl(AbstractAction action) {
		return action.createActionURI("showDownloads") + "?fileId=";
	}

	@Override
	protected boolean initializeDownload() {
		if (fileId != null) {
			downloadLink = loadDownloadLink();
			
			if (downloadLink == null) {
				logger.warn(String.format("Download requested for non-existant, expired or unloadable link [%d]", fileId));
				addFlashErrorText("error.download_failed");
				return false;
			}
			
			return true;
		}else if (downloadId!=null){
			downloadLink = loadPublicDownloadLink();
			
			if (downloadLink == null) {
				logger.warn(String.format("Download requested for non-existant, expired or unloadable link [%d]", fileId));
				addFlashErrorText("error.download_failed");
				return false;
			}
			
			return true;
		}
		return false;
	
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
	
	@SkipValidation
	public String doDownload() {
		downloadLink = loadDownloadLink();
		if (!downloadLink.getState().equals(DownloadState.DOWNLOADED)) {
			downloadLink.setState(DownloadState.DOWNLOADED);
			downloadLinkSaver.update(downloadLink); 
		}
		return super.doDownload();
	}
	
	@SkipValidation
	public String doDownloadPublicFile() {
		publicDownloadLink = loadPublicDownloadLink();
		if (!publicDownloadLink.getState().equals(DownloadState.DOWNLOADED)) {
			publicDownloadLink.setState(DownloadState.DOWNLOADED);
			downloadLinkSaver.update(publicDownloadLink); 
		}
		return super.doDownload();
	}
	
	protected DownloadLink loadDownloadLink() {
		FilteredIdLoader<DownloadLink> linkLoader = getLoaderFactory().createFilteredIdLoader(DownloadLink.class);
		linkLoader.setId(fileId);
		
		return linkLoader.load();
	}
	
	protected DownloadLink loadPublicDownloadLink(){
		//FilteredIdLoader<DownloadLink> linkLoader = getOpenSecurityFilteredLoaderFactory().createFilteredIdLoader(DownloadLink.class);
		//linkLoader.setId(fileId);
		DownloadsByDownloadIdLoader publicDownloadLinkLoader = getOpenSecurityFilteredLoaderFactory().createPublicDownloadLinkLoader();
		publicDownloadLinkLoader.setDownloadId(getDownloadId());
		
		return publicDownloadLinkLoader.load();
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
	
	@SkipValidation
	public String doPublicDownload() {
		return SUCCESS;
	}
	
	public String doSave() {
		downloadLink = loadDownloadLink();
		downloadLink.setName(downloadLinkName);
		downloadLinkSaver.update(downloadLink); 
		return SUCCESS;	
	}
	
	@SkipValidation
	public String doDelete() {
		downloadLink = loadDownloadLink();
		downloadLink.setState(DownloadState.DELETED);
		downloadLinkSaver.update(downloadLink);
		fileId = null;
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEmail() {
		
		boolean success = true;
		for (String email : parseRecipients(recipients)){
			try {
				TemplateMailMessage downloadMessage = buildDownloadMessage(email);
				ServiceLocator.getMailManager().sendMessage(downloadMessage);
				
				} catch (MessagingException e) {
					logger.error("Could not send download message", e);
					addFlashErrorText("error.problem_sending_email");
					success=false;
				} catch (RuntimeException e) {
					logger.error("Could not send download message", e);
					addFlashErrorText("error.problem_sending_email");
					success=false;
				}
				
				if (success) {
					addFlashMessageText("label.invitation_sent");
			}
		}
		return SUCCESS;
	}

	private TemplateMailMessage buildDownloadMessage(String email) {
		subject = getText("label.you_have_received_a_file_from") +" "+ getSessionUser().getName() + " " + getText("label.via_fieldid");
		
		TemplateMailMessage invitationMessage = new TemplateMailMessage(subject, "downloadLink");
		invitationMessage.getToAddresses().add(email);
		invitationMessage.getTemplateMap().put("message", getMessage());
		invitationMessage.getTemplateMap().put("downloadUrl", getDownloadUrl());
		invitationMessage.getTemplateMap().put("expiryDate", getExpiresText(getPublicDownloadLink().getCreated()));
		invitationMessage.getTemplateMap().put("senderName", getSessionUser().getName());
		
		return invitationMessage;
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
	
	public Date getExpiryDate(Date created){
		Integer expireTTL = getConfigContext().getInteger(ConfigEntry.DOWNLOAD_TTL_DAYS);

		// add the TTL and truncate back to midnight
		return DateHelper.truncate(DateHelper.addDaysToDate(created, expireTTL.longValue()), DateHelper.DAY);

	}
	
	public DownloadLink getDownloadLink() {
		if(downloadLink == null) {
			downloadLink = loadDownloadLink();
		}
		return downloadLink;
	}
	
	public DownloadLink getPublicDownloadLink() {
		if(downloadLink == null) {
			downloadLink = loadPublicDownloadLink();
		}
		return downloadLink;
	}
	
	public String getDownloadLinkName() {
		return downloadLinkName;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.reporttitlerequired")
	public void setDownloadLinkName(String name) {
		this.downloadLinkName = name;
	}

	public void setDownloadLinkSaver(DownloadLinkSaver downloadLinkSaver) {
		this.downloadLinkSaver = downloadLinkSaver;
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, key="label.email_required", message="")
	public String getRecipients() {
		return recipients;
	}
	
	
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getBody() {
		return message;
	}

	public void setBody(String body) {
		this.message = body;
	}
	
	public String getDownloadUrl(){
		return createActionURIWithParameters(getPrimaryOrg().getTenant(), "public/publicDownload", "downloadId="+getDownloadLink().getDownloadId());
	}
	
	private String[] parseRecipients(String recipients){
		return recipients.split(",");
	}

	public String getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(String downloadId) {
		this.downloadId = downloadId;
	}

	public String getMessage() {
		if (message==null){
			message = getText("label.download_link_message1") + "\n\n" + getDownloadUrl() + "\n\n" + getText("label.this_link_will_expire") + getExpiresText(getDownloadLink().getCreated()) + "\n\n" + getText("label.regards") + "\n"
			+ getSessionUser().getName();
		}
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
