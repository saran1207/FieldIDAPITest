package com.n4systems.api.product;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.product.ProductToViewConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Product;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.test.helpers.Asserts;

public class ProductToViewConverterTest {
	private ProductToViewConverter converter = new ProductToViewConverter();
	
	private Product createProduct() {
		Product model = ProductBuilder.aProduct().ofType(ProductTypeBuilder.aProductType().build()).withOwner(OrgBuilder.aCustomerOrg().build()).withSerialNumber("12345").build();
		model.setRfidNumber("rf1234");
		model.setCustomerRefNumber("cr12345");
		model.setLocation("loc123");
		model.setPurchaseOrder("PO8282");
		model.setComments("comments 12312");
		model.setIdentified(new Date());
		
		ProductStatusBean ps = new ProductStatusBean();
		ps.setName("In Service");
		model.setProductStatus(ps);
		
		Order order = new Order();
		order.setOrderNumber("ON12312312");
		LineItem line = new LineItem();
		line.setOrder(order);
		model.setShopOrder(line);
		
		InfoOptionBeanBuilder optionBuilder = InfoOptionBeanBuilder.aDynamicInfoOption();
		InfoFieldBeanBuilder fieldBuilder = InfoFieldBeanBuilder.aTextField();
		
		model.getInfoOptions().add(optionBuilder.withName("Opt1").forField(fieldBuilder.withName("Field1").build()).build());
		model.getInfoOptions().add(optionBuilder.withName("Opt2").forField(fieldBuilder.withName("Field2").build()).build());
		
		return model;
	}
	
	@Test
	public void test_to_view_copies_all_properties() throws ConversionException {
		Product model = createProduct();
		
		ProductView view = converter.toView(model);
		
		Asserts.assertMethodReturnValuesEqual(model, view, "getSerialNumber", "getRfidNumber", "getCustomerRefNumber", "getLocation", "getPurchaseOrder", "getComments", "getIdentified");
		
		assertEquals(model.getProductStatus().getName(), view.getProductStatus());
		assertEquals(model.getShopOrder().getOrder().getOrderNumber(), view.getShopOrder());
		assertEquals(model.getInfoOptions().size(), view.getAttributes().size());
		
		InfoFieldBean field;
		for (InfoOptionBean option: model.getInfoOptions()) {
			field = option.getInfoField();
			assertTrue(view.getAttributes().containsKey(field.getName()));
			assertEquals(option.getName(), view.getAttributes().get(field.getName()));
		}
	}

	@Test
	public void test_to_view_handles_null_product_status() throws ConversionException {
		Product model = createProduct();
		model.setProductStatus(null);
			
		ProductView view = converter.toView(model);
		
		assertNull(view.getProductStatus());
	}
	
	@Test
	public void test_to_view_handles_null_shop_order() throws ConversionException {
		Product model = createProduct();
		model.setShopOrder(null);
			
		ProductView view = converter.toView(model);
		
		assertNull(view.getShopOrder());
	}
	
}
