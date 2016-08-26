package com.n4systems.ejb.legacy;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetExtension;

import java.util.Collection;

public interface LegacyAsset {
	
	public Long countAllEvents( Asset asset, SecurityFilter securityFilter );
	public Long countAllLocalEvents(Asset asset, SecurityFilter securityFilter);
	
	
	public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException;
	
	public Asset createWithHistory( Asset asset, User modifiedBy ) throws SubAssetUniquenessException;
	
	public boolean duplicateIdentifier(String identifier, Long uniqueID, Tenant tenant);
	
	public Event findLastEvents( Asset asset, SecurityFilter securityFilter );
	
	
	
	public AssetStatus findAssetStatus(Long uniqueID, Long tenantId);
	
	
	/**
	 * @deprecated use AssetSerialExtensionListLoader
	 */
	public Collection<AssetExtension> getAssetExtensions(Long tenantId);
		
	public AddAssetHistory getAddAssetHistory(Long rFieldidUser);
	
	
	public boolean rfidExists(String rfidNumber, Long tenantId);
	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID);
	
	/*####################################
	 * updating asset
	 #########################################################*/
	
	public Asset update(Asset asset, User modifiedBy) throws SubAssetUniquenessException;
	
	
	public Asset createAssetWithServiceTransaction( String transactionGUID, Asset asset, User modifiedBy ) throws TransactionAlreadyProcessedException, SubAssetUniquenessException;
}
