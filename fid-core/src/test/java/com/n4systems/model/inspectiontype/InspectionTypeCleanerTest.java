package com.n4systems.model.inspectiontype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.criteriasection.CriteriaSectionBuilder;

public class InspectionTypeCleanerTest {
	private static final String name = "type_name";	
	private static final String description = "type_desc";
	private static final boolean printable = true;
	private static final boolean retired = false;
	private static final boolean master = false;
	private static final long formVersion = InspectionType.DEFAULT_FORM_VERSION;
	private static final InspectionTypeGroup group = new InspectionTypeGroup();
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
		InspectionType type = buildType();
		Tenant newTenant = TenantBuilder.aTenant().build();
		
		assertNotNull(type.getId());
		assertNotNull(type.getCreated());
		assertNotNull(type.getModified());
		assertNotNull(type.getModifiedBy());
		assertNotNull(type.getTenant());
		
		Cleaner<CriteriaSection> sectionCleaner = EasyMock.createMock(Cleaner.class);
		sectionCleaner.clean(sections.get(0));
		sectionCleaner.clean(sections.get(2));
		EasyMock.replay(sectionCleaner);
		
		InspectionTypeCleaner cleaner = new InspectionTypeCleaner(newTenant, sectionCleaner);
		cleaner.clean(type);
		
		assertNull(type.getId());
		assertNull(type.getCreated());
		assertNull(type.getModified());
		assertNull(type.getModifiedBy());
		
		assertSame(newTenant, type.getTenant());
		assertSame(group, type.getGroup());
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
		
		assertEquals(2, type.getSections().size());
		
		EasyMock.verify(sectionCleaner);
	}
	
	private InspectionType buildType() {
		InspectionType type = InspectionTypeBuilder.anInspectionType().build();
		type.setName(name);
		type.setDescription(description);
		type.setPrintable(printable);
		type.setRetired(retired);
		type.setMaster(master);
		type.setFormVersion(formVersion);
		type.setGroup(group);
		type.setSupportedProofTests(supportedProofTests);
		type.setInfoFieldNames(infoFieldNames);
		type.setSections(sections);
		return type;
	}
	
}
