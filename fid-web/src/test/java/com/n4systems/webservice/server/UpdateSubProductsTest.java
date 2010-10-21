package com.n4systems.webservice.server;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Asset;
import org.junit.Test;


import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.SubProduct;
import com.n4systems.model.builders.SubProductBuilder;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;


public class UpdateSubProductsTest {
	
	
	@Test
	public void should_not_call_product_manager_when_the_both_lists_of_sub_products_are_empty() throws Exception {
		LegacyProductSerial productManager = null;
		
		ProductManager productManager2 = createProductMangerMock();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), anAsset().build(), new InspectionServiceDTO(), new ArrayList<SubProduct>(), productManager2);
		
		sut.run();
		
	}


	private ProductManager createProductMangerMock() {
		Asset asset = anAsset().build();
		
		ProductManager productManager2 = createMock(ProductManager.class);
		expect(productManager2.findProduct(asset.getId(), null)).andReturn(asset);
		replay(productManager2);
		return productManager2;
	}

	
	@Test
	public void should_add_the_single_new_subproduct_to_an_empty_master_product() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterAsset);
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		ProductManager productManager2 = createProductMangerMock();
		
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(subProduct), masterAsset.getSubProducts());
	}
	
	@Test
	public void remove_existing_sub_product_from_master_product() throws Exception {

		//1. create masterAsset with two sub products in it
		Asset masterAsset = anAsset().build();
		
		SubProduct subProductToRemove = aSubProduct().build();
		subProductToRemove.getAsset().setId(1L);

		List<SubProduct> subProducts = new FluentArrayList<SubProduct>(subProductToRemove, aSubProduct().build());
		masterAsset.setSubProducts(subProducts);
		
		//2. creating InspectionServiceDTO with detaching sub products information
		SubProductMapServiceDTO subProductMapServiceDTO = new SubProductMapServiceDTO();
		subProductMapServiceDTO.setSubProductId(1L);
		
		InspectionServiceDTO inspectionServiceDTO = new InspectionServiceDTO();
		inspectionServiceDTO.getDetachSubProducts().add(subProductMapServiceDTO);
		
		UpdateSubProducts sut = new UpdateSubProducts(successfulProductManager(masterAsset), new Long(1L), masterAsset, inspectionServiceDTO, new FluentArrayList<SubProduct>(), lookUpProductById(subProductToRemove));
		
		sut.run();
		
		assertEquals(1, masterAsset.getSubProducts().size());
		
	}


	private ProductManager lookUpProductById(SubProduct subProductToRemove) {
		ProductManager productManager = createMock(ProductManager.class);
		expect(productManager.findProduct(same(subProductToRemove.getAsset().getId()), (SecurityFilter)anyObject())).andReturn(subProductToRemove.getAsset());
		replay(productManager);
		return productManager;
	}
	
	
	
	@Test
	public void should_not_add_subproduct_that_is_already_attached() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterAsset);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = aSubProduct().build();
		
		SubProduct alreadyAttachedSubProduct = aSubProduct().containingProduct(subProduct.getAsset()).withMasterProduct(masterAsset).build();
		masterAsset.getSubProducts().add(alreadyAttachedSubProduct);
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(alreadyAttachedSubProduct), masterAsset.getSubProducts());
	}
	
	
	
	@Test
	public void should_persist_changes_to_the_master_product_when_new_sub_products_are_added() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		verify(productManager);
	}


	@Test
	public void remove_existing_sub_product() throws Exception {
		Asset masterAsset = anAsset().build();
		
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterAsset, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		verify(productManager);
	}
	
	private LegacyProductSerial successfulProductManager(Asset masterAsset) throws SubProductUniquenessException {
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterAsset, masterAsset.getModifiedBy())).andReturn(masterAsset);
		replay(productManager);
		return productManager;
	}
	
	
}
