package com.n4systems.taskscheduling.task;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.model.TenantOrganization;
import com.n4systems.reporting.ReportDefiner;
import com.n4systems.util.FileHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

import rfid.ejb.entity.UserBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;

public class PrintInspectionSummaryReportTask implements Runnable {
	private static Logger logger = Logger.getLogger(PrintInspectionSummaryReportTask.class);
	private static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd_h_mm_a";
	private static final String pdfExt = ".pdf";
	
	private ReportDefiner reportDefiner;
	private String dateFormat;
	private String downloadLocation;
	private Long userId;
	private TenantOrganization tenant; 
	
	public PrintInspectionSummaryReportTask() {}
	
	public void run() {
		UserBean user = ServiceLocator.getUser().getUser(userId);
		
		logger.info("Generating Inspection Summary Report for user [" + user.toString() + "]");
		
		String reportFileName = createReportFileName();
		
		String body = generateReport(user, reportFileName);

		sendMessage(user, reportFileName, body);
	}

	private String createReportFileName() {
	    String dateString = new SimpleDateFormat(FILE_NAME_DATE_FORMAT).format(new java.util.Date());		
		String reportFileName = "inspection-report-" + tenant.getName() + "_" + dateString;
	    
		return reportFileName;
    }

	private void sendMessage(UserBean user, String reportFileName, String body) {
		String subject = "Inspection Report Print Out for " + reportFileName;
		
		logger.info("Sending Inspection Summary Report notification email [" + user.getEmailAddress() + "]");
	    try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Inspection Summary Report notification email", e);
        }
    }

	private String generateReport(UserBean user, String reportFileName) {
	    String body;
		
		try {
			// generate the report
			JasperPrint p = null;
			
			File reportOutputDir = FileHelper.getTempDir(ServiceLocator.getDownloadManager().getBaseAccessDir(user));
			File reportFile = new File( reportOutputDir, reportFileName +  pdfExt);
			
			String reportPath = ServiceLocator.getDownloadManager().resolveRelativePath(reportFile);
			
			p = ServiceLocator.getReportFactory().generateInspectionReport( reportDefiner, user, tenant );
			OutputStream pdf = new FileOutputStream( reportFile );
			logger.info("Generating inspection summary report Complete [" + reportPath + "]");
			
			ServiceLocator.getReportFactory().printToPDF( p, pdf );
			logger.info("Printing pdf Complete[" + reportPath + "]");
			
			body = "<h4>Your Inspection Report print out is ready.</h4>";
			body += "<br />Please click the link below to download your report.<br />";
			body += "If you are not logged in you will be prompted to do so.<br /><br />";
			body += "<a href='" + downloadLocation + "?downloadPath=" + URLEncoder.encode(reportPath, "UTF-8") + "'>Click here to download your report</a>";
			
		} catch( EmptyReportException e) {
			logger.warn( "inspection report print empty report" );
			body = "<h4>Your Inspection Report print out was not created because it contained no inspections.</h4>";
		} catch( Exception e ) {
			logger.error( "inspection report print failed.", e );
			body = "<h4>Your Inspection Report print out has failed to be generated.</h4>";
		}
	    return body;
    }

	public ReportDefiner getReportDefiner() {
    	return reportDefiner;
    }

	public void setReportDefiner(ReportDefiner reportDefiner) {
    	this.reportDefiner = reportDefiner;
    }

	public String getDateFormat() {
    	return dateFormat;
    }

	public void setDateFormat(String dateFormat) {
    	this.dateFormat = dateFormat;
    }

	public String getDownloadLocation() {
    	return downloadLocation;
    }

	public void setDownloadLocation(String downloadLocation) {
    	this.downloadLocation = downloadLocation;
    }

	public Long getUserId() {
    	return userId;
    }

	public void setUserId(Long userId) {
    	this.userId = userId;
    }

	public TenantOrganization getTenant() {
    	return tenant;
    }

	public void setTenant(TenantOrganization tenant) {
    	this.tenant = tenant;
    }

}
