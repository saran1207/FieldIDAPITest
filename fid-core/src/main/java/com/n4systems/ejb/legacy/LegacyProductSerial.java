package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;

public interface LegacyProductSerial {
	
	public Long countAllInspections( Asset asset, SecurityFilter securityFilter );
	public Long countAllLocalInspections(Asset asset, SecurityFilter securityFilter);
	
	
	public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException;
	
	public Asset createWithHistory( Asset asset, User modifiedBy ) throws SubAssetUniquenessException;
	
	public boolean duplicateSerialNumber( String serialNumber, Long uniqueID, Tenant tenant );
	
	public Inspection findLastInspections( Asset asset, SecurityFilter securityFilter );
	
	
	
	public List<AssetStatus> findAssetStatus(Long tenantId, Date beginDate);
	public AssetStatus findAssetStatus(Long uniqueID, Long tenantId);
	
	
	/**
	 * @deprecated use AssetSerialExtensionListLoader
	 */
	public Collection<AssetSerialExtension> getAssetSerialExtensions(Long tenantId);
		
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser);
	
	
	public boolean rfidExists(String rfidNumber, Long tenantId);
	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID);
	
	/*####################################
	 * updating asset
	 #########################################################*/
	
	public Asset update(Asset assetSerial, User modifiedBy) throws SubAssetUniquenessException;
	
	
	public Asset createAssetWithServiceTransaction( String transactionGUID, Asset asset, User modifiedBy ) throws TransactionAlreadyProcessedException, SubAssetUniquenessException;
}
