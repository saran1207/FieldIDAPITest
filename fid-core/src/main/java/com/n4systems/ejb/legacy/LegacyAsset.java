package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetExtension;

import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;

public interface LegacyAsset {
	
	public Long countAllEvents( Asset asset, SecurityFilter securityFilter );
	public Long countAllLocalEvents(Asset asset, SecurityFilter securityFilter);
	
	
	public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException;
	
	public Asset createWithHistory( Asset asset, User modifiedBy ) throws SubAssetUniquenessException;
	
	public boolean duplicateSerialNumber( String serialNumber, Long uniqueID, Tenant tenant );
	
	public Event findLastEvents( Asset asset, SecurityFilter securityFilter );
	
	
	
	public List<AssetStatus> findAssetStatus(Long tenantId, Date beginDate);
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
