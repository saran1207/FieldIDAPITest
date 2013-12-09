package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventtypes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EventType extends ArchivableEntityWithTenant implements NamedEntity, Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;
	public static final long DEFAULT_FORM_VERSION = 1;
	
	@Column(nullable=false)
	private @Localized String name;
	
	private String archivedName;
	
	private String description;
	
	@Column(nullable=false)
	private boolean printable;
	
	@Column(nullable=false)
	private boolean retired = false;

	@Column(nullable=false)
	private boolean assignedToAvailable = false;
	
	@ManyToOne(cascade={CascadeType.REFRESH}, optional=false)
	private EventTypeGroup group;

    @OneToOne(cascade = {CascadeType.MERGE}, orphanRemoval = false)
    @JoinColumn(name="eventform_id")
    private EventForm eventForm;

	@ElementCollection(fetch= FetchType.LAZY)
	@IndexColumn(name="orderidx")
    @JoinTable(name="eventtypes_infofieldnames", joinColumns = {@JoinColumn(name="eventtypes_id")})
    @Column(name="element")
	private List<String> infoFieldNames = new ArrayList<String>();
	
	@Column(nullable=false)
	private long formVersion = DEFAULT_FORM_VERSION;

    @Column(nullable=false, name="display_section_totals")
    private boolean displaySectionTotals;

    @Column(nullable=false, name="display_score_percentage")
    private boolean displayScorePercentage;

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
		}
	}

	public String getArchivedName() {
		return archivedName;
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

    @AllowSafetyNetworkAccess
    public EventForm getEventForm() {
        return eventForm;
    }

    public void setEventForm(EventForm eventForm) {
        this.eventForm = eventForm;
    }

    public boolean isDisplaySectionTotals() {
        return displaySectionTotals;
    }

    public void setDisplaySectionTotals(boolean displaySectionTotals) {
        this.displaySectionTotals = displaySectionTotals;
    }

    public boolean isDisplayScorePercentage() {
        return displayScorePercentage;
    }

    public void setDisplayScorePercentage(boolean displayScorePercentage) {
        this.displayScorePercentage = displayScorePercentage;
    }

    public boolean isThingEventType() {
        return false;
    }

    public boolean isPlaceEventType() {
        return false;
    }

    public boolean isActionEventType() {
        return false;
    }

}
