package com.n4systems.reporting;

import static com.n4systems.model.builders.EventBuilder.anEvent;
import static org.easymock.EasyMock.*;

import com.n4systems.model.Event;
import com.n4systems.model.builders.EventTypeBuilder;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.PrintOut.PrintOutType;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigContextOverridableTestDouble;
public class InspectionCertificateReportGeneratorTest {

	
	private ConfigContext oldContext;

	@Before
	public void changeConfigContext() {
		oldContext = ConfigContext.getCurrentContext();
		ConfigContext.setCurrentContext(new ConfigContextOverridableTestDouble());
	}
	
	@After 
	public void removeConfig() {
		ConfigContext.setCurrentContext(oldContext);
	}
	
	
	@Test
	public void should_send_a_printable_inspection_to_the_cert_generateor() throws Exception {
		Event printableEvent = createPrintableInspection();
		
		EventCertificateGenerator certGenerator = createMock(EventCertificateGenerator.class);
		expect(certGenerator.generate(EventReportType.INSPECTION_CERT, printableEvent, null)).andReturn(null);
		replay(certGenerator);
		
		EventCertificateReportGenerator sut = new EventCertificateReportGenerator(certGenerator);
		
		sut.setType(EventReportType.INSPECTION_CERT);
		Transaction transaction = null;
		sut.generate(new FluentArrayList<Event>(printableEvent), new NullOutputStream(), "packageName", transaction);
		
		verify(certGenerator);
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
