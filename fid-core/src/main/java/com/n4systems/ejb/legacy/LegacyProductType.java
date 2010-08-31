/**
 * Copyright N4Systems Inc. 2006
 */
package com.n4systems.ejb.legacy;

import java.io.File;
import java.util.Collection;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProductType;
import com.n4systems.util.ListingPair;

public interface LegacyProductType {
	 
	
	public ProductType findDefaultProductType(Long tenantId);
	
	public ProductType updateProductType(ProductType productTypeBean, List<FileAttachment> uploadedFiles, File productImage ) throws FileAttachmentException, ImageAttachmentException ;
	
	
	
	public ProductType updateProductType(ProductType productTypeBean) throws FileAttachmentException, ImageAttachmentException ;
	
	
	public List<ProductType> getProductTypesForTenant(Long tenantId);
	
	public List<ListingPair> getProductTypeListForTenant(Long tenantId);
	
	public Collection<Long> infoFieldsInUse( Collection<InfoFieldBean> infoFields ) ;
	public boolean cleanInfoOptions( int pageNumber, int pageSize );
	
	public ProductType findProductTypeForProduct(Long productId) throws InvalidQueryException;
}
