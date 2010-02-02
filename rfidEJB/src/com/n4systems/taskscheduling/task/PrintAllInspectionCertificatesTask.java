package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.safetynetwork.SafetyNetworkInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredProductInspectionLoader;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.InspectionCertificateReportGenerator;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.util.mail.MailMessage;


public class PrintAllInspectionCertificatesTask extends DownloadTask {
	private final InspectionCertificateReportGenerator reportGen;
	private final SafetyNetworkInspectionLoader inspectionLoader;
	
	private List<Long> inspectionIds;
	private InspectionReportType reportType;
	
	public PrintAllInspectionCertificatesTask(DownloadLink downloadLink, String downloadUrl, DateTimeDefiner dateDefiner, SafetyNetworkInspectionLoader inspectionLoader) {
		super(downloadLink, downloadUrl, "printAllInspectionCerts");
		this.reportGen = new InspectionCertificateReportGenerator(dateDefiner);
		this.inspectionLoader = inspectionLoader;
	}
	
	public PrintAllInspectionCertificatesTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new DateTimeDefiner(downloadLink.getUser()), new SafetyNetworkRegisteredProductInspectionLoader(downloadLink.getUser().getSecurityFilter()));
	}
	
	@Override
	protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
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
			mailManager.sendMessage(new MailMessage(downloadLink, "We're sorry, your report did not contain any printable inspections."));
		}
	}

	private void generateReport(UserBean user, File downloadFile, String downloadName, Transaction transaction) throws UnsupportedEncodingException, NonPrintableEventType, ReportException {
		List<Inspection> inspections = loadInspections(user, transaction);
			
		reportGen.generate(reportType, inspections, downloadFile, user, downloadName, transaction);
	}

	private List<Inspection> loadInspections(UserBean user, Transaction transaction) {
		List<Inspection> inspections = new ArrayList<Inspection>();

		Inspection inspection;
		for (Long inspectionId: inspectionIds) {
			inspection = inspectionLoader.setId(inspectionId).load(transaction);
			inspections.add(inspection);
		}
		
		return inspections;
	}

	public void setInspectionIds(List<Long> inspectionIds) {
    	this.inspectionIds = inspectionIds;
    }

	public void setReportType(InspectionReportType reportType) {
    	this.reportType = reportType;
	}

}
