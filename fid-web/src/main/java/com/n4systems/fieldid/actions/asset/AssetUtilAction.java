package com.n4systems.fieldid.actions.asset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.IdentifierCounter;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
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
    private String elementId;
	
	private Long uniqueId;
	
    private AssetType assetType;
    private Long assetTypeId;
    
    private String [] rfids = {};
    private List<String> duplicateRfids = new ArrayList<String>();
    
	public AssetUtilAction(IdentifierCounter identifierCounter, LegacyAsset legacyAssetManager, AssetManager assetManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.identifierCounter = identifierCounter;
		this.legacyAssetManager = legacyAssetManager;
		this.assetManager = assetManager;
	}

	public String doCheckDuplicateRfid( ) {
		try {
            for (String rfid : rfids) {
                if( legacyAssetManager.rfidExists( rfid, getTenantId(), uniqueId) ) {
                    duplicateRfids.add(rfid);
                }
            }
			return duplicateRfids.size() > 0 ? "duplicate" : SUCCESS;
		} catch( Exception e) {
			addActionError( getText( "error.lookinguprfid" ) );
			logger.error( "looking up rfid numbers " + rfids, e );
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
            addActionErrorText("message.error_generating_identifier");
			return ERROR;
		}
		return SUCCESS;
	}


	public String getIdentifier() {
		return identifier;
	}

	public Long getUniqueId() {
		return uniqueId;
	}


	public void setUniqueId( Long uniqueId ) {
		this.uniqueId = uniqueId;
	}


	public Collection<Asset> getAssets() {
		Set<Asset> assets = new HashSet<Asset>();

		for (String duplicateRfid: duplicateRfids) {
			List<Asset> dupAssets = assetManager.findAssetsByRfidNumber( duplicateRfid, new TenantOnlySecurityFilter( getTenantId() ), "infoOptions", "type.name" );
			assets.addAll(dupAssets);
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


	public String[] getRfids() {
		return rfids;
	}


	public void setRfids(String[] rfids) {
		this.rfids = rfids;
	}

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }
}
