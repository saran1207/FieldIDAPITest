package com.n4systems.fieldid.actions.product;

import java.util.Collection;

import org.apache.log4j.Logger;

import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.SerialNumberCounter;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Product;
import com.n4systems.util.SecurityFilter;

public class ProductUtilAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( ProductUtilAction.class );
	
	private SerialNumberCounter serialNumberCounter;
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;
	
	private String serialNumber;
	
	private String rfidString;
	private Long uniqueId;
	
	private Collection<Product> products;
	
	public ProductUtilAction(SerialNumberCounter serialNumberCounter, LegacyProductSerial productSerialManager, ProductManager productManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.serialNumberCounter = serialNumberCounter;
		this.productSerialManager = productSerialManager;
		this.productManager = productManager;
	}


	public String doCheckDuplicateRfid( ) {
		try {
			if( productSerialManager.rfidExists( rfidString, getTenantId(), uniqueId ) ) {
				return "duplicate";
			}
			return SUCCESS;
		} catch( Exception e) {
			addActionError( getText( "error.lookinguprfid" ) );
			logger.error( "looking up rfid number " + rfidString, e );
			return ERROR;
		}
	}
	
	
	public String doCheckSerialNumber() {
		if (productSerialManager.duplicateSerialNumber(serialNumber, uniqueId, getTenant())) {
			return "used";
		} else {
			return "available";
		}
	}
	
	
	public String doGenerateSerialNumber() {
		try {
			serialNumber = serialNumberCounter.generateSerialNumber(getPrimaryOrg());
		} catch (Exception e) {
			logger.error("Generating serial number", e);
			return ERROR;
		}
		return SUCCESS;
	}


	public String getSerialNumber() {
		return serialNumber;
	}


	public String getRfidString() {
		return rfidString;
	}


	public void setRfidString( String rfidString ) {
		this.rfidString = rfidString;
	}


	public Long getUniqueId() {
		return uniqueId;
	}


	public void setUniqueId( Long uniqueId ) {
		this.uniqueId = uniqueId;
	}


	public Collection<Product> getProducts() {
		if( products == null ) {
			products =  productManager.findProductsByRfidNumber( rfidString, new SecurityFilter( getTenantId() ), "infoOptions", "type.name" );
		}
		return products;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
