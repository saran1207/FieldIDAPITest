package com.n4systems.webservice.server;

import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import rfid.ejb.session.LegacyProductSerial;

import com.lowagie.text.pdf.PdfSigGenericPKCS.VeriSign;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.builders.SubProductBuilder;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.webservice.dto.InspectionServiceDTO;


public class UpdateSubProductsTest {
	
	
	@Test
	public void should_not_call_product_manager_when_the_both_lists_of_sub_products_are_empty() throws Exception {
		LegacyProductSerial productManager = null;
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), aProduct().build(), new InspectionServiceDTO(), new ArrayList<SubProduct>());
		
		sut.run();
		
	}

	
	@Test
	public void should_add_the_single_new_subproduct_to_an_empty_master_product() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterProduct);
		
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct));
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(subProduct), masterProduct.getSubProducts());
	}
	
	
	@Test
	public void should_not_add_subproduct_that_is_already_attached() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = successfulProductManager(masterProduct);
		
		
		SubProduct subProduct = aSubProduct().build();
		
		SubProduct alreadyAttachedSubProduct = aSubProduct().containingProduct(subProduct.getProduct()).withMasterProduct(masterProduct).build();
		masterProduct.getSubProducts().add(alreadyAttachedSubProduct);
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct));
		
		sut.run();
		
		assertEquals(new FluentArrayList<SubProduct>(alreadyAttachedSubProduct), masterProduct.getSubProducts());
	}
	
	
	
	@Test
	public void should_persist_changes_to_the_master_product_when_new_sub_products_are_added() throws Exception {
		Product masterProduct = aProduct().build();
		
		LegacyProductSerial productManager = createMock(LegacyProductSerial.class);
		expect(productManager.update(masterProduct, masterProduct.getModifiedBy())).andReturn(masterProduct);
		replay(productManager);
		
		
		SubProduct subProduct = SubProductBuilder.aSubProduct().build();
		
		UpdateSubProducts sut = new UpdateSubProducts(productManager, new Long(1L), masterProduct, new InspectionServiceDTO(), new FluentArrayList<SubProduct>(subProduct));
		
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
