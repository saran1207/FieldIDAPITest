package com.n4systems.taskscheduling.task;

import java.io.File;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.TemplateMailMessage;

public abstract class DownloadTask implements Runnable {
	protected Logger logger = Logger.getLogger(DownloadTask.class); 
	
	protected final String templateName;
	protected final String downloadUrl;
	protected final DownloadLinkSaver linkSaver;
	protected final MailManager mailManager;
	protected final DownloadLink downloadLink;
	
	public DownloadTask(DownloadLink downloadLink, String downloadUrl, String templateName, DownloadLinkSaver linkSaver, MailManager mailManager) {
		this.downloadLink = downloadLink;
		this.downloadUrl = downloadUrl;
		this.templateName = templateName;
		this.linkSaver = linkSaver;
		this.mailManager = mailManager;
	}
	
	public DownloadTask(DownloadLink downloadLink, String downloadUrl, String templateName) {
		this(downloadLink, downloadUrl, templateName, new DownloadLinkSaver(), ServiceLocator.getMailManager());
	}
	
	abstract protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception;
	
	public void run() {
		logger.info(String.format("Download Task Started [%s]", downloadLink));
		
		updateDownloadLinkState(DownloadState.INPROGRESS);
		
		try {
			generateFile(downloadLink.getFile(), downloadLink.getUser(), downloadLink.getName());
			
			updateDownloadLinkState(DownloadState.COMPLETED);
	
			try {
				// we don't want exceptions coming from the notification to 
				// hit the failure block
				sendSuccessNotification(mailManager, downloadLink);
			} catch(Exception e) {
				logger.error("Failed to send success notification, the download has not been affected", e);
			}
		} catch(Exception e) {
			logger.error("Failed to generate download", e);
			
			updateDownloadLinkState(DownloadState.FAILED);
			try {
				sendFailureNotification(mailManager, downloadLink, e);
			} catch(MessagingException me) {
				logger.error("Failed to send failure notification", me);
			}
		}
		
		logger.info(String.format("Download Task Finished [%s]", downloadLink));
	}
	
	private void updateDownloadLinkState(DownloadState state) {
		downloadLink.setState(state);
		linkSaver.update(downloadLink);
	}
	
	protected void sendSuccessNotification(MailManager mailManager, DownloadLink downloadLink) throws MessagingException {
		TemplateMailMessage tMail = new TemplateMailMessage(downloadLink.getName(), templateName);
		tMail.getToAddresses().add(downloadLink.getUser().getEmailAddress());
		tMail.getTemplateMap().put("downloadLink", downloadLink);
		tMail.getTemplateMap().put("downloadUrl", downloadUrl + downloadLink.getId());
		
		mailManager.sendMessage(tMail);
	}
	
	protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink, Exception cause) throws MessagingException {
		// sub classes may override this to send failure notices
	}
}
