package com.n4systems.reporting;

import static com.n4systems.model.builders.EventBuilder.*;
import static com.n4systems.model.builders.OrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import net.sf.jasperreports.engine.JasperPrint;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.testutils.TestConfigContext;

public class EventSummaryGeneratorTest {
	
	private DateTimeDefiner dateDefiner = new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault());
	private ReportDefiner reportDefiner;
	private PersistenceManager persistenceManager;
	private EventManager eventManager;
	private User user;
	
	@Before
	public void setUp() {
		reportDefiner = createMock(ReportDefiner.class);
		persistenceManager = createMock(PersistenceManager.class);
		eventManager = createMock(EventManager.class);
		user = UserBuilder.aUser().build();
		
		TestConfigContext.newContext();
	}
	
	@After
	public void reset_config_context() {
		TestConfigContext.resetToDefaultContext();
	}
	
	@Test
	@Ignore("This test keeps breaking since it requires a specific jrxml file to be in the right place on the filesystem.  The jasper stuff needs to be mocked out.")
	public void generate_event_summary() throws Exception {
		final Event printableEvent = createPrintableInspection();
		
		setReportDefinerExpectations(printableEvent);
		
		EventSummaryGenerator generator = new EventSummaryGenerator(dateDefiner, persistenceManager, eventManager, null, null) {
			@Override
			protected PrimaryOrg getTenant(User user, Long tenantId) {
				return (PrimaryOrg) aPrimaryOrg().build();
			}
		};
		
		JasperPrint jp = generator.generate(reportDefiner, Arrays.asList(printableEvent.getId()), user);
		assertNotNull(jp);
		verify(reportDefiner);
		verify(eventManager);
	}

	private void setReportDefinerExpectations(Event event) {
		expect(reportDefiner.getIdentifier()).andReturn("");
		expect(reportDefiner.getRfidNumber()).andReturn("");
		expect(reportDefiner.getOrderNumber()).andReturn("");
		expect(reportDefiner.getPurchaseOrder()).andReturn("");
		expect(reportDefiner.getToDate()).andReturn(new Date());
		expect(reportDefiner.getFromDate()).andReturn(new Date());
		expect(reportDefiner.getAssetType()).andReturn(null);
		expect(reportDefiner.getEventBook()).andReturn(null);
		expect(reportDefiner.getEventTypeGroup()).andReturn(null);
		expect(reportDefiner.getPerformedBy()).andReturn(null);
		expect(reportDefiner.getOwner()).andReturn(null);
		replay(reportDefiner);
		
		expect(eventManager.findAllFields(eq(event.getId()), isA(SecurityFilter.class))).andReturn(event);
		replay(eventManager);
	}

	private Event createPrintableInspection() {
		EventTypeGroup eventTypeGroup = createPrintableInspectionTypeGroup();
				
		EventType printableEventType = EventTypeBuilder.anEventType().withGroup(eventTypeGroup).build();
		Event event = anEvent().ofType(printableEventType).build();
		return event;
	}
	
	private EventTypeGroup createPrintableInspectionTypeGroup() {
		EventTypeGroup eventTypeGroup = new EventTypeGroup();
		eventTypeGroup.setPrintOut(new PrintOut());
		eventTypeGroup.getPrintOut().setPdfTemplate("somefile");
		eventTypeGroup.getPrintOut().setType(PrintOutType.CERT);
		
		Assert.assertTrue(eventTypeGroup.getPrintOutForReportType(EventReportType.INSPECTION_CERT) != null);
		
		return eventTypeGroup;
	}
}
