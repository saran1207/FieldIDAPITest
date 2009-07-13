package com.n4systems.model.utils;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.TenantOrganization;

public class CleanInspectionTypeFactory {
	
	private InspectionType originalType;
	private TenantOrganization targetTenant;
	
	public CleanInspectionTypeFactory(InspectionType originalType, TenantOrganization targetTenant) {
		super();
		this.originalType = originalType;
		this.targetTenant = targetTenant;
	}
	
	
	public InspectionType clean() {
		originalType.setId(null);
		originalType.setCreated(null);
		originalType.setModified(null);
		originalType.setModifiedBy(null);
		originalType.setFormVersion(InspectionType.DEFAULT_FORM_VERSION);
		originalType.setLegacyEventId(null);
		cleanSections();			
		originalType.setTenant(targetTenant);
		return originalType;
	}
	
	public void cleanSections() {
		List<CriteriaSection> onlyAvailableCriteriaSections = new ArrayList<CriteriaSection>();
		
		for (CriteriaSection section : originalType.getSections()) {
			if (!section.isRetired()) {
				section.setId(null);
				section.setCreated(null);
				section.setModified(null);
				section.setModifiedBy(null);
				section.setTenant(targetTenant);
				cleanCriteria(section);
				onlyAvailableCriteriaSections.add(section);
			}
		}
		originalType.setSections(onlyAvailableCriteriaSections);
		
	}

	private void cleanCriteria(CriteriaSection section) {
		List<Criteria> availableCriteria = new ArrayList<Criteria>();
		for (Criteria criteria : section.getCriteria()) {
			if (!criteria.isRetired()) {
				criteria.setId(null);
				criteria.setCreated(null);
				criteria.setModified(null);
				criteria.setModifiedBy(null);
				criteria.setTenant(targetTenant);
				availableCriteria.add(criteria);
			}
		}
		
		section.setCriteria(availableCriteria);
	}
	
	
}
