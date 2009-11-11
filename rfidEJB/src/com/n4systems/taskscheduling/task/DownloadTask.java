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
	
	private final String templateName;
	private final String downloadUrl;
	private final DownloadLink downloadLink;
	private final DownloadLinkSaver linkSaver;
	private final MailManager mailManager;
	
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
	
	abstract protected void generateFile(File downloadFile, UserBean user) throws Exception;
	
	public void run() {
		logger.info(String.format("Download Task Started [%s]", downloadLink));
		
		updateDownloadLinkState(DownloadState.INPROGRESS);
		
		try {
			generateFile(downloadLink.getFile(), downloadLink.getUser());
			
			updateDownloadLinkState(DownloadState.COMPLETED);
	
			sendSuccessNotification(mailManager, downloadLink);
		} catch(MessagingException me) {
			logger.error("Failed to send success notification", me);
		} catch(Exception e) {
			logger.error("Failed to generate download", e);
			
			updateDownloadLinkState(DownloadState.FAILED);
			try {
				sendFailureNotification(mailManager, downloadLink);
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
	
	protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink) throws MessagingException {
		// sub classes may override this to send failure notices
	}
}
