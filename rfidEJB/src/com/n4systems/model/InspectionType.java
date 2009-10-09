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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "inspectiontypes")
public class InspectionType extends ArchivableEntityWithTenant implements NamedEntity, Listable<Long>, Saveable, SecurityEnhanced<InspectionType> {
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
	
	@ManyToOne(cascade={CascadeType.REFRESH}, optional=false)
	private InspectionTypeGroup group;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@IndexColumn(name="orderidx")
	private List<CriteriaSection> sections = new ArrayList<CriteriaSection>();
	
	@CollectionOfElements(fetch= FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private Set<ProofTestType> supportedProofTests = new HashSet<ProofTestType>();
	
	@CollectionOfElements(fetch= FetchType.LAZY)
	@IndexColumn(name="orderidx")
	private List<String> infoFieldNames = new ArrayList<String>();
	
	@Column(nullable=false)
	private long formVersion = DEFAULT_FORM_VERSION;
	
	private Long legacyEventId;

	public InspectionType() {
		this(null);
	}

	public InspectionType(String name) {
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

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
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
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public InspectionTypeGroup getGroup() {
		return group;
	}

	public void setGroup(InspectionTypeGroup group) {
		this.group = group;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public List<String> getInfoFieldNames() {
		return infoFieldNames;
	}

	public void setInfoFieldNames(List<String> infoFieldNames) {
		this.infoFieldNames = infoFieldNames;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public List<CriteriaSection> getSections() {
		return sections;
	}

	public void setSections(List<CriteriaSection> sections) {
		this.sections = sections;
	}
	
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public Set<ProofTestType> getSupportedProofTests() {
		return supportedProofTests;
	}

	public void setSupportedProofTests(Set<ProofTestType> supportedProofTests) {
		this.supportedProofTests = supportedProofTests;
	}
	
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public boolean supports(ProofTestType proofTestType) {
		return supportedProofTests.contains(proofTestType);
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Long getLegacyEventId() {
		return legacyEventId;
	}

	public void setLegacyEventId(Long legacyEventId) {
		this.legacyEventId = legacyEventId;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public long getFormVersion() {
    	return formVersion;
    }

	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
    }
	
	private void archiveName() {
		if (isArchived() && archivedName == null) {
			archivedName = name;
			name = UUID.randomUUID().toString();
		}
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getArchivedName() {
		return archivedName;
	}

	public InspectionType enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
	
}
