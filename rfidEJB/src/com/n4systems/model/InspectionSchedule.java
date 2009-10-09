package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.n4systems.exceptions.InvalidScheduleStateException;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;

@Entity
@Table(name = "inspectionschedules")
public class InspectionSchedule extends ArchivableEntityWithOwner implements NetworkEntity<InspectionSchedule> {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "product.owner", null, "state");
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
	private Product product;

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

	private String location;

	@ManyToOne()
	private Project project;

	public InspectionSchedule() {
	}

	public InspectionSchedule(Product product, InspectionType inspectionType) {
		this(product, inspectionType, null);
	}

	public InspectionSchedule(Product product, InspectionType inspectionType, Date scheduledDate) {
		this.setTenant(product.getTenant());
		setProduct(product);
		this.inspectionType = inspectionType;
		nextDate = scheduledDate;
	}

	public InspectionSchedule(Inspection inspection) {
		this(inspection.getProduct(), inspection.getType());
		nextDate = inspection.getDate();
		this.completed(inspection);
	}

	public InspectionSchedule(Product product, ProductTypeSchedule typeSchedule) {
		this(product, typeSchedule.getInspectionType());
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (status != ScheduleStatus.COMPLETED) {
			updateOwnershipToProduct();
		}
	}

	private void updateOwnershipToProduct() {
		setLocation(product.getLocation());
		setOwner(product.getOwner());
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
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

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public PlainDate getNextDate() {
		return (nextDate != null) ? new PlainDate(nextDate) : null;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
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
	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public static boolean isPastDue(Date nextInspectionDate) {
		return DateHelper.getToday().after(nextInspectionDate);
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Long getDaysPastDue() {
		Long daysPast = null;
		if (isPastDue()) {
			daysPast = DateHelper.getDaysUntilToday(nextDate);
		}
		return daysPast;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
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
		if (getId() == null)
			return false;

		return getId().equals(schedule.getId());

	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Date getCompletedDate() {
		return completedDate;
	}

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public ScheduleStatus getStatus() {
		return status;
	}

	public void completed(Inspection inspection) {
		if (status == ScheduleStatus.COMPLETED) {
			throw new InvalidScheduleStateException();
		}
		this.inspection = inspection;
		completedDate = new Date();
		status = ScheduleStatus.COMPLETED;
		location = inspection.getLocation();
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

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public Inspection getInspection() {
		return inspection;
	}

	public void removeInspection() {
		status = ScheduleStatus.SCHEDULED;
		inspection = null;
		completedDate = null;
		updateOwnershipToProduct();
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SafetyNetworkSecurityCache.getSecurityLevel(fromOrg, getOwner());
	}
	
	public InspectionSchedule enhance(SecurityLevel level) {
		InspectionSchedule enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setProduct(enhance(product, level));
		enhanced.setInspectionType(enhance(inspectionType, level));
		enhanced.inspection = enhance(inspection, level);
		return enhanced;
	}

}
