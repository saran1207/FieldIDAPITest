package com.n4systems.api.conversion.event;

import com.google.common.collect.Lists;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.model.EventView;
import com.n4systems.model.*;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.builders.*;
import com.n4systems.model.eventbook.EventBookFindOrCreateLoader;
import com.n4systems.model.location.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.AssetsByIdOwnerTypeLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class EventToModelConverterTest {
	private final class EventToModelConverterWithAllButConvertPerformedByEmptied extends EventToModelConverter {
		private EventToModelConverterWithAllButConvertPerformedByEmptied(UserByFullNameLoader userLoader) {
			super(null, null, null, null, userLoader, null, null, TimeZone.getDefault());
		}
		@Override protected void resolveType(Event model) {}
		@Override protected void resolveStatus(String statusName, Event model) {}
		@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
		@Override protected void resolvePrintable(EventView view, Event model) {}
		@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
		@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
		@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
        @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
    }

	private static final String PERSONS_NAME = "Full Name";
	private Transaction transaction = new DummyTransaction();
		
	@Test
	public void to_model_sets_type_form_version_and_tenant_from_type() throws ConversionException {
		EventType type = EventTypeBuilder.anEventType().build();
		type.setTenant(TenantBuilder.aTenant().build());
		
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null, null, type, TimeZone.getDefault()) {
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
	
		
		Event event = converter.toModel(new EventView(), transaction);
		
		assertEquals(type, event.getType());
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
		
		EventToModelConverter converter = new EventToModelConverter(orgLoader, null, null, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		assertEquals(org, converter.toModel(view, transaction).getOwner());
		verify(orgLoader);
	}

    @Test
    public void to_model_resolves_location() throws ConversionException {
        EventView view = new EventView();
        view.setLocation("hello > world : freeForm");

        PredefinedLocation hello = PredefinedLocationBuilder.aPredefinedLocation().withName("hello").build();
        PredefinedLocation world = PredefinedLocationBuilder.aPredefinedLocation().withName("world").withParent(hello).build();
        PredefinedLocationTree tree = new PredefinedLocationTree();

        PredefinedLocationTreeNode node = new PredefinedLocationTreeNode(hello);
        PredefinedLocationTreeNode child = new PredefinedLocationTreeNode(world);
        node.addChild(child);
        tree.addNode(node);

        PredefinedLocationTreeLoader predefinedLocationTreeLoader = createMock(PredefinedLocationTreeLoader.class);
        expect(predefinedLocationTreeLoader.load(transaction)).andReturn(tree);
        replay(predefinedLocationTreeLoader);

        EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null, predefinedLocationTreeLoader, null, TimeZone.getDefault()) {
            @Override protected void resolveType(Event model) {}
            @Override protected void resolveStatus(String statusName, Event model) {}
            @Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolvePrintable(EventView view, Event model) {}
            @Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {
                PrimaryOrg owner = new PrimaryOrg();
                owner.getExtendedFeatures().add(ExtendedFeature.AdvancedLocation);
                model.setOwner(owner);
            }
        };

        Location result = converter.toModel(view, transaction).getAdvancedLocation();
        assertEquals(world, result.getPredefinedLocation());
        assertEquals("freeForm", result.getFreeformLocation());
        verify(predefinedLocationTreeLoader);
    }



    @Test
	public void to_model_resolves_status_ignoring_case() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
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
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
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
		
		AssetsByIdOwnerTypeLoader loader = createMock(AssetsByIdOwnerTypeLoader.class);
		expect(loader.setIdentifier(view.getIdentifier())).andReturn(loader);
		expect(loader.setOwner(view.getOrganization(), view.getCustomer(), view.getDivision())).andReturn(loader);
		expect(loader.load(transaction)).andReturn(Arrays.asList(asset));
		replay(loader);
		
		EventToModelConverter converter = new EventToModelConverter(null, loader, null, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		assertEquals(asset, converter.toModel(view, transaction).getAsset());
		verify(loader);
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
		
		EventToModelConverter converter = new EventToModelConverterWithAllButConvertPerformedByEmptied(userLoader);
		
		assertEquals(user, converter.toModel(view, transaction).getPerformedBy());
		verify(userLoader);
	}
	
	@Test
	public void to_model_resolves_printable() throws ConversionException {
		EventToModelConverter converter = new EventToModelConverter(null, null, null, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		EventView view = new EventView();
		
		view.setPrintable("Y");
		assertTrue(converter.toModel(view, transaction).isPrintable());
		
		view.setPrintable("N");
		assertFalse(converter.toModel(view, transaction).isPrintable());
	}
	
	@Test
	public void to_model_resolves_event_book() throws ConversionException {
		EventView view = new EventView();
		view.setEventBook("my book");
		
		final BaseOrg owner = OrgBuilder.aPrimaryOrg().build();
		EventBook book = new EventBook();
		
		EventBookFindOrCreateLoader bookLoader = createMock(EventBookFindOrCreateLoader.class);
		expect(bookLoader.setName(view.getEventBook())).andReturn(bookLoader);
		expect(bookLoader.setOwner(owner)).andReturn(bookLoader);
		expect(bookLoader.load(transaction)).andReturn(book);
		replay(bookLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, null, bookLoader, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {
				model.setOwner(owner);
			}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		assertSame(book, converter.toModel(view, transaction).getBook());
		verify(bookLoader);
	}
	
	@Test
	public void to_model_ignores_event_book_when_null() throws ConversionException {
		EventBookFindOrCreateLoader bookLoader = createMock(EventBookFindOrCreateLoader.class);
		replay(bookLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, null, bookLoader, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
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
		
		EventToModelConverter converter = new EventToModelConverter(null, null, psLoader, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		assertSame(ps, converter.toModel(view, transaction).getAssetStatus());
		verify(psLoader);
	}
	
	@Test
	public void to_model_ignores_asset_status_when_null() throws ConversionException {
		AssetStatusByNameLoader psLoader = createMock(AssetStatusByNameLoader.class);
		replay(psLoader);
		
		EventToModelConverter converter = new EventToModelConverter(null, null, psLoader, null, null, null, null, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };
		
		assertNull(converter.toModel(new EventView(), transaction).getAssetStatus());
		verify(psLoader);
	}
	
	@Test
	public void to_model_criteria_results() throws ConversionException {
		AssetStatusByNameLoader psLoader = createMock(AssetStatusByNameLoader.class);
		replay(psLoader);

		EventView eventView = new EventView();
		CriteriaResultView criteriaResultView = new CriteriaResultViewBuilder().aCriteriaResultView().build();
		Collection<CriteriaResultView> criteriaResults = Lists.newArrayList(criteriaResultView);
		eventView.setCriteriaResults(criteriaResults);

		EventForm eventForm = new EventForm();
		StateSet stateSet = StateSetBuilder.aStateSet().states(new State("Pass", Status.PASS, "Pass")).build();
		OneClickCriteria criteria = OneClickCriteriaBuilder.aCriteria().withStateSet(stateSet).build();
		
		CriteriaSection section = CriteriaSectionBuilder.aCriteriaSection().
										withCriteria(criteria).
										withDisplayText(criteriaResultView.getSection()).
										build();
		ArrayList<CriteriaSection> sections = Lists.newArrayList(section);
		eventForm.setSections(sections);
				
		EventType type = EventTypeBuilder.anEventType().withEventForm(eventForm).build();
		type.setTenant(TenantBuilder.aTenant().build());
		
		EventToModelConverter converter = new EventToModelConverter(null, null, psLoader, null, null, null, type, TimeZone.getDefault()) {
			@Override protected void resolveType(Event model) {}
			@Override protected void resolveStatus(String statusName, Event model) {}
			@Override protected void resolveAsset(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolvePrintable(EventView view, Event model) {}
			@Override protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveEventBook(EventView view, Event model, Transaction transaction) {}
			@Override protected void resolveOwner(EventView view, Event model, Transaction transaction) {}
            @Override protected void resolveLocation(EventView view, Event model, Transaction transaction) {}
        };

	
		Set<CriteriaResult> results = converter.toModel(eventView, transaction).getResults();
		assertNotNull(results);
		assertEquals(1, results.size());	
		
		CriteriaResult onlyMemberOfSet = results.iterator().next();
		assertTrue(onlyMemberOfSet instanceof OneClickCriteriaResult);
		OneClickCriteriaResult oneClickCriteriaResult = (OneClickCriteriaResult)onlyMemberOfSet;

		assertEquals(criteria, oneClickCriteriaResult.getCriteria());
		assertEquals(Status.PASS, oneClickCriteriaResult.getState().getStatus());
		assertEquals(eventForm, oneClickCriteriaResult.getEvent().getEventForm() );
		
		verify(psLoader);
	}
	
	
	
}
