package com.n4systems.api.conversion.event;

import static com.n4systems.model.location.Location.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Date;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import org.junit.Test;


import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.EventView;
import com.n4systems.model.Status;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;

public class EventToViewConverterTest {

	@Test
	public void to_view_copies_simple_fields() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		Event model = new Event();
		model.setComments("comments");
		model.setDate(new Date());
		model.setAdvancedLocation(Location.onlyFreeformLocation("location"));
		model.setPrintable(true);
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getComments(), view.getComments());
		assertEquals(model.getDate(), view.getDatePerformed());
		assertEquals(model.getAdvancedLocation(), onlyFreeformLocation(view.getLocation()));
		assertEquals(model.isPrintable(), view.isPrintable());
	}
	
	@Test
	public void to_view_copies_event_status() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		Event model = new Event();
		model.setStatus(Status.PASS);
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getStatus().toString(), view.getStatus().toUpperCase());
	}
	
	@Test
	public void to_view_copies_identifier_to_identifier() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		Event model = new Event();
		model.setAsset(AssetBuilder.anAsset().withIdentifier("serial").build());
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getAsset().getIdentifier(), view.getIdentifier());
	}
	
	@Test
	public void to_view_copies_performed_by_full_name() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		Event model = new Event();
		model.setPerformedBy(UserBuilder.anEmployee().withFirstName("Mark").withLastName("F").build());
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getPerformedBy().getFullName(), view.getPerformedBy());
	}
	
	@Test
	public void to_view_copies_loads_next_event_date() throws ConversionException {
		Event model = new Event();
		Date nextDate = new Date();
		
		NextEventDateByEventLoader loader = createMock(NextEventDateByEventLoader.class);
		expect(loader.setEvent(model)).andReturn(loader);
		expect(loader.load()).andReturn(nextDate);
		replay(loader);
		
		EventToViewConverter converter = new EventToViewConverter(loader) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		EventView view = converter.toView(model);
		
		assertEquals(nextDate, view.getNextEventDate());
		
		verify(loader);
	}
	
	@Test
	public void to_view_copies_book_name() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		EventBook book = new EventBook();
		book.setName("event book");
		
		Event model = new Event();
		model.setBook(book);
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getBook().getName(), view.getEventBook());
	}
	
	@Test
	public void to_view_allows_null_books() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		EventView view = converter.toView(new Event());
		
		assertNull(view.getEventBook());
	}
	
	@Test
	public void to_view_copies_asset_status_name() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		AssetStatus pse = new AssetStatus();
		pse.setName("asset status");
		
		Event model = new Event();
		model.setAssetStatus(pse);
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getAssetStatus().getName(), view.getAssetStatus());
	}
	
	@Test
	public void to_view_allows_null_asset_status() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertOwnerFields(BaseOrg owner, EventView view) {}
		};
		
		EventView view = converter.toView(new Event());
		
		assertNull(view.getAssetStatus());
	}
	
	@Test
	public void to_view_copies_owner_fields() throws ConversionException {
		EventToViewConverter converter = new EventToViewConverter(null) {
			protected void convertDirectFields(Event model, EventView view) {}
			protected void convertEventStatus(Event model, EventView view) {}
			protected void convertAssetIdentifier(Event model, EventView view) {}
			protected void converterPerformedBy(Event model, EventView view) {}
			protected void convertNextDate(Event model, EventView view) {}
			protected void convertBook(Event model, EventView view) {}
			protected void convertAssetStatus(Event model, EventView view) {}
		};
		
		BaseOrg org = OrgBuilder.aDivisionOrg().withName("division").withParent(
				OrgBuilder.aCustomerOrg().withName("customer").withParent(
						OrgBuilder.aPrimaryOrg().withName("primary").build()
				).build()
		).build();
		
		
		Event model = new Event();
		model.setOwner(org);
		
		EventView view = converter.toView(model);
		
		assertEquals(model.getOwner().getName(), view.getDivision());
		assertEquals(model.getOwner().getParent().getName(), view.getCustomer());
		assertEquals(model.getOwner().getParent().getParent().getName(), view.getOrganization());
	}
}
