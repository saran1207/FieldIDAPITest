package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.exceptions.NonPrintableEventType;
import com.n4systems.exceptions.ReportException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.safetynetwork.SafetyNetworkInspectionLoader;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.reporting.InspectionCertificateReportGenerator;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;


public class PrintAllInspectionCertificatesTask implements Runnable {
	private static Logger logger = Logger.getLogger(PrintAllInspectionCertificatesTask.class);
	private static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
	
	private final InspectionCertificateReportGenerator reportGen;
	
	private Tenant tenant;
	private List<Long> inspectionDocs = new ArrayList<Long>(); 
	private String dateFormat;
	private Long userId;
	private String downloadLocation;
	private InspectionReportType reportType;
	
	public PrintAllInspectionCertificatesTask(InspectionCertificateReportGenerator reportGen) {
		this.reportGen = reportGen;
	}
	
	public PrintAllInspectionCertificatesTask() {
		this(new InspectionCertificateReportGenerator());
	}
	
	public void run() {
		UserBean user = ServiceLocator.getUser().getUser(userId);
		
		logger.info("Generating Multi-Inspection certificate report for user [" + user.toString() + "]");
		
		Transaction transaction = null;
        try {
        	transaction = PersistenceManager.startTransaction();
        	
        	String reportFileName = createReportFileName();
        	
        	String body = generateReport(user, reportFileName, transaction);
	        
	        sendMessage(user, reportFileName, body);
	        
	        PersistenceManager.finishTransaction(transaction);
        } catch (Exception e) {
        	logger.error("Unable to generate Multi-Inspection certificate report", e);
        	PersistenceManager.rollbackTransaction(transaction);
        }
	}
	
	private String createReportFileName() {
	    String dateString = new SimpleDateFormat(FILE_NAME_DATE_FORMAT).format(new java.util.Date());		
		String reportFileName = reportType.getReportNamePrefix() + "_report_" + tenant.getName() + "_" + dateString;
	    
		return reportFileName;
    }
	
	/** Generates the report and returns the body text of the message. */
	private String generateReport(UserBean user, String reportFileName, Transaction transaction) throws UnsupportedEncodingException, NonPrintableEventType, ReportException {
		StringBuilder body = new StringBuilder();
		
		try {
			// we need to make sure we don't have any duplicate inspection doc ids.  We'll do this by converting them to a set and 
			// then back to a List
			List<Long> inspectionIds = new ArrayList<Long>(new HashSet<Long>(inspectionDocs));
			
			File report = generateReport(user, reportFileName, inspectionIds, transaction);
			
			logger.info("Generating Multi-Inspection certificate Report Complete [" + report + "]");
			
			body.append("<h4>Your Report is ready</h4>");
			body.append("<br />Please click the link below to download your report package.<br />");
			body.append("If you are not logged in you will be prompted to do so.<br /><br />");
			body.append("<a href='" + downloadLocation + "?downloadPath=" + URLEncoder.encode(report.getName(), "UTF-8") + "'>Click here to download your report</a>");
			
		} catch(EmptyReportException e) {
			body.append("<h4>Your Report contained no printable inspections</h4>");
			
		}
		
		return body.toString();
	}

	private File generateReport(UserBean user, String reportFileName, List<Long> inspectionIds, Transaction transaction) throws ReportException, EmptyReportException {
		List<Inspection> inspections = loadInspections(user, inspectionIds, transaction);
		
		return reportGen.generate(reportType, inspections, reportFileName, user, transaction);
	}

	private List<Inspection> loadInspections(UserBean user, List<Long> inspectionIds, Transaction transaction) {
		SafetyNetworkInspectionLoader inspectionLoader = createSafetyNetworkInspectionLoader(user);
		
		List<Inspection> inspections = new ArrayList<Inspection>();

		Inspection inspection;
		for (Long inspectionId: inspectionIds) {
			inspection = inspectionLoader.setId(inspectionId).load(transaction);
			inspections.add(inspection);
		}
		
		return inspections;
	}
	
	/** Sends the download link email. */
	private void sendMessage(UserBean user, String reportFileName, String body) {
		String subject = reportType.getDisplayName() + " Report for " + reportFileName;

		logger.info("Sending Multi-Inspection certificate report email [" + user.getEmailAddress() + "]");
		
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Multi-Inspection certificate report email", e);
        }
	}

	protected SafetyNetworkInspectionLoader createSafetyNetworkInspectionLoader(UserBean user) {
		return new SafetyNetworkInspectionLoader(user.getSecurityFilter());
	}
	
	public Tenant getTenant() {
    	return tenant;
    }

	public void setTenant(Tenant tenant) {
    	this.tenant = tenant;
    }

	public List<Long> getInspectionDocs() {
    	return inspectionDocs;
    }

	public void setInspectionDocs(List<Long> inspectionDocs) {
    	this.inspectionDocs = inspectionDocs;
    }

	public String getDateFormat() {
    	return dateFormat;
    }

	public void setDateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    }

	public Long getUserId() {
    	return userId;
    }

	public void setUserId(Long userId) {
    	this.userId = userId;
    }

	public String getDownloadLocation() {
    	return downloadLocation;
    }

	public void setDownloadLocation(String downloadLocation) {
    	this.downloadLocation = downloadLocation;
    }

	public InspectionReportType getReportType() {
    	return reportType;
    }

	public void setReportType(InspectionReportType reportType) {
    	this.reportType = reportType;
	}
}
