package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.reporting.EventReportType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="eventtypegroups")
@Localized
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EventTypeGroup extends ArchivableEntityWithTenant implements NamedEntity, Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private @Localized String name;

	@Column(nullable=false)
	private String reportTitle;

    @ManyToOne(fetch=FetchType.EAGER)
	private PrintOut printOut;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private PrintOut observationPrintOut;
	
	public EventTypeGroup() {}
	
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		name = (name != null) ? name.trim() : null;
	}
	
	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		return getName();
	}

	@AllowSafetyNetworkAccess
	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	
	/*
	 * Retuns the name to be used on the filesystem. (converts to lower case and replaces spaces with _'s
	 */
	@AllowSafetyNetworkAccess
	public String getFileSystemName() {
		return name.trim().toLowerCase().replaceAll( "\\s", "_");
	}

	@AllowSafetyNetworkAccess
	public PrintOut getPrintOut() {
		return printOut;
	}

	public void setPrintOut(PrintOut printOut) {
		this.printOut = printOut;
	}
	
	@AllowSafetyNetworkAccess
	public boolean hasPrintOut() {
		return (printOut != null);
	}
	
	@AllowSafetyNetworkAccess
	public boolean hasObservationPrintOut() {
		return (observationPrintOut != null);
	}

	@AllowSafetyNetworkAccess
	public PrintOut getObservationPrintOut() {
		return observationPrintOut;
	}

	public void setObservationPrintOut(PrintOut observationPrintOut) {
		this.observationPrintOut = observationPrintOut;
	}
	
	public PrintOut getPrintOutForReportType(EventReportType reportType) {
		switch (reportType) {
			case INSPECTION_CERT:
				return printOut;
			case OBSERVATION_CERT:
				return observationPrintOut;
			default:
				throw new IllegalArgumentException("Unknown InspectionReportType " + reportType.name());
		}
	}
	
	@Override
	public String toString() {
		return name;
	}

}
