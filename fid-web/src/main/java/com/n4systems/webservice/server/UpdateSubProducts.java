package com.n4systems.webservice.server;

import java.util.ArrayList;
import java.util.List;


import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;

public class UpdateSubProducts {

	private final LegacyProductSerial legacyProductSerial;
	private final Long tenantId;
	private final Asset asset;
	private final InspectionServiceDTO inspectionServiceDTO;
	private final List<SubAsset> subAssets;
	private final ProductManager productManager;

	public UpdateSubProducts(LegacyProductSerial legacyProductSerial, Long tenantId,
			Asset asset, InspectionServiceDTO inspectionServiceDTO,
			List<SubAsset> subAssets,
			ProductManager productManager) {
				this.legacyProductSerial = legacyProductSerial;
				this.tenantId = tenantId;
				this.asset = asset;
				this.inspectionServiceDTO = inspectionServiceDTO;
				this.subAssets = subAssets;
				this.productManager = productManager;
	
	}
	
	public void run() throws SubAssetUniquenessException {
		if (subAssets.size() > 0) {
			/*
			 * Note: the list of SubProducts on Asset is marked as @Transient however productManager.update
			 * has special handling code to persist it anyway.  and yes it does suck ...  
			 */
			asset.getSubAssets().addAll(subProductNotAlreadyAdded(asset, subAssets));
		}
		if (inspectionServiceDTO.getDetachSubProducts().size() > 0)
			asset.getSubAssets().removeAll(detachExistingSubProduct(tenantId, inspectionServiceDTO.getDetachSubProducts(), asset) );
		
		if (subAssets.size() > 0 ||
			inspectionServiceDTO.getDetachSubProducts().size() > 0) {
			legacyProductSerial.update(asset, asset.getModifiedBy());
		}
	}

	
	private List<SubAsset> subProductNotAlreadyAdded(Asset masterAsset, List<SubAsset> subAssets) {
		List<SubAsset> notAddedSubAssets = new ArrayList<SubAsset>();
		
		for (SubAsset subAsset : subAssets) {
			if (!masterAsset.getSubAssets().contains(subAsset)) {
				notAddedSubAssets.add(subAsset);
			}
		}
		
		return notAddedSubAssets;
	}
	
	private List<SubAsset> detachExistingSubProduct(Long tenantId, List<SubProductMapServiceDTO> subProductMaps, Asset masterAsset) {
		
		List<SubAsset> detachingSubAssets = new ArrayList<SubAsset>();
		
		for (SubProductMapServiceDTO subProductDTO : subProductMaps) {
			Asset asset = productManager.findAsset(subProductDTO.getSubProductId(), new TenantOnlySecurityFilter( tenantId ) );
			
			// Try by mobileGuid in case the asset is created locally in the mobile
			if (asset == null) {
				asset = productManager.findAssetByGUID(subProductDTO.getNewProduct().getMobileGuid(), new TenantOnlySecurityFilter( tenantId ) );
			}				
			
			SubAsset subAsset = new SubAsset();
			subAsset.setAsset(asset);
			subAsset.setMasterAsset(masterAsset);
			
			detachingSubAssets.add(subAsset);
		}
		
		return detachingSubAssets;
		
	}
}
