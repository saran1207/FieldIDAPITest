package com.n4systems.api.product;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.product.ProductToModelConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.model.LineItem;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.ProductTypeBuilder;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.MissingInfoOptionException;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.productstatus.ProductStatusByNameLoader;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.Asserts;
import com.n4systems.testutils.DummyTransaction;

public class ProductToModelConverterTest {

	private OrgByNameLoader dummyOrgLoader = new OrgByNameLoader(null) {
		final CustomerOrg org = OrgBuilder.aCustomerOrg().buildCustomer();
		@Override
		public BaseOrg load(Transaction transaction) {
			return org;
		}
	};
	
	private InfoOptionMapConverter dummyOptionConverter = new InfoOptionMapConverter() {
		@Override
		public List<InfoOptionBean> convertProductAttributes(Map<String, String> optionMap, ProductType type) throws MissingInfoOptionException, StaticOptionResolutionException {
			return new ArrayList<InfoOptionBean>();
		}
	};
	
	ProductType type;
	
	@Before
	public void setup_product_type() {
		InfoFieldBean[] fields = {
				InfoFieldBeanBuilder.aComboBox().withName("combo").build(),
				InfoFieldBeanBuilder.aComboBox().withName("select").build(),
				InfoFieldBeanBuilder.aComboBox().withName("text").build()
		};
		
		fields[0].setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				InfoOptionBeanBuilder.aStaticInfoOption().withName("cf-1").forField(fields[0]).build(),
				InfoOptionBeanBuilder.aStaticInfoOption().withName("cf-2").forField(fields[0]).build()
		)));
		
		fields[1].setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				InfoOptionBeanBuilder.aStaticInfoOption().withName("sl-1").forField(fields[1]).build(),
				InfoOptionBeanBuilder.aStaticInfoOption().withName("sl-2").forField(fields[1]).build()		
		)));
		
		type = ProductTypeBuilder.aProductType().named("my type").withFields(fields).build();
	}
	
	@Test
	public void to_model_uses_now_when_date_is_null() throws ConversionException {
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter);
		converter.setType(type);
		
		ProductView view = createView(null, null);
		view.setIdentified(null);
		
		Product model =  converter.toModel(view, null);
		
		assertEquals(new PlainDate(), model.getIdentified());
	}
	
	@Test
	public void to_model_resolves_owner_and_uses_tenant() throws ConversionException {
		Transaction trans = new DummyTransaction();
		CustomerOrg resolvedOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		ProductView view = createView(null, null);
		
		OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		ProductToModelConverter converter = new ProductToModelConverter(orgLoader, null, null, dummyOptionConverter);
		converter.setType(type);
		
		expect(orgLoader.setName(view.getOwner())).andReturn(orgLoader);
		expect(orgLoader.load(trans)).andReturn(resolvedOrg);
		replay(orgLoader);
		
		Product product = converter.toModel(view, trans);
		verify(orgLoader);
		
		assertSame(resolvedOrg, product.getOwner());
		assertSame(resolvedOrg.getTenant(), product.getTenant());
	}
	
	@Test
	public void to_model_creates_order_when_not_null() throws ConversionException {
		ProductView view = createView("on1234", null);
		LineItem line = new LineItem();
		Tenant tenant = dummyOrgLoader.load(null).getTenant();
		
		Transaction trans = new DummyTransaction();
		NonIntegrationOrderManager orgLoader = createMock(NonIntegrationOrderManager.class);
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, orgLoader, null, dummyOptionConverter);
		converter.setType(type);
		
		expect(orgLoader.createAndSave(view.getShopOrder(), tenant, trans)).andReturn(line);
		replay(orgLoader);
		
		Product product = converter.toModel(view, trans);
		verify(orgLoader);
		assertSame(line, product.getShopOrder());
	}
	
	@Test
	public void to_model_product_status_when_not_null() throws ConversionException {
		ProductView view = createView(null, "in service");
		ProductStatusBean status = new ProductStatusBean();
		
		Transaction trans = new DummyTransaction();
		ProductStatusByNameLoader psLoader = createMock(ProductStatusByNameLoader.class);
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, null, psLoader, dummyOptionConverter);
		converter.setType(type);
		
		expect(psLoader.setName(view.getProductStatus())).andReturn(psLoader);
		expect(psLoader.load(trans)).andReturn(status);
		replay(psLoader);
		
		Product product = converter.toModel(view, trans);
		verify(psLoader);
		assertSame(status, product.getProductStatus());
	}
	
	@Test
	public void to_model_sets_identified_by_and_type() throws ConversionException {
		UserBean identifiedBy = new UserBean();
		
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter);
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		
		Product product = converter.toModel(createView(null, null), null);

		assertSame(identifiedBy, product.getIdentifiedBy());
		assertSame(type, product.getType());	
	}
	
	@Test
	public void to_model_copies_non_resolved_properties() throws ConversionException {
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter);
		converter.setType(type);
		
		ProductView view = createView(null, null);
		Product model =  converter.toModel(view, null);

		model.setSerialNumber(view.getSerialNumber());
		model.setRfidNumber(view.getRfidNumber());
		model.setCustomerRefNumber(view.getCustomerRefNumber());
		model.setLocation(view.getLocation());
		model.setPurchaseOrder(view.getPurchaseOrder());
		model.setComments(view.getComments());
		
		Asserts.assertMethodReturnValuesEqual(view, model, "getSerialNumber", "getRfidNumber", "getCustomerRefNumber", "getLocation", "getPurchaseOrder", "getComments", "getIdentified");
	}
	
	@Test
	public void test_converts_info_options() throws ConversionException, InfoOptionConversionException {
		InfoOptionMapConverter mapConverter = createMock(InfoOptionMapConverter.class);
		ProductView view = createView(null, null);
		
		List<InfoOptionBean> options = Arrays.asList(InfoOptionBeanBuilder.aDynamicInfoOption().withName("test").build());
		
		ProductToModelConverter converter = new ProductToModelConverter(dummyOrgLoader, null, null, mapConverter);
		converter.setType(type);
		
		expect(mapConverter.convertProductAttributes(view.getAttributes(), type)).andReturn(options);
		replay(mapConverter);
		
		Product model =  converter.toModel(view, null);
		verify(mapConverter);
		
		assertEquals(options.size(), model.getInfoOptions().size());
		assertTrue(model.getInfoOptions().contains(options.get(0)));
	}
	
	private ProductView createView(String shopOrder, String status) {
		ProductView view = new ProductView(); 
		view.setOwner("My Org");
		view.setSerialNumber("sn1234");
		view.setRfidNumber("rf1234");
		view.setCustomerRefNumber("cr12345");
		view.setLocation("loc123");
		view.setPurchaseOrder("po8282");
		view.setComments("comments 12312");
		view.setShopOrder(shopOrder);
		view.setStatus(status);

		 // We want to make sure this date is not today
		view.setIdentified(new PlainDate(new Date(System.currentTimeMillis() - (3600000 * 25))));
		
		view.getAttributes().put("combo", "cf-1");
		view.getAttributes().put("select", "sl-2");
		view.getAttributes().put("text", "dynamic");
		
		return view;
	}
}
