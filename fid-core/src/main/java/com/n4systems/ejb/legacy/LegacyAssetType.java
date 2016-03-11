/**
 * Copyright N4Systems Inc. 2006
 */
package com.n4systems.ejb.legacy;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.util.ListingPair;
import rfid.ejb.entity.InfoFieldBean;

import java.io.File;
import java.util.Collection;
import java.util.List;

public interface LegacyAssetType {
	 
	
	public AssetType findDefaultAssetType(Long tenantId);
	
	public AssetType updateAssetType(AssetType assetType, List<FileAttachment> uploadedFiles, File assetImage ) throws FileAttachmentException, ImageAttachmentException ;
	
	
	
	public AssetType updateAssetType(AssetType assetType) throws FileAttachmentException, ImageAttachmentException ;
	
	
	public List<AssetType> getAssetTypesForTenant(Long tenantId);
	
	public List<ListingPair> getAssetTypeListForTenant(Long tenantId);
	
	public Collection<Long> infoFieldsInUse( Collection<InfoFieldBean> infoFields ) ;
	public boolean cleanInfoOptions( int pageNumber, int pageSize );
	
	public AssetType findAssetTypeForAsset(Long assetId) throws InvalidQueryException;
}
