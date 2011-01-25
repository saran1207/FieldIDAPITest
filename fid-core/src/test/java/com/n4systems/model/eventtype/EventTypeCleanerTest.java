package com.n4systems.model.eventtype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.UserBuilder;
import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.TenantBuilder;

public class EventTypeCleanerTest {
	private static final String name = "type_name";	
	private static final String description = "type_desc";
	private static final boolean printable = true;
	private static final boolean retired = false;
	private static final boolean master = false;
	private static final long formVersion = EventType.DEFAULT_FORM_VERSION;
	private static final EventTypeGroup GROUP = new EventTypeGroup();
	private static final Set<ProofTestType> supportedProofTests = new HashSet<ProofTestType>(Arrays.asList(ProofTestType.CHANT, ProofTestType.NATIONALAUTOMATION));;
	private static final List<String> infoFieldNames = Arrays.asList("field 1", "field 2", "field 3");	

	@SuppressWarnings("unchecked")
	@Test
	public void clean_cleans_required_fields() {
		EventType type = buildType();
		Tenant newTenant = TenantBuilder.aTenant().build();
		
		assertNotNull(type.getId());
		assertNotNull(type.getCreated());
		assertNotNull(type.getModified());
		assertNotNull(type.getModifiedBy());
		
		Cleaner<EventForm> eventFormCleaner = EasyMock.createMock(Cleaner.class);
		eventFormCleaner.clean(type.getEventForm());
		EasyMock.replay(eventFormCleaner);
		
		EventTypeCleaner cleaner = new EventTypeCleaner(newTenant, eventFormCleaner);
		cleaner.clean(type);
		
		assertNull(type.getId());
		assertNull(type.getCreated());
		assertNull(type.getModified());
		assertNull(type.getModifiedBy());
		
		assertSame(newTenant, type.getTenant());
		assertSame(GROUP, type.getGroup());
		assertEquals(name, type.getName());
		assertEquals(description, type.getDescription());
		assertEquals(printable, type.isPrintable());
		assertEquals(retired, type.isRetired());
		assertEquals(master, type.isMaster());
		assertEquals(formVersion, type.getFormVersion());
		
		// supported proffs and infofield names should be equal but NOT the same
		assertEquals(supportedProofTests, type.getSupportedProofTests());
		assertNotSame(supportedProofTests, type.getSupportedProofTests());
		assertEquals(infoFieldNames, type.getInfoFieldNames());
		assertNotSame(infoFieldNames, type.getInfoFieldNames());
		
		EasyMock.verify(eventFormCleaner);
	}
	
	private EventType buildType() {
		EventType type = EventTypeBuilder.anEventType().build();
		type.setName(name);
		type.setDescription(description);
		type.setPrintable(printable);
		type.setRetired(retired);
		type.setMaster(master);
		type.setFormVersion(formVersion);
		type.setGroup(GROUP);
		type.setSupportedProofTests(supportedProofTests);
		type.setInfoFieldNames(infoFieldNames);
        type.setEventForm(new EventForm());
        type.setModifiedBy(UserBuilder.aUser().build());
		return type;
	}
	
}
