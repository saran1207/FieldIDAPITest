package com.n4systems.fieldid.actions.asset;

import java.util.Collection;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class AssetUtilAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( AssetUtilAction.class );
	
	private SerialNumberCounter serialNumberCounter;
	private LegacyAsset legacyAssetManager;
	private AssetManager assetManager;
	
	private String serialNumber;
	
	private String rfidString;
	private Long uniqueId;
	
	private Collection<Asset> assets;
	
	public AssetUtilAction(SerialNumberCounter serialNumberCounter, LegacyAsset legacyAssetManager, AssetManager assetManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.serialNumberCounter = serialNumberCounter;
		this.legacyAssetManager = legacyAssetManager;
		this.assetManager = assetManager;
	}


	public String doCheckDuplicateRfid( ) {
		try {
			if( legacyAssetManager.rfidExists( rfidString, getTenantId(), uniqueId ) ) {
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
		if (legacyAssetManager.duplicateSerialNumber(serialNumber, uniqueId, getTenant())) {
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
			assets =  assetManager.findAssetsByRfidNumber( rfidString, new TenantOnlySecurityFilter( getTenantId() ), "infoOptions", "type.name" );
		}
		return assets;
	}


	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
