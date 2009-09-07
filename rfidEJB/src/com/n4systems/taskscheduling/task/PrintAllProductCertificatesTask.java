package com.n4systems.taskscheduling.task;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.MailManager;
import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.reporting.ReportFactory;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class PrintAllProductCertificatesTask implements Runnable {
	private static final Logger logger = Logger.getLogger( PrintAllProductCertificatesTask.class );
	
	private List<Long> productIdList = new ArrayList<Long>();
	private String dateFormat;
	private String packageName;
	private String downloadLocation;
	private Long userId;
	
	public void run() {
		
		try {
			// get the report factory and user manager
			ReportFactory reportFactory = ServiceLocator.getReportFactory();
			User userManager = ServiceLocator.getUser();
			MailManager mailManager = ServiceLocator.getMailManager();
			
			UserBean user = userManager.getUser(userId);
			
			logger.info("Generating report ... ");
			
			String subject = "Manufacturer Certificate Report for " + packageName;
			String body;
			
			try {
				// generate the report
				File report = reportFactory.generateProductCertificateReport(productIdList, packageName, user);
				
				logger.info("Generating report Complete [" + report + "]");
				
				body = "<h4>Your Report is ready</h4>";
				body += "<br />Please click the link below to download your report package.<br />";
				body += "If you are not logged in you will be prompted to do so.<br /><br />";
				body += "<a href='" + downloadLocation + "?downloadPath=" + URLEncoder.encode(report.getName(), "UTF-8") + "'>Click here to download your report</a>";
				
			} catch(EmptyReportException e) {
				body = "<h4>Your report contained no manufacturer certificates. </h4>";
			}

			logger.info("Sending notification email [" + user.getEmailAddress() + "]");
			mailManager.sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
			
		} catch (Exception e) {
			logger.error("Print all Product Certificates job failed", e);
		}
	}

	public List<Long> getProductIdList() {
		return productIdList;
	}

	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
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

}
