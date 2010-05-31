package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;

public interface LegacyProductSerial {
	
	public Long countAllInspections( Product product, SecurityFilter securityFilter );
	public Long countAllLocalInspections(Product product, SecurityFilter securityFilter);
	
	
	public Product create(Product product, User modifiedBy) throws SubProductUniquenessException;
	
	public Product createWithHistory( Product product, User modifiedBy ) throws SubProductUniquenessException;	
	
	public boolean duplicateSerialNumber( String serialNumber, Long uniqueID, Tenant tenant );
	
	public Inspection findLastInspections( Product product, SecurityFilter securityFilter );
	
	
	
	public List<ProductStatusBean> findProductStatus(Long tenantId, Date beginDate);
	public ProductStatusBean findProductStatus(Long uniqueID, Long tenantId);	
	
	
	/**
	 * @deprecated use ProductSerialExtensionListLoader
	 */
	public Collection<ProductSerialExtensionBean> getProductSerialExtensions(Long tenantId);
		
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser);
	
	/*########################################################
	 * product Status crud
	 #########################################################*/
	/**
	 * @deprecated use ProductStatusListLoader
	 */
	public List<ProductStatusBean> getAllProductStatus(Long tenantId);
	public Long createProductStatus(ProductStatusBean productStatus);
	public void removeProductStatus( ProductStatusBean obj );
	public Long updateProductStatus(ProductStatusBean productStatus);
	
	public boolean rfidExists(String rfidNumber, Long tenantId);
	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID);
	
	/*####################################
	 * updating product
	 #########################################################*/
	
	public Product update(Product productSerial, User modifiedBy) throws SubProductUniquenessException;
	
	
	public Product createProductWithServiceTransaction( String transactionGUID, Product product, User modifiedBy ) throws TransactionAlreadyProcessedException, SubProductUniquenessException;
}
