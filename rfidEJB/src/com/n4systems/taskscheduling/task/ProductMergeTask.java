package com.n4systems.taskscheduling.task;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.ProductManager;
import com.n4systems.model.Product;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class ProductMergeTask implements Runnable {
	private static final Logger logger = Logger.getLogger(ProductMergeTask.class);
	
	private final Product winningProduct;
	private final Product losingProduct;
	private final UserBean user;
	
	private final String serialNumberOfLoser;
	private final String serialNumberOfWinner;
	
	private ProductManager productManager;
	private boolean error = false;
	
	public ProductMergeTask(Product winningProduct, Product losingProduct, UserBean user) {
		super();
		this.winningProduct = winningProduct;
		this.losingProduct = losingProduct;
		this.user = user;
		
		serialNumberOfLoser = losingProduct.getSerialNumber();
		serialNumberOfWinner = winningProduct.getSerialNumber();
	}


	public void run() {
		setUp();
		executeMerge();
		sendEmailResponse();
	}
	
	private void setUp() {
		productManager = ServiceLocator.getProductManager();
	}
	
	private void executeMerge() {
		try {
			productManager.mergeProducts(winningProduct, losingProduct, user);
		} catch (Exception e) {
			logger.error("could not merge products", e);
			error = true;
		}
	}
	
	private void sendEmailResponse() {
		String subject = "Product Merge Completed";
		String body = "<h2>Product merge</h2>";
		if (error) {
			
			body += "<p>An error occured while merging the products " + serialNumberOfLoser + " and " + serialNumberOfWinner +
				 " please contact support and we will be able to help get this problem resolved.</p>";
		} else {
			body += "<p>All inspections from product " + serialNumberOfLoser + " have been moved to product " + serialNumberOfWinner + "</p>";
		}
		
		logger.info("Sending email [" + user.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Multi-Inspection certificate report email", e);
        }

	}

}
