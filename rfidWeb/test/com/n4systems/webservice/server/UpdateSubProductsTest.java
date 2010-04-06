package com.n4systems.webservice.server;

import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Product;
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
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), aProduct().build(), new InspectionServiceDTO(), new ArrayList<SubProduct>(), productManager2);
		
		sut.run();
		
	}


	private ProductManager createProductMangerMock() {
		Product product = aProduct().build();
		
		ProductManager productManager2 = createMock(ProductManager.class);
		expect(productManager2.findProduct(product.getId(), null)).andReturn(product);
		replay(productManager2);
		return productManager2;
	}

	
	@Test
	public void should_add_the_single_new_subproduct_to_an_empty_master_product() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterProduct);
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		ProductManager productManager2 = createProductMangerMock();
		
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(subProduct), masterProduct.getSubProducts());
	}
	
	@Test
	public void remove_existing_sub_product_from_master_product() throws Exception {

		//1. create masterProduct with two sub products in it
		Product masterProduct = aProduct().build();
		
		SubProduct subProductToRemove = aSubProduct().build();
		subProductToRemove.getProduct().setId(1L);

		List<SubProduct> subProducts = new FluentArrayList<SubProduct>(subProductToRemove, aSubProduct().build());
		masterProduct.setSubProducts(subProducts);
		
		//2. creating InspectionServiceDTO with detaching sub products information
		SubProductMapServiceDTO subProductMapServiceDTO = new SubProductMapServiceDTO();
		subProductMapServiceDTO.setSubProductId(1L);
		
		InspectionServiceDTO inspectionServiceDTO = new InspectionServiceDTO();
		inspectionServiceDTO.getDetachSubProducts().add(subProductMapServiceDTO);
		
		UpdateSubProducts sut = new UpdateSubProducts(successfulProductManager(masterProduct), new Long(1L), masterProduct, inspectionServiceDTO, new FluentArrayList<SubProduct>(), lookUpProductById(subProductToRemove));
		
		sut.run();
		
		assertEquals(1, masterProduct.getSubProducts().size());
		
	}


	private ProductManager lookUpProductById(SubProduct subProductToRemove) {
		ProductManager productManager = createMock(ProductManager.class);
		expect(productManager.findProduct(same(subProductToRemove.getProduct().getId()), (SecurityFilter)anyObject())).andReturn(subProductToRemove.getProduct());
		replay(productManager);
		return productManager;
	}
	
	
	
	@Test
	public void should_not_add_subproduct_that_is_already_attached() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterProduct);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = aSubProduct().build();
		
		SubProduct alreadyAttachedSubProduct = aSubProduct().containingProduct(subProduct.getProduct()).withMasterProduct(masterProduct).build();
		masterProduct.getSubProducts().add(alreadyAttachedSubProduct);
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(alreadyAttachedSubProduct), masterProduct.getSubProducts());
	}
	
	
	
	@Test
	public void should_persist_changes_to_the_master_product_when_new_sub_products_are_added() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterProduct, masterProduct.getModifiedBy())).andReturn(masterProduct);
		replay(productManager);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		verify(productManager);
	}


	@Test
	public void remove_existing_sub_product() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterProduct, masterProduct.getModifiedBy())).andReturn(masterProduct);
		replay(productManager);
		
		ProductManager productManager2 = createProductMangerMock();
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct), productManager2);
		
		sut.run();
		
		verify(productManager);
	}
	
	private LegacyProductSerial successfulProductManager(Product masterProduct) throws SubProductUniquenessException {
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterProduct, masterProduct.getModifiedBy())).andReturn(masterProduct);
		replay(productManager);
		return productManager;
	}
	
	
}
