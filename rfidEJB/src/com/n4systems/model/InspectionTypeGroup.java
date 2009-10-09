package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name="inspectiontypegroups")
public class InspectionTypeGroup extends EntityWithTenant implements NamedEntity, Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String reportTitle;

	@ManyToOne(fetch=FetchType.EAGER)
	private PrintOut printOut;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private PrintOut observationPrintOut;
	
	public InspectionTypeGroup() {}
	
	
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
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getDisplayName() {
		return getName();
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	
	/*
	 * Retuns the name to be used on the filesystem. (converts to lower case and replaces spaces with _'s
	 */
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public String getFileSystemName() {
		return name.trim().toLowerCase().replaceAll( "\\s", "_");
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public PrintOut getPrintOut() {
		return printOut;
	}

	public void setPrintOut(PrintOut printOut) {
		this.printOut = printOut;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean hasPrintOut() {
		return (printOut != null);
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public boolean hasObservationPrintOut() {
		return (observationPrintOut != null);
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public PrintOut getObservationPrintOut() {
		return observationPrintOut;
	}

	public void setObservationPrintOut(PrintOut observationPrintOut) {
		this.observationPrintOut = observationPrintOut;
	}
}
