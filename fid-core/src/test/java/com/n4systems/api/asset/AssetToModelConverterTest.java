package com.n4systems.api.asset;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.model.*;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.builders.*;
import com.n4systems.model.infooption.InfoOptionConversionException;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.model.infooption.MissingInfoOptionException;
import com.n4systems.model.infooption.StaticOptionResolutionException;
import com.n4systems.model.location.*;
import com.n4systems.model.orders.NonIntegrationOrderManager;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.Asserts;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.persistence.TestingTransaction;
import org.junit.Before;
import org.junit.Test;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class AssetToModelConverterTest {

	private OrgByNameLoader dummyOrgLoader = new OrgByNameLoader(null) {
		final CustomerOrg org = OrgBuilder.aCustomerOrg().buildCustomer();
		@Override
		public BaseOrg load(Transaction transaction) {
			return org;
		}
	};
	
	private InfoOptionMapConverter dummyOptionConverter = new InfoOptionMapConverter() {

		@Override
		public List<InfoOptionBean> convertAssetAttributes(Map<String, String> optionMap, AssetType type) throws MissingInfoOptionException, StaticOptionResolutionException {
			return new ArrayList<InfoOptionBean>();
		}
	};
	
	private Transaction transaction = new TestingTransaction();
	
	AssetType type;
	
	@Before
	public void setup_asset_type() {
		InfoFieldBean[] fields = {
				InfoFieldBeanBuilder.aComboBox().named("combo").build(),
				InfoFieldBeanBuilder.aComboBox().named("select").build(),
				InfoFieldBeanBuilder.aComboBox().named("text").build()
		};
		
		fields[0].setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				InfoOptionBeanBuilder.aStaticInfoOption().withName("cf-1").forField(fields[0]).build(),
				InfoOptionBeanBuilder.aStaticInfoOption().withName("cf-2").forField(fields[0]).build()
		)));
		
		fields[1].setUnfilteredInfoOptions(new TreeSet<InfoOptionBean>(Arrays.asList(
				InfoOptionBeanBuilder.aStaticInfoOption().withName("sl-1").forField(fields[1]).build(),
				InfoOptionBeanBuilder.aStaticInfoOption().withName("sl-2").forField(fields[1]).build()		
		)));
		
		type = AssetTypeBuilder.anAssetType().named("my type").withFields(fields).build();
	}
	
	@Test
	public void to_model_uses_now_when_date_is_null() throws ConversionException {
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setType(type);
		converter.setIdentifiedBy(createIdentifiedBy());
		
		AssetView view = createView(null, null);
		view.setIdentified(null);
		
		Asset model =  converter.toModel(view, null);
		
		assertEquals(new PlainDate(), model.getIdentified());
	}

    @Test
    public void to_model_uses_location_freeform() throws ConversionException {
        String predefinedLocation = ":FreeFormText";

        Transaction trans = new DummyTransaction();

        PredefinedLocationTreeLoader treeLoader = createMock(PredefinedLocationTreeLoader.class);
        AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, treeLoader);
        converter.setType(type);
        converter.setIdentifiedBy(createIdentifiedBy());

        PredefinedLocationTree tree = new PredefinedLocationTree();
        expect(treeLoader.load(trans)).andReturn(tree);
        replay(treeLoader);

        AssetView view = createView(null, null);
        view.setLocation(predefinedLocation);
        view.setIdentified(null);

        Asset model =  converter.toModel(view, trans);
        assertEquals("FreeFormText", model.getAdvancedLocation().getFreeformLocation());
        verify(treeLoader);
    }

    @Test
    public void to_model_uses_location_tree() throws ConversionException {
        Transaction trans = new DummyTransaction();

        PredefinedLocationTreeLoader treeLoader = createMock(PredefinedLocationTreeLoader.class);
        AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, treeLoader);
        converter.setType(type);
        converter.setIdentifiedBy(createIdentifiedBy());

        PredefinedLocationTree tree = new PredefinedLocationTree();
        PredefinedLocation pdl = new PredefinedLocation();
        pdl.setName("level1");
        tree.addNode(new PredefinedLocationTreeNode(pdl));
        expect(treeLoader.load(trans)).andReturn(tree);
        replay(treeLoader);

        AssetView view = createView(null, null);
        view.setLocation("level1:moreFreeFormText");
        view.setIdentified(null);

        Asset model =  converter.toModel(view, trans);
        assertEquals("moreFreeFormText", model.getAdvancedLocation().getFreeformLocation());
        assertEquals("level1", model.getAdvancedLocation().getPredefinedLocation().getName());
        verify(treeLoader);
    }

    @Test
	public void to_model_resolves_owner_and_uses_tenant() throws ConversionException {
		Transaction trans = new DummyTransaction();
		CustomerOrg resolvedOrg = OrgBuilder.aCustomerOrg().buildCustomer();
		AssetView view = createView(null, null);
		
		OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		AssetToModelConverter converter = new AssetToModelConverter(orgLoader, null, null, dummyOptionConverter, null) {
            @Override
            protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setType(type);
		converter.setIdentifiedBy(createIdentifiedBy());
		
		expect(orgLoader.setOrganizationName(view.getOrganization())).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(view.getCustomer())).andReturn(orgLoader);
		expect(orgLoader.setDivision(view.getDivision())).andReturn(orgLoader);
		expect(orgLoader.load(trans)).andReturn(resolvedOrg);
		replay(orgLoader);
		
		Asset asset = converter.toModel(view, trans);
		verify(orgLoader);
		
		assertSame(resolvedOrg, asset.getOwner());
		assertSame(resolvedOrg.getTenant(), asset.getTenant());
	}
	
	@Test
	public void to_model_creates_order_when_not_null() throws ConversionException {
		AssetView view = createView("on1234", null);
		LineItem line = new LineItem();
		Tenant tenant = dummyOrgLoader.load((Transaction)null).getTenant();

		NonIntegrationOrderManager orgLoader = createMock(NonIntegrationOrderManager.class);
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, orgLoader, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setType(type);
		converter.setIdentifiedBy(createIdentifiedBy());
		
		expect(orgLoader.createAndSave(view.getShopOrder(), tenant)).andReturn(line);
		replay(orgLoader);
		
		Asset asset = converter.toModel(view, transaction);
		verify(orgLoader);
		assertSame(line, asset.getShopOrder());
	}
	
	@Test
	public void to_model_ignores_shop_order_for_integration_tenants() throws ConversionException {
		User identifiedBy = createIdentifiedBy();
		identifiedBy.getOwner().getPrimaryOrg().getExtendedFeatures().add(ExtendedFeature.Integration);
		
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		
		AssetView view = createView("12345", null);
		Asset model =  converter.toModel(view, null);
		
		assertNull(model.getShopOrder());
	}
	
	@Test
	public void to_model_asset_status_when_not_null() throws ConversionException {
		AssetView view = createView(null, "in service");
		AssetStatus status = new AssetStatus();
		
		Transaction trans = new DummyTransaction();
		AssetStatusByNameLoader psLoader = createMock(AssetStatusByNameLoader.class);
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, psLoader, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setType(type);
		converter.setIdentifiedBy(createIdentifiedBy());
		
		expect(psLoader.setName(view.getStatus())).andReturn(psLoader);
		expect(psLoader.load(trans)).andReturn(status);
		replay(psLoader);
		
		Asset asset = converter.toModel(view, trans);
		verify(psLoader);
		assertSame(status, asset.getAssetStatus());
	}
	
	@Test
	public void to_model_sets_identified_by_and_type() throws ConversionException {
		User identifiedBy = createIdentifiedBy();
		
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		
		Asset asset = converter.toModel(createView(null, null), null);

		assertSame(identifiedBy, asset.getIdentifiedBy());
		assertSame(type, asset.getType());
	}
	
	@Test
	public void to_model_publishes_asset_when_auto_publish_on() throws ConversionException {
		User identifiedBy = createIdentifiedBy();
		identifiedBy.getOwner().getPrimaryOrg().setAutoPublish(true);
		
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		
		Asset asset = converter.toModel(createView(null, null), null);

		assertTrue(asset.isPublished());
	}
	
	@Test
	public void to_model_does_not_publish_asset_when_auto_publish_off() throws ConversionException {
		User identifiedBy = createIdentifiedBy();
		identifiedBy.getOwner().getPrimaryOrg().setAutoPublish(false);
		
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setIdentifiedBy(identifiedBy);
		converter.setType(type);
		
		Asset asset = converter.toModel(createView(null, null), null);

		assertFalse(asset.isPublished());
	}
	
	@Test
	public void to_model_copies_non_resolved_properties() throws ConversionException {
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setIdentifiedBy(createIdentifiedBy());
		converter.setType(type);
		
		AssetView view = createView(null, null);
		Asset model =  converter.toModel(view, null);
		
		Asserts.assertMethodReturnValuesEqual(view, model, "getIdentifier", "getRfidNumber", "getCustomerRefNumber", "getPurchaseOrder", "getComments", "getIdentified");
	}
	
	@Test
	public void test_converts_info_options() throws ConversionException, InfoOptionConversionException {
		InfoOptionMapConverter mapConverter = createMock(InfoOptionMapConverter.class);
		AssetView view = createView(null, null);
		
		List<InfoOptionBean> options = Arrays.asList(InfoOptionBeanBuilder.aDynamicInfoOption().withName("test").build());
		
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, mapConverter, null) {
            @Override protected Location resolveLocation(AssetView view, Transaction transaction) {
                return null;
            }
        };
		converter.setType(type);
		converter.setIdentifiedBy(createIdentifiedBy());
		
		expect(mapConverter.convertAssetAttributes(view.getAttributes(), type)).andReturn(options);
		replay(mapConverter);
		
		Asset model =  converter.toModel(view, null);
		verify(mapConverter);
		
		assertEquals(options.size(), model.getInfoOptions().size());
		assertTrue(model.getInfoOptions().contains(options.get(0)));
	}
	
	@Test(expected=ConversionException.class)
	public void to_model_throws_exception_when_identified_is_not_a_date() throws ConversionException {
		AssetToModelConverter converter = new AssetToModelConverter(dummyOrgLoader, null, null, dummyOptionConverter, null);
		converter.setIdentifiedBy(createIdentifiedBy());
		converter.setType(type);
		
		AssetView view = createView(null, null);
		view.setIdentified("bad date");
		
		converter.toModel(view, null);
	}
	
	private User createIdentifiedBy() {
		User user = UserBuilder.anEmployee().build();
		
		user.getOwner().getPrimaryOrg().setDateFormat("yyyy-MM-dd");
		user.getOwner().getPrimaryOrg().getExtendedFeatures().remove(ExtendedFeature.Integration);
		return user;
	}
	
	private AssetView createView(String shopOrder, String status) {
		AssetView view = new AssetView();
		view.setOrganization("My Org");
		view.setCustomer("My Customer");
		view.setDivision("My Division");
		view.setIdentifier("sn1234");
		view.setRfidNumber("rf1234");
		view.setCustomerRefNumber("cr12345");
		view.setLocation("loc123");
		view.setPurchaseOrder("po8282");
		view.setComments("comments 12312");
		view.setShopOrder(shopOrder);
		view.setStatus(status);
		view.setIdentified(new PlainDate());

		view.getAttributes().put("combo", "cf-1");
		view.getAttributes().put("select", "sl-2");
		view.getAttributes().put("text", "dynamic");
		
		return view;
	}
}
