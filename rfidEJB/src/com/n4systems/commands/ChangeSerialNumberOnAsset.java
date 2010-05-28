package com.n4systems.commands;

import org.apache.log4j.Logger;

import com.n4systems.model.Product;
import com.n4systems.model.assets.Asset;
import com.n4systems.model.user.User1;

public class ChangeSerialNumberOnAsset extends SystemMessage {
	private static final Logger logger = Logger.getLogger(ChangeSerialNumberOnAsset.class);
	
	private final Asset asset;
	private final String serialNumber;
	private final User1 user;
	
	
	public ChangeSerialNumberOnAsset(Asset asset, String serialNumber, User1 user) {
		super();
		this.asset = asset;
		this.serialNumber = serialNumber;
		this.user = user;
		
	}


	public void applyChange(Product product) {
		product.changeSerialNumberTo(serialNumber);
		logger.info("applied serial number [" + serialNumber + "] on asset [" + product.getId().toString() + "]");
	}
	
	
	
	
}
