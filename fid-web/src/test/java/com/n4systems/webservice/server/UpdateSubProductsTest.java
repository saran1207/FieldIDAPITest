package com.n4systems.webservice.server;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.SubAssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.builders.SubAssetBuilder;
import org.junit.Test;


import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;


public class UpdateSubProductsTest {
	
	
	@Test
	public void should_not_call_product_manager_when_the_both_lists_of_sub_products_are_empty() throws Exception {
		LegacyAsset productManager = null;
		
		AssetManager assetManager2 = createProductMangerMock();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), anAsset().build(), new InspectionServiceDTO(), new ArrayList<SubAsset>(), assetManager2);
		
		sut.run();
		
	}


	private AssetManager createProductMangerMock() {
		Asset asset = anAsset().build();
		
		AssetManager assetManager2 = createMock(AssetManager.class);
		expect(assetManager2.findAsset(asset.getId(), null)).andReturn(asset);
		replay(assetManager2);
		return assetManager2;
	}

	
	@Test
	public void should_add_the_single_new_subproduct_to_an_empty_master_product() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyAsset productManager = successfulProductManager(masterAsset);
		
		SubAsset subAsset = SubAssetBuilder.aSubAsset().build();
		
		AssetManager assetManager2 = createProductMangerMock();
		
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubAsset>(subAsset), assetManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubAsset>(subAsset), masterAsset.getSubAssets());
	}
	
	@Test
	public void remove_existing_sub_product_from_master_product() throws Exception {

		//1. create masterAsset with two sub products in it
		Asset masterAsset = anAsset().build();
		
		SubAsset subAssetToRemove = aSubAsset().build();
		subAssetToRemove.getAsset().setId(1L);

		List<SubAsset> subAssets = new FluentArrayList<SubAsset>(subAssetToRemove, aSubAsset().build());
		masterAsset.setSubAssets(subAssets);
		
		//2. creating InspectionServiceDTO with detaching sub products information
		SubProductMapServiceDTO subProductMapServiceDTO = new SubProductMapServiceDTO();
		subProductMapServiceDTO.setSubProductId(1L);
		
		InspectionServiceDTO inspectionServiceDTO = new InspectionServiceDTO();
		inspectionServiceDTO.getDetachSubProducts().add(subProductMapServiceDTO);
		
		UpdateSubProducts sut = new UpdateSubProducts(successfulProductManager(masterAsset), new Long(1L), masterAsset, inspectionServiceDTO, new FluentArrayList<SubAsset>(), lookUpProductById(subAssetToRemove));
		
		sut.run();
		
		assertEquals(1, masterAsset.getSubAssets().size());
		
	}


	private AssetManager lookUpProductById(SubAsset subAssetToRemove) {
		AssetManager assetManager = createMock(AssetManager.class);
		expect(assetManager.findAsset(same(subAssetToRemove.getAsset().getId()), (SecurityFilter)anyObject())).andReturn(subAssetToRemove.getAsset());
		replay(assetManager);
		return assetManager;
	}
	
	
	
	@Test
	public void should_not_add_subproduct_that_is_already_attached() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyAsset productManager = successfulProductManager(masterAsset);
		
		AssetManager assetManager2 = createProductMangerMock();
		
		SubAsset subAsset = aSubAsset().build();
		
		SubAsset alreadyAttachedSubAsset = aSubAsset().containingAsset(subAsset.getAsset()).withMasterAsset(masterAsset).build();
		masterAsset.getSubAssets().add(alreadyAttachedSubAsset);
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubAsset>(subAsset), assetManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubAsset>(alreadyAttachedSubAsset), masterAsset.getSubAssets());
	}
	
	
	
	@Test
	public void should_persist_changes_to_the_master_product_when_new_sub_products_are_added() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyAsset productManager = createMock(LegacyAsset.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		
		AssetManager assetManager2 = createProductMangerMock();
		
		SubAsset subAsset = SubAssetBuilder.aSubAsset().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubAsset>(subAsset), assetManager2);
		
		sut.run();
		
		verify(productManager);
	}


	@Test
	public void remove_existing_sub_product() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyAsset productManager = createMock(LegacyAsset.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		
		AssetManager assetManager2 = createProductMangerMock();
		
		SubAsset subAsset = SubAssetBuilder.aSubAsset().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubAsset>(subAsset), assetManager2);
		
		sut.run();
		
		verify(productManager);
	}
	
	private LegacyAsset successfulProductManager(Asset masterAsset) throws SubAssetUniquenessException {
		LegacyAsset productManager = createMock(LegacyAsset.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		return productManager;
	}
	
	
}
