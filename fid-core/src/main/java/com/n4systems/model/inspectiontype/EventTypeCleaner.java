package com.n4systems.model.inspectiontype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.api.EntityWithTenantCleaner;
import com.n4systems.model.criteriasection.CriteriaSectionCleaner;
import com.n4systems.util.ListHelper;

public class EventTypeCleaner extends EntityWithTenantCleaner<EventType> {
	private final Cleaner<CriteriaSection> sectionCleaner;
	
	public EventTypeCleaner(Tenant newTenant, Cleaner<CriteriaSection> sectionCleaner) {
		super(newTenant);
		this.sectionCleaner = sectionCleaner;
	}
	
	public EventTypeCleaner(Tenant newTenant) {
		this(newTenant, new CriteriaSectionCleaner(newTenant));
	}
	
	@Override
	public void clean(EventType type) {
		super.clean(type);

		type.setFormVersion(EventType.DEFAULT_FORM_VERSION);
		
		cleanSections(type);
		cleanSupportedProofTests(type);
		cleanInfoFieldNames(type);
	}

	private void cleanSections(EventType type) {
		// we want to create a new list of sections, rather then removing old ones to avoid ConcurrentModification while we iterate
		List<CriteriaSection> cleanedSections = new ArrayList<CriteriaSection>();
		for (CriteriaSection section: type.getSections()) {
			// there's no need to copy the retired criteria
			if (!section.isRetired()) {
				sectionCleaner.clean(section);
				
				cleanedSections.add(section);
			}
		}
		
		type.setSections(cleanedSections);
	}
	
	private void cleanSupportedProofTests(EventType type) {
		// Note: this is actually required so that hibernate does not move the old list to the new entity
		// when it's being copied. -mf
		type.setSupportedProofTests(ListHelper.copy(type.getSupportedProofTests(), new HashSet<ProofTestType>()));
	}
	
	private void cleanInfoFieldNames(EventType type) {
		// Note: this is actually required so that hibernate does not move the old list to the new entity
		// when it's being copied. -mf
		type.setInfoFieldNames(ListHelper.copy(type.getInfoFieldNames(), new ArrayList<String>()));
	}
}
