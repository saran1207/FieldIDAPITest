package com.n4systems.webservice.server;

import java.util.ArrayList;
import java.util.List;


import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.SubProduct;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;

public class UpdateSubProducts {

	private final LegacyProductSerial legacyProductSerial;
	private final Long tenantId;
	private final Asset asset;
	private final InspectionServiceDTO inspectionServiceDTO;
	private final List<SubProduct> subProducts;
	private final ProductManager productManager;

	public UpdateSubProducts(LegacyProductSerial legacyProductSerial, Long tenantId,
			Asset asset, InspectionServiceDTO inspectionServiceDTO,
			List<SubProduct> subProducts,
			ProductManager productManager) {
				this.legacyProductSerial = legacyProductSerial;
				this.tenantId = tenantId;
				this.asset = asset;
				this.inspectionServiceDTO = inspectionServiceDTO;
				this.subProducts = subProducts;
				this.productManager = productManager;
	
	}
	
	public void run() throws SubProductUniquenessException {
		if (subProducts.size() > 0) {
			/*
			 * Note: the list of SubProducts on Asset is marked as @Transient however productManager.update
			 * has special handling code to persist it anyway.  and yes it does suck ...  
			 */
			asset.getSubProducts().addAll(subProductNotAlreadyAdded(asset, subProducts));
		}
		if (inspectionServiceDTO.getDetachSubProducts().size() > 0)
			asset.getSubProducts().removeAll(detachExistingSubProduct(tenantId, inspectionServiceDTO.getDetachSubProducts(), asset) );
		
		if (subProducts.size() > 0 ||
			inspectionServiceDTO.getDetachSubProducts().size() > 0) {
			legacyProductSerial.update(asset, asset.getModifiedBy());
		}
	}

	
	private List<SubProduct> subProductNotAlreadyAdded(Asset masterAsset, List<SubProduct> subProducts) {
		List<SubProduct> notAddedSubProducts = new ArrayList<SubProduct>();
		
		for (SubProduct subProduct : subProducts) {
			if (!masterAsset.getSubProducts().contains(subProduct)) {
				notAddedSubProducts.add(subProduct);
			}
		}
		
		return notAddedSubProducts;
	}
	
	private List<SubProduct> detachExistingSubProduct(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Asset masterAsset) {
		
		List<SubProduct> detachingSubProducts = new ArrayList<SubProduct>();
		
		for (SubProductMapServiceDTO subProductDTO : subProductMaps) {
			Asset asset = productManager.findProduct(subProductDTO.getSubProductId(), new TenantOnlySecurityFilter( tenantId ) );
			
			// Try by mobileGuid in case the asset is created locally in the mobile
			if (asset == null) {
				asset = productManager.findProductByGUID(subProductDTO.getNewProduct().getMobileGuid(), new TenantOnlySecurityFilter( tenantId ) );
			}				
			
			SubProduct subProduct = new SubProduct();
			subProduct.setAsset(asset);
			subProduct.setMasterProduct(masterAsset);
			
			detachingSubProducts.add(subProduct);
		}
		
		return detachingSubProducts;
		
	}
}
