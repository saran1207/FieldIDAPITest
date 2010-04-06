package com.n4systems.webservice.server;

import java.util.ArrayList;
import java.util.List;


import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;

public class UpdateSubProducts {

	private final LegacyProductSerial legacyProductSerial;
	private final Long tenantId;
	private final Product product;
	private final InspectionServiceDTO inspectionServiceDTO;
	private final List<SubProduct> subProducts;
	private final ProductManager productManager;

	public UpdateSubProducts(LegacyProductSerial legacyProductSerial, Long tenantId,
			Product product, InspectionServiceDTO inspectionServiceDTO,
			List<SubProduct> subProducts,
			ProductManager productManager) {
				this.legacyProductSerial = legacyProductSerial;
				this.tenantId = tenantId;
				this.product = product;
				this.inspectionServiceDTO = inspectionServiceDTO;
				this.subProducts = subProducts;
				this.productManager = productManager;
	
	}
	
	public void run() throws SubProductUniquenessException {
		if (subProducts.size() > 0) {
			/*
			 * Note: the list of SubProducts on Product is marked as @Transient however productManager.update 
			 * has special handling code to persist it anyway.  and yes it does suck ...  
			 */
			product.getSubProducts().addAll(subProductNotAlreadyAdded(product, subProducts));
		}
		if (inspectionServiceDTO.getDetachSubProducts().size() > 0)
			product.getSubProducts().removeAll(detachExistingSubProduct(tenantId, inspectionServiceDTO.getDetachSubProducts(), product) );
		
		if (subProducts.size() > 0 ||
			inspectionServiceDTO.getDetachSubProducts().size() > 0) {
			legacyProductSerial.update(product, product.getModifiedBy());
		}
	}

	
	private List<SubProduct> subProductNotAlreadyAdded(Product masterProduct, List<SubProduct> subProducts) {
		List<SubProduct> notAddedSubProducts = new ArrayList<SubProduct>();
		
		for (SubProduct subProduct : subProducts) {
			if (!masterProduct.getSubProducts().contains(subProduct)) {
				notAddedSubProducts.add(subProduct);
			}
		}
		
		return notAddedSubProducts;
	}
	
	private List<SubProduct> detachExistingSubProduct(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Product masterProduct) {
		
		List<SubProduct> detachingSubProducts = new ArrayList<SubProduct>();
		
		for (SubProductMapServiceDTO subProductDTO : subProductMaps) {
			Product product = productManager.findProduct(subProductDTO.getSubProductId(), new TenantOnlySecurityFilter( tenantId ) );
			
			// Try by mobileGuid in case the product is created locally in the mobile
			if (product == null) {
				product = productManager.findProductByGUID(subProductDTO.getNewProduct().getMobileGuid(), new TenantOnlySecurityFilter( tenantId ) );
			}				
			
			SubProduct subProduct = new SubProduct();
			subProduct.setProduct(product);
			subProduct.setMasterProduct(masterProduct);
			
			detachingSubProducts.add(subProduct);
		}
		
		return detachingSubProducts;
		
	}
}
