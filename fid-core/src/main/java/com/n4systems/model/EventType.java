package com.n4systems.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.security.AllowSafetyNetworkAccess;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "eventtypes")
public class EventType extends ArchivableEntityWithTenant implements NamedEntity, Listable<Long>, Saveable, SecurityEnhanced<EventType> {
	private static final long serialVersionUID = 1L;
	public static final long DEFAULT_FORM_VERSION = 1;
	
	@Column(nullable=false)
	private String name;
	
	private String archivedName;
	
	private String description;
	
	@Column(nullable=false)
	private boolean printable;
	
	@Column(nullable=false)
	private boolean retired = false;

	@Column(nullable=false)
	private boolean master = false;
	
	@Column(nullable=false)
	private boolean assignedToAvailable = false;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, optional=false)
	private EventTypeGroup group;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@IndexColumn(name="orderidx")
    @JoinTable(name="eventtypes_criteriasections", joinColumns = {@JoinColumn(name="eventtypes_id")})
	private List<CriteriaSection> sections = new ArrayList<CriteriaSection>();
	
	@CollectionOfElements(fetch= FetchType.LAZY)
	@Enumerated(EnumType.STRING)
    @JoinTable(name="eventtypes_supportedprooftests", joinColumns = {@JoinColumn(name="eventtypes_id")})
	private Set<ProofTestType> supportedProofTests = new HashSet<ProofTestType>();
	
	@CollectionOfElements(fetch= FetchType.LAZY)
	@IndexColumn(name="orderidx")
    @JoinTable(name="eventtypes_infofieldnames", joinColumns = {@JoinColumn(name="eventtypes_id")})
	private List<String> infoFieldNames = new ArrayList<String>();
	
	@Column(nullable=false)
	private long formVersion = DEFAULT_FORM_VERSION;
	
	private Long legacyEventId;

	public EventType() {
		this(null);
	}

	public EventType(String name) {
		super();
		this.name = name;
	}
	
	protected void onCreate() {
		super.onCreate();
		trimName();
	}

	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
	}
	
	protected void onUpdate() {
		super.onUpdate();
		trimName();
		archiveName();
	}
	
	@Override
    public String toString() {
	    return name + " (" + getId() +")";
    }

	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return getName();
	}
	
	public void incrementFormVersion() {
		formVersion++;
	}
	
	/**
	 * Finds the CriteriaSections for a given Criteria. 
	 * @param criteria	Criteria
	 * @return			The CriteriaSection containing the Criteria or null if no section was found
	 */
	public CriteriaSection findSection(Criteria criteria) {
		CriteriaSection criteriaSection = null;
		for(CriteriaSection section: sections) {
			if(section.getCriteria().contains(criteria)) {
				criteriaSection = section;
				break;
			}
		}
		return criteriaSection;
	}
	
	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@AllowSafetyNetworkAccess
	public EventTypeGroup getGroup() {
		return group;
	}

	public void setGroup(EventTypeGroup group) {
		this.group = group;
	}

	@AllowSafetyNetworkAccess
	public List<String> getInfoFieldNames() {
		return infoFieldNames;
	}

	public void setInfoFieldNames(List<String> infoFieldNames) {
		this.infoFieldNames = infoFieldNames;
	}

	@AllowSafetyNetworkAccess
	public List<CriteriaSection> getSections() {
		return sections;
	}

	public void setSections(List<CriteriaSection> sections) {
		this.sections = sections;
	}
	
	@AllowSafetyNetworkAccess
	public Set<ProofTestType> getSupportedProofTests() {
		return supportedProofTests;
	}

	public void setSupportedProofTests(Set<ProofTestType> supportedProofTests) {
		this.supportedProofTests = supportedProofTests;
	}
	
	@AllowSafetyNetworkAccess
	public boolean supports(ProofTestType proofTestType) {
		return supportedProofTests.contains(proofTestType);
	}
	
	@AllowSafetyNetworkAccess
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@AllowSafetyNetworkAccess
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}
	
	@AllowSafetyNetworkAccess
	public Long getLegacyEventId() {
		return legacyEventId;
	}

	public void setLegacyEventId(Long legacyEventId) {
		this.legacyEventId = legacyEventId;
	}
	
	@AllowSafetyNetworkAccess
	public long getFormVersion() {
    	return formVersion;
    }

	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }
	
	private void archiveName() {
		if (isArchived() && (archivedName == null || archivedName.length() == 0)) {
			archivedName = name;
			name = UUID.randomUUID().toString();
		}
	}

	public String getArchivedName() {
		return archivedName;
	}

	public EventType enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}

	public boolean isAssignedToAvailable() {
		return assignedToAvailable;
	}
	
	public void makeAssignedToAvailable() {
		assignedToAvailable = true;
	}
	
	public void removeAssignedTo() {
		assignedToAvailable = false;
	}
}
