package com.n4systems.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "inspectiongroups")
public class InspectionGroup extends EntityWithTenant implements SecurityEnhanced<InspectionGroup> {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "group")
	private Set<Inspection> inspections = new TreeSet<Inspection>();

	private String mobileGuid;

	public InspectionGroup() {
	}

	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Set<Inspection> getInspections() {
		return inspections;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getMobileGuid() {
		return mobileGuid;
	}

	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Date getFirstDate() {
		Date minDate = null;
		for (Inspection inspection : getAvailableInspections()) {
			if (minDate == null) {
				minDate = inspection.getDate();
			} else if (inspection.getDate().before(minDate)) {
				minDate = inspection.getDate();
			}
		}

		return minDate;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Date getLastDate() {
		Date maxDate = null;
		for (Inspection inspection : getAvailableInspections()) {
			if (maxDate == null) {
				maxDate = inspection.getDate();
			} else if (inspection.getDate().after(maxDate)) {
				maxDate = inspection.getDate();
			}
		}

		return maxDate;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public List<Inspection> getAvailableInspections() {
		List<Inspection> availableInspections = new ArrayList<Inspection>();

		for (Inspection inspection : inspections) {
			if (inspection.isActive()) {
				availableInspections.add(inspection);
			}

		}
		return availableInspections;
	}

	public InspectionGroup enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
}
