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
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.criteriasection.CriteriaSectionBuilder;

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
	private static final List<CriteriaSection> sections= Arrays.asList(
			CriteriaSectionBuilder.aCriteriaSection().build(),
			CriteriaSectionBuilder.aCriteriaSection().withRetired(true).build(),
			CriteriaSectionBuilder.aCriteriaSection().build()
		);

	@SuppressWarnings("unchecked")
	@Test
	public void clean_cleans_required_fields() {
		EventType type = buildType();
		Tenant newTenant = TenantBuilder.aTenant().build();
		
		assertNotNull(type.getId());
		assertNotNull(type.getCreated());
		assertNotNull(type.getModified());
		assertNotNull(type.getModifiedBy());
		
		Cleaner<CriteriaSection> sectionCleaner = EasyMock.createMock(Cleaner.class);
		sectionCleaner.clean(sections.get(0));
		sectionCleaner.clean(sections.get(2));
		EasyMock.replay(sectionCleaner);
		
		EventTypeCleaner cleaner = new EventTypeCleaner(newTenant, sectionCleaner);
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
		
		assertEquals(2, type.getEventForm().getSections().size());
		
		EasyMock.verify(sectionCleaner);
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
		type.getEventForm().setSections(sections);
        type.setModifiedBy(UserBuilder.aUser().build());
		return type;
	}
	
}
