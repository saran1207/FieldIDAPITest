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
import com.n4systems.model.Tenant;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;


public class PrintAllInspectionCertificatesTask implements Runnable {
	private static Logger logger = Logger.getLogger(PrintAllInspectionCertificatesTask.class);
	private static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
	
	private Tenant tenant;
	private List<Long> inspectionDocs = new ArrayList<Long>(); 
	private String dateFormat;
	private Long userId;
	private String downloadLocation;
	private InspectionReportType reportType;
	
	public PrintAllInspectionCertificatesTask() {}
	
	public void run() {
		
		UserBean user = ServiceLocator.getUser().getUser(userId);
		
		logger.info("Generating Multi-Inspection certificate report for user [" + user.toString() + "]");
		
        try { 
        	String reportFileName = createReportFileName();
        	
        	String body = generateReport(user, reportFileName);
	        
	        sendMessage(user, reportFileName, body);
	            
        } catch (Exception e) {
        	logger.error("Unable to generate Multi-Inspection certificate report", e);
        }
	}
	
	private String createReportFileName() {
	    String dateString = new SimpleDateFormat(FILE_NAME_DATE_FORMAT).format(new java.util.Date());		
		String reportFileName = reportType.getReportNamePrefix() + "_report_" + tenant.getName() + "_" + dateString;
	    
		return reportFileName;
    }
	
	/** Generates the report and returns the body text of the message. */
	private String generateReport(UserBean user, String reportFileName) throws UnsupportedEncodingException, NonPrintableEventType, ReportException {
		StringBuilder body = new StringBuilder();
		
		try {
			// generate the report
			File report = ServiceLocator.getReportFactory().generateInspectionCertificateReport(reportType, new HashSet<Long>(inspectionDocs), reportFileName, user);
			
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
