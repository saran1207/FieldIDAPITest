package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;


import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.mail.MailManager;
import com.n4systems.model.Event;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.safetynetwork.SafetyNetworkEventLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredAssetEventLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LazyLoadingList;
import com.n4systems.reporting.EventReportType;
import com.n4systems.reporting.EventCertificateReportGenerator;


public class PrintAllEventCertificatesTask extends DownloadTask {
	private final EventCertificateReportGenerator reportGen;
	private final SafetyNetworkEventLoader eventLoader;
	
	private List<Long> eventIds;
	private EventReportType reportType;
	
	public PrintAllEventCertificatesTask(DownloadLink downloadLink, String downloadUrl, DateTimeDefiner dateDefiner, SafetyNetworkEventLoader eventLoader) {
		super(downloadLink, downloadUrl, "printAllEventCerts");
		this.reportGen = new EventCertificateReportGenerator(dateDefiner);
		this.eventLoader = eventLoader;
	}
	
	public PrintAllEventCertificatesTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new DateTimeDefiner(downloadLink.getUser()), new SafetyNetworkRegisteredAssetEventLoader(downloadLink.getUser().getSecurityFilter()));
	}
	
	@Override
	protected void generateFile(File downloadFile, User user, String downloadName) throws Exception {
		Transaction transaction = null;
        try {
        	transaction = PersistenceManager.startTransaction();
        	
        	generateReport(user, downloadFile, downloadName, transaction);
	        
	        PersistenceManager.finishTransaction(transaction);
        } catch (Exception e) {
        	PersistenceManager.rollbackTransaction(transaction);
        	throw e;
        }
	}
	
	@Override
	protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink, Exception cause) throws MessagingException {
		// if the failure was caused by an empty report, we send a message.  Otherwise the failure is silent to the end user
		if (cause instanceof EmptyReportException) {
			mailManager.sendMessage(downloadLink.generateMailMessage("We're sorry, your report did not contain any printable events."));
		}
	}

	private void generateReport(User user, File downloadFile, String downloadName, Transaction transaction) throws IOException, UnsupportedEncodingException, NonPrintableEventType, ReportException {
		List<Event> events = loadEvents(user, transaction);
		reportGen.setType(reportType);
		
		reportGen.generate(events, new FileOutputStream(downloadFile), downloadName, transaction);
	}

	

	private List<Event> loadEvents(User user, Transaction transaction) {
		return new LazyLoadingList<Event>(eventIds, eventLoader, transaction);
	}

	public void setEventIds(List<Long> eventIds) {
    	this.eventIds = eventIds;
    }

	public void setReportType(EventReportType reportType) {
    	this.reportType = reportType;
	}

}
