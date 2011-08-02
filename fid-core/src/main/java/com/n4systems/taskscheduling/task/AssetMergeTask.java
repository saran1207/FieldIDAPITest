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
	
	private final String identifierOfLoser;
	private final String identifierOfWinner;
	
	private AssetManager assetManager;
	private boolean error = false;
	
	public AssetMergeTask(Asset winningProduct, Asset losingProduct, User user) {
		super();
		this.winningAsset = winningProduct;
		this.losingAsset = losingProduct;
		this.user = user;
		
		identifierOfLoser = losingProduct.getIdentifier();
		identifierOfWinner = winningProduct.getIdentifier();
	}


	public void run() {
		setUp();
		executeMerge();
		sendEmailResponse();
	}
	
	private void setUp() {
		assetManager = ServiceLocator.getAssetManager();
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
			
			body += "<p>An error occured while merging the products " + identifierOfLoser + " and " + identifierOfWinner +
				 " please contact support and we will be able to help get this problem resolved.</p>";
		} else {
			body += "<p>All events from asset " + identifierOfLoser + " have been moved to asset " + identifierOfWinner + "</p>";
		}
		
		logger.info("Sending email [" + user.getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, user.getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Multi-Event certificate report email", e);
        }

	}

}
