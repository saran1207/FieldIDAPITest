package com.n4systems.fieldid.actions.asset;

import java.util.Collection;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.IdentifierCounter;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.security.Permissions;


@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class AssetUtilAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger( AssetUtilAction.class );
	
	private IdentifierCounter identifierCounter;
	private LegacyAsset legacyAssetManager;
	private AssetManager assetManager;
	
	private String identifier;
	
	private String rfidString;
	private Long uniqueId;
	
	private Collection<Asset> assets;

    private AssetType assetType;
    private Long assetTypeId;
	
	public AssetUtilAction(IdentifierCounter identifierCounter, LegacyAsset legacyAssetManager, AssetManager assetManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.identifierCounter = identifierCounter;
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
	
	
	public String doCheckIdentifier() {
		if (legacyAssetManager.duplicateIdentifier(identifier, uniqueId, getTenant())) {
			return "used";
		} else {
			return "available";
		}
	}
	
	
	public String doGenerateIdentifier() {
		try {
			identifier = identifierCounter.generateIdentifier(getPrimaryOrg(), assetType);
		} catch (Exception e) {
			logger.error("Generating identifier", e);
			return ERROR;
		}
		return SUCCESS;
	}


	public String getIdentifier() {
		return identifier;
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


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
        if (assetTypeId != null) {
            this.assetType = persistenceManager.find(AssetType.class, assetTypeId, getTenantId());
        }
    }

}
