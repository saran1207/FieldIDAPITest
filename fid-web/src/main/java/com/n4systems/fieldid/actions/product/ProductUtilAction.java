package com.n4systems.fieldid.actions.product;

import java.util.Collection;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class ProductUtilAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( ProductUtilAction.class );
	
	private SerialNumberCounter serialNumberCounter;
	private LegacyProductSerial productSerialManager;
	private ProductManager productManager;
	
	private String serialNumber;
	
	private String rfidString;
	private Long uniqueId;
	
	private Collection<Asset> assets;
	
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


	public Collection<Asset> getAssets() {
		if( assets == null ) {
			assets =  productManager.findProductsByRfidNumber( rfidString, new TenantOnlySecurityFilter( getTenantId() ), "infoOptions", "type.name" );
		}
		return assets;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
