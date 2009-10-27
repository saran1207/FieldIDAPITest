package com.n4systems.taskscheduling.task;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.model.Product;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.reporting.ProductCertificateReportGenerator;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class PrintAllProductCertificatesTask implements Runnable {
	private static final Logger logger = Logger.getLogger( PrintAllProductCertificatesTask.class );
	
	private List<Long> productIdList = new ArrayList<Long>();
	private String dateFormat;
	private String packageName;
	private String downloadLocation;
	private UserBean user;
	
	private final ProductCertificateReportGenerator certGen;
	private final FilteredIdLoader<Product> productLoader;
	
	PrintAllProductCertificatesTask(ProductCertificateReportGenerator certGen, FilteredIdLoader<Product> productLoader) {
		this.certGen = certGen;
		this.productLoader = productLoader;
	}
	
	public PrintAllProductCertificatesTask(FilteredIdLoader<Product> productLoader) {
		this(new ProductCertificateReportGenerator(), productLoader);
	}
	
	public void run() {
		logger.debug(String.format("Generating manufacturer certificate report [%s]", packageName));
		
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
			
			
			String body;
			try {
				List<Product> products = loadProducts(user, transaction);
				
				File report = certGen.generate(products, packageName, user);
				
				logger.debug(String.format("Generating report Complete [%s]", packageName));
				
				body = "<h4>Your Report is ready</h4>";
				body += "<br />Please click the link below to download your report package.<br />";
				body += "If you are not logged in you will be prompted to do so.<br /><br />";
				body += "<a href='" + downloadLocation + "?downloadPath=" + URLEncoder.encode(report.getName(), "UTF-8") + "'>Click here to download your report</a>";
				
			} catch(EmptyReportException e) {
				body = "<h4>Your report contained no manufacturer certificates. </h4>";
			}

			sendMessage(user, body);
			
			PersistenceManager.finishTransaction(transaction);
		} catch (Exception e) {
			logger.error("Print all Product Certificates job failed", e);
			PersistenceManager.rollbackTransaction(transaction);
		}
	}
	
	private void sendMessage(UserBean user, String body) {
		logger.debug(String.format("Sending manufacturer certificate report email [%s]", user.getEmailAddress()));
		
		String subject = "Manufacturer Certificate Report for " + packageName;

		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Multi-Inspection certificate report email", e);
        }
	}
	
	private List<Product> loadProducts(UserBean user, Transaction transaction) {
		List<Product> products = new ArrayList<Product>();
		
		Product product;
		for (Long productId: productIdList) {
			product = productLoader.setId(productId).load(transaction);
			products.add(product);
		}
		
		return products;
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

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

}
