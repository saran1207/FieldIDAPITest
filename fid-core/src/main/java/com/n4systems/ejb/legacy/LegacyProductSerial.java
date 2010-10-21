package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;

public interface LegacyProductSerial {
	
	public Long countAllInspections( Asset product, SecurityFilter securityFilter );
	public Long countAllLocalInspections(Asset product, SecurityFilter securityFilter);
	
	
	public Asset create(Asset product, User modifiedBy) throws SubProductUniquenessException;
	
	public Asset createWithHistory( Asset product, User modifiedBy ) throws SubProductUniquenessException;
	
	public boolean duplicateSerialNumber( String serialNumber, Long uniqueID, Tenant tenant );
	
	public Inspection findLastInspections( Asset product, SecurityFilter securityFilter );
	
	
	
	public List<AssetStatus> findProductStatus(Long tenantId, Date beginDate);
	public AssetStatus findProductStatus(Long uniqueID, Long tenantId);
	
	
	/**
	 * @deprecated use ProductSerialExtensionListLoader
	 */
	public Collection<AssetSerialExtension> getProductSerialExtensions(Long tenantId);
		
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser);
	
	
	public boolean rfidExists(String rfidNumber, Long tenantId);
	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID);
	
	/*####################################
	 * updating asset
	 #########################################################*/
	
	public Asset update(Asset assetSerial, User modifiedBy) throws SubProductUniquenessException;
	
	
	public Asset createProductWithServiceTransaction( String transactionGUID, Asset product, User modifiedBy ) throws TransactionAlreadyProcessedException, SubProductUniquenessException;
}
