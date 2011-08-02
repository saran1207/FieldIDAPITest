package com.n4systems.api.asset;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.location.Location.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import com.n4systems.api.conversion.asset.AssetToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.model.Asset;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.test.helpers.Asserts;

public class AssetToViewConverterTest {
	private AssetToViewConverter converter = new AssetToViewConverter();
	
	private Asset createAsset() {
		Asset model = anAsset().ofType(AssetTypeBuilder.anAssetType().build())
			.withOwner(OrgBuilder.aCustomerOrg().build()).withIdentifier("12345")
			.withAdvancedLocation(onlyFreeformLocation("loc123")).build();
		model.setRfidNumber("rf1234");
		model.setCustomerRefNumber("cr12345");
		model.setPurchaseOrder("PO8282");
		model.setComments("comments 12312");
		model.setIdentified(new Date());
		
		AssetStatus ps = new AssetStatus();
		ps.setName("In Service");
		model.setAssetStatus(ps);
		
		Order order = new Order();
		order.setOrderNumber("ON12312312");
		LineItem line = new LineItem();
		line.setOrder(order);
		model.setShopOrder(line);
		
		InfoOptionBeanBuilder optionBuilder = InfoOptionBeanBuilder.aDynamicInfoOption();
		InfoFieldBeanBuilder fieldBuilder = InfoFieldBeanBuilder.aTextField();
		
		model.getInfoOptions().add(optionBuilder.withName("Opt1").forField(fieldBuilder.named("Field1").build()).build());
		model.getInfoOptions().add(optionBuilder.withName("Opt2").forField(fieldBuilder.named("Field2").build()).build());
		
		// ensure this is a non-integration tenant
		model.getOwner().getPrimaryOrg().getExtendedFeatures().remove(ExtendedFeature.Integration);
		
		return model;
	}
	
	@Test
	public void test_to_view_copies_all_properties() throws ConversionException, ParseException {
		Asset model = createAsset();
		
		model.getOwner().getPrimaryOrg().setDateFormat("yyyy-MM-dd");
		
		AssetView view = converter.toView(model);
		
		Asserts.assertMethodReturnValuesEqual(model, view, "getIdentifier", "getRfidNumber", "getCustomerRefNumber", "getPurchaseOrder", "getComments", "getIdentified");

		assertEquals(model.getAssetStatus().getName(), view.getStatus());
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
	public void test_to_view_handles_null_asset_status() throws ConversionException {
		Asset model = createAsset();
		model.setAssetStatus(null);
			
		AssetView view = converter.toView(model);
		
		assertNull(view.getStatus());
	}
	
	@Test
	public void test_to_view_handles_null_shop_order() throws ConversionException {
		Asset model = createAsset();
		model.setShopOrder(null);
			
		AssetView view = converter.toView(model);
		
		assertNull(view.getShopOrder());
	}
	
	@Test
	public void test_order_number_field_ignored_for_integration_customers()  throws ConversionException {
		Asset model = createAsset();
		model.getOwner().getPrimaryOrg().getExtendedFeatures().add(ExtendedFeature.Integration);
			
		AssetView view = converter.toView(model);
		
		assertNull(view.getShopOrder());
	}
	
}
