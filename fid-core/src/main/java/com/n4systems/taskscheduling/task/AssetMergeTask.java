package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.AssetManager;
import org.apache.log4j.Logger;


import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;

public class AssetMergeTask implements Runnable {
	private static final Logger logger = Logger.getLogger(AssetMergeTask.class);
	
	private final Asset winningAsset;
	private final Asset losingAsset;
	private final User user;
	
	private final String serialNumberOfLoser;
	private final String serialNumberOfWinner;
	
	private AssetManager assetManager;
	private boolean error = false;
	
	public AssetMergeTask(Asset winningProduct, Asset losingProduct, User user) {
		super();
		this.winningAsset = winningProduct;
		this.losingAsset = losingProduct;
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
		assetManager = ServiceLocator.getProductManager();
	}
	
	private void executeMerge() {
		try {
			assetManager.mergeAssets(winningAsset, losingAsset, user);
		} catch (Exception e) {
			logger.error("could not merge products", e);
			error = true;
		}
	}
	
	private void sendEmailResponse() {
		String subject = "Asset Merge Completed";
		String body = "<h2>Asset merge</h2>";
		if (error) {
			
			body += "<p>An error occured while merging the products " + serialNumberOfLoser + " and " + serialNumberOfWinner +
				 " please contact support and we will be able to help get this problem resolved.</p>";
		} else {
			body += "<p>All inspections from asset " + serialNumberOfLoser + " have been moved to asset " + serialNumberOfWinner + "</p>";
		}
		
		logger.info("Sending email [" + user.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Multi-Inspection certificate report email", e);
        }

	}

}
