/**
 * Copyright N4Systems Inc. 2006
 */
package com.n4systems.ejb.legacy;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.FileAttachment;
import com.n4systems.util.ListingPair;

public interface LegacyProductType {
	 
	
	public AssetType findDefaultProductType(Long tenantId);
	
	public AssetType updateProductType(AssetType assetType, List<FileAttachment> uploadedFiles, File productImage ) throws FileAttachmentException, ImageAttachmentException ;
	
	
	
	public AssetType updateProductType(AssetType assetType) throws FileAttachmentException, ImageAttachmentException ;
	
	
	public List<AssetType> getProductTypesForTenant(Long tenantId);
	
	public List<ListingPair> getProductTypeListForTenant(Long tenantId);
	
	public Collection<Long> infoFieldsInUse( Collection<InfoFieldBean> infoFields ) ;
	public boolean cleanInfoOptions( int pageNumber, int pageSize );
	
	public AssetType findProductTypeForProduct(Long productId) throws InvalidQueryException;
}
