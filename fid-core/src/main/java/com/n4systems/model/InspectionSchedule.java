package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.exceptions.InvalidScheduleStateException;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

@Entity
@Table(name = "inspectionschedules")
public class InspectionSchedule extends ArchivableEntityWithOwner implements NetworkEntity<InspectionSchedule> {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "asset.owner", null, "state");
	}

	public enum ScheduleStatus implements DisplayEnum {
		SCHEDULED("label.scheduled"), IN_PROGRESS("label.inprogress"), COMPLETED("label.completed");

		private String label;

		private ScheduleStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public String getName() {
			return name();
		}
	}

	public enum ScheduleStatusGrouping {
		NON_COMPLETE(ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS), COMPLETE(ScheduleStatus.COMPLETED);

		private ScheduleStatus[] members;

		private ScheduleStatusGrouping(ScheduleStatus... members) {
			this.members = members;
		}

		public ScheduleStatus[] getMembers() {
			return members;
		}

	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="product_id")
	private Asset asset;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private InspectionType inspectionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date nextDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;

	@Enumerated(EnumType.STRING)
	private ScheduleStatus status = ScheduleStatus.SCHEDULED;

	@OneToOne
	private Inspection inspection;

	private Location advancedLocation = new Location();

	@ManyToOne()
	private Project project;
	
	private String mobileGUID;

	public InspectionSchedule() {
	}

	public InspectionSchedule(Asset asset, InspectionType inspectionType) {
		this(asset, inspectionType, null);
	}

	public InspectionSchedule(Asset asset, InspectionType inspectionType, Date scheduledDate) {
		this.setTenant(asset.getTenant());
		this.setAsset(asset);
		this.inspectionType = inspectionType;
		this.nextDate = scheduledDate;
	}

	public InspectionSchedule(Inspection inspection) {
		this(inspection.getAsset(), inspection.getType());
		nextDate = inspection.getDate();
		this.completed(inspection);
	}

	public InspectionSchedule(Asset asset, AssetTypeSchedule typeSchedule) {
		this(asset, typeSchedule.getInspectionType());
	}

	@AllowSafetyNetworkAccess
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
		if (status != ScheduleStatus.COMPLETED) {
			updateOwnershipToAsset();
		}
	}

	private void updateOwnershipToAsset() {
		setAdvancedLocation(asset.getAdvancedLocation());
		setOwner(asset.getOwner());
	}

	@AllowSafetyNetworkAccess
	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
	}

	public void setNextDate(PlainDate nextDate) {
		this.nextDate = nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = new PlainDate(nextDate);
	}

	@AllowSafetyNetworkAccess
	public PlainDate getNextDate() {
		return (nextDate != null) ? new PlainDate(nextDate) : null;
	}

	@AllowSafetyNetworkAccess
	public boolean isPastDue() {
		return (status != ScheduleStatus.COMPLETED && isPastDue(nextDate));
	}

	/**
	 * A static method consolidating the logic for checking if a next inspection
	 * date is past due.
	 * 
	 * @param nextInspectionDate
	 *            The next inspection date
	 * @return True if nextInspectionDate is after {@link DateHelper#getToday()
	 *         today}
	 */
	@AllowSafetyNetworkAccess
	public static boolean isPastDue(Date nextInspectionDate) {
		return DateHelper.getToday().after(nextInspectionDate);
	}

	@AllowSafetyNetworkAccess
	public Long getDaysPastDue() {
		Long daysPast = null;
		if (isPastDue()) {
			daysPast = DateHelper.getDaysUntilToday(nextDate);
		}
		return daysPast;
	}

	@AllowSafetyNetworkAccess
	public Long getDaysToDue() {
		Long daysTo = null;
		if (!isPastDue()) {
			daysTo = DateHelper.getDaysFromToday(nextDate);
		}
		return daysTo;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof InspectionSchedule) {
			return this.equals((InspectionSchedule) obj);
		} else {
			return super.equals(obj);
		}

	}

	public boolean equals(InspectionSchedule schedule) {

		if (schedule == null)
			return false;
		if (getId() == null) {
			return false; //asset.equals(schedule.asset) && inspectionType.equals(schedule.inspectionType) && nextDate.equals(schedule.nextDate);
		}
			

		return getId().equals(schedule.getId());

	}

	@AllowSafetyNetworkAccess
	public Date getCompletedDate() {
		return completedDate;
	}

	@AllowSafetyNetworkAccess
	public ScheduleStatus getStatus() {
		return status;
	}

	public void completed(Inspection inspection) throws InvalidScheduleStateException {
		if (status == ScheduleStatus.COMPLETED) {
			throw new InvalidScheduleStateException();
		}
		this.inspection = inspection;
		completedDate = new Date();
		status = ScheduleStatus.COMPLETED;
		advancedLocation = inspection.getAdvancedLocation();
		setOwner(inspection.getOwner());
	}

	public void inProgress() {
		if (status == ScheduleStatus.COMPLETED) {
			throw new InvalidScheduleStateException();
		}
		status = ScheduleStatus.IN_PROGRESS;
	}

	public void stopProgress() {
		if (status == ScheduleStatus.COMPLETED) {
			throw new InvalidScheduleStateException();
		}
		status = ScheduleStatus.SCHEDULED;
	}

	@AllowSafetyNetworkAccess
	public Inspection getInspection() {
		return inspection;
	}

	public void removeInspection() {
		status = ScheduleStatus.SCHEDULED;
		inspection = null;
		completedDate = null;
		updateOwnershipToAsset();
	}

	@AllowSafetyNetworkAccess
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
	}
	
	public InspectionSchedule enhance(SecurityLevel level) {
		InspectionSchedule enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setAsset(enhance(asset, level));
		enhanced.setInspectionType(enhance(inspectionType, level));
		enhanced.inspection = enhance(inspection, level);
		return enhanced;
	}
	
	public String getMobileGUID() {
		return mobileGUID;
	}
	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	
	@AllowSafetyNetworkAccess
	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	public void setAdvancedLocation(Location advancedLocation) {
		this.advancedLocation = advancedLocation;
	}
	

}
