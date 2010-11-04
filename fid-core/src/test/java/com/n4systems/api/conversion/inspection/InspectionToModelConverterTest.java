package com.n4systems.api.conversion.inspection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.n4systems.api.model.EventView;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.inspectionbook.EventBookFindOrCreateLoader;
import org.junit.Test;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.model.EventBook;
import com.n4systems.model.Asset;
import com.n4systems.model.Status;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;

public class InspectionToModelConverterTest {
	private final class InspectionToModelConverterWithAllButConvertPerformedByEmptied extends EventToModelConverter {
		private InspectionToModelConverterWithAllButConvertPerformedByEmptied(UserByFullNameLoader userLoader) {
			super(null, null, null, null, userLoader);
		}

		protected void resolveType(Event model) {}

		protected void resolveStatus(String statusName, Event model) {}

		protected void resolveAsset(EventView view, Event model, Transaction transaction) {}

		protected void resolvePrintable(EventView view, Event model) {}

		protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}

		protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}

		protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
	}

	private static final String PERSONS_NAME = "Full Name";
	private Transaction transaction = new DummyTransaction();
		
	@Test
	public void to_model_sets_type_form_version_and_tenant_from_type() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null) {
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		EventType type = EventTypeBuilder.anEventType().build();
		type.setTenant(TenantBuilder.aTenant().build());
		
		converter.setType(type);
		
		Event event = converter.toModel(new EventView(), transaction);
		
		assertEquals(type, event.getType());
		assertEquals(type.getFormVersion(), event.getFormVersion());
		assertEquals(type.getTenant(), event.getTenant());
	}
	
	@Test
	public void to_model_resolves_owner() throws ConversionException {
		EventView view = new EventView();
		view.setCustomer("customer_name");
		view.setDivision("division_name");
		view.setOrganization("org_name");
		
		BaseOrg org = OrgBuilder.aDivisionOrg().build();
		
		OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		expect(orgLoader.setOrganizationName(view.getOrganization())).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(view.getCustomer())).andReturn(orgLoader);
		expect(orgLoader.setDivision(view.getDivision())).andReturn(orgLoader);
		expect(orgLoader.load(transaction)).andReturn(org);
		replay(orgLoader);
		
		EventToModelConverter converter = new EventToModelConverter(orgLoader, null, null, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
		};
		
		assertEquals(org, converter.toModel(view, transaction).getOwner());
		verify(orgLoader);
	}
	
	@Test
	public void to_model_resolves_status_ignoring_case() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		EventView view = new EventView();
		
		view.setStatus("PaSs");
		assertEquals(Status.PASS, converter.toModel(view, transaction).getStatus());
		
		view.setStatus("fail");
		assertEquals(Status.FAIL, converter.toModel(view, transaction).getStatus());
		
		view.setStatus("N/A");
		assertEquals(Status.NA, converter.toModel(view, transaction).getStatus());		
	}
	
	@Test
	public void to_model_resolves_status_defaulting_to_na() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		EventView view = new EventView();
		
		view.setStatus("bleh");
		assertEquals(Status.NA, converter.toModel(view, transaction).getStatus());		
	}
	
	@Test
	public void to_model_resolves_asset_via_smart_search() throws ConversionException {
		EventView view = new EventView();
		view.setIdentifier("serial number");
		
		Asset asset = AssetBuilder.anAsset().build();
		
		SmartSearchLoader smartSearchLoader = createMock(SmartSearchLoader.class);
		expect(smartSearchLoader.setSearchText(view.getIdentifier())).andReturn(smartSearchLoader);
		expect(smartSearchLoader.load(transaction)).andReturn(Arrays.asList(asset));
		replay(smartSearchLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, smartSearchLoader, null, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		assertEquals(asset, converter.toModel(view, transaction).getAsset());
		verify(smartSearchLoader);
	}
	
	@Test
	public void to_model_resolves_performed_by() throws ConversionException {
		EventView view = new EventView();
		view.setPerformedBy(PERSONS_NAME);
		
		User user = UserBuilder.aUser().build();
		
		UserByFullNameLoader userLoader = createMock(UserByFullNameLoader.class);
		expect(userLoader.setFullName(view.getPerformedBy())).andReturn(userLoader);
		expect(userLoader.load(transaction)).andReturn(Arrays.asList(user));
		replay(userLoader);
		
		EventToModelConverter converter = new InspectionToModelConverterWithAllButConvertPerformedByEmptied(userLoader);
		
		assertEquals(user, converter.toModel(view, transaction).getPerformedBy());
		verify(userLoader);
	}
	
	@Test
	public void to_model_resolves_printable() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		EventView view = new EventView();
		
		view.setPrintable("Y");
		assertTrue(converter.toModel(view, transaction).isPrintable());
		
		view.setPrintable("N");
		assertFalse(converter.toModel(view, transaction).isPrintable());
	}
	
	@Test
	public void to_model_resolves_inspection_book() throws ConversionException {
		EventView view = new EventView();
		view.setInspectionBook("my book");
		
		final BaseOrg owner = OrgBuilder.aPrimaryOrg().build();
		EventBook book = new EventBook();
		
		EventBookFindOrCreateLoader bookLoader = createMock(EventBookFindOrCreateLoader.class);
		bookLoader.setName(view.getInspectionBook());
		bookLoader.setOwner(owner);
		expect(bookLoader.load(transaction)).andReturn(book);
		replay(bookLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, null, bookLoader, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {
				model.setOwner(owner);
			}
		};
		
		assertSame(book, converter.toModel(view, transaction).getBook());
		verify(bookLoader);
	}
	
	@Test
	public void to_model_ignores_inspection_book_when_null() throws ConversionException {
		EventBookFindOrCreateLoader bookLoader = createMock(EventBookFindOrCreateLoader.class);
		replay(bookLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, null, bookLoader, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		assertNull(converter.toModel(new EventView(), transaction).getBook());
		verify(bookLoader);
	}
	
	@Test
	public void to_model_resolves_asset_status() throws ConversionException {
		EventView view = new EventView();
		view.setAssetStatus("asset status");
		
		AssetStatus ps = new AssetStatus();
		
		AssetStatusByNameLoader psLoader = createMock(AssetStatusByNameLoader.class);
		expect(psLoader.setName(view.getAssetStatus())).andReturn(psLoader);
		expect(psLoader.load(transaction)).andReturn(ps);
		replay(psLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, psLoader, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		assertSame(ps, converter.toModel(view, transaction).getAssetStatus());
		verify(psLoader);
	}
	
	@Test
	public void to_model_ignores_asset_status_when_null() throws ConversionException {
		AssetStatusByNameLoader psLoader = createMock(AssetStatusByNameLoader.class);
		replay(psLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, psLoader, null, null) {
			protected void resolveType(Event model) {}
			protected void resolveStatus(String statusName, Event model) {}
			protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			protected void resolvePrintable(EventView view, Event model) {}
			protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			protected void resolveInspectionBook(EventView view, Event model, Transaction transaction) {}
			protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
		};
		
		assertNull(converter.toModel(new EventView(), transaction).getAssetStatus());
		verify(psLoader);
	}
}
