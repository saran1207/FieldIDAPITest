package com.n4systems.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "inspectiongroups")
public class InspectionGroup extends EntityWithTenant {
	private static final long serialVersionUID = 1L;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.MERGE, mappedBy="group")
	private Set<Inspection> inspections = new TreeSet<Inspection>();
	
	private String mobileGuid;

	public InspectionGroup() {}
	
	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}

	public Set<Inspection> getInspections() {
		return inspections;
	}
	
	public String getMobileGuid() {
		return mobileGuid;
	}

	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}

	public List<Inspection> getFilteredInspections( SecurityFilter filter ) {
		List<Inspection> filteredInspections = new ArrayList<Inspection>();
		if( !filter.isCustomerSet() && !filter.isDivisionSet()  ) {
			filteredInspections.addAll( getAvailableInspections() );
			return filteredInspections;
		}
		
		for( Inspection inspection : getAvailableInspections() ) {
			if( filter.isCustomerSet() && inspection.getCustomer() != null && 
					filter.getCustomerId().equals( inspection.getCustomer().getId() ) ) {
				// only
				if( filter.isDivisionSet() && ( inspection.getDivision() == null || 
						!filter.getDivisionId().equals( inspection.getDivision().getId() ) ) ) {
					continue;
				}
				filteredInspections.add( inspection );
			}	
		}
		
		
		return filteredInspections;
	}
	
	public Date getFirstDate() {
		Date minDate = null;
		for( Inspection inspection : getAvailableInspections() ) {
			if( minDate == null ) {
				minDate = inspection.getDate();
			} else if( inspection.getDate().before( minDate ) ) {
				minDate = inspection.getDate();
			}
		}
		
		return minDate;
	}
	
	public Date getLastDate() {
		Date maxDate = null;
		for( Inspection inspection : getAvailableInspections() ) {
			if( maxDate == null ) {
				maxDate = inspection.getDate();
			} else if( inspection.getDate().after( maxDate ) ) {
				maxDate = inspection.getDate();
			}
		}
		
		return maxDate;
	}
	
	
	
	public Set<Inspection> getAvailableInspections() {
		Set<Inspection> availableInspections = new HashSet<Inspection>();
		
		for( Inspection inspection : inspections ) {
			if( inspection.isActive() ) {
				availableInspections.add( inspection );
			}
			
		}
		return availableInspections;
	}
}
