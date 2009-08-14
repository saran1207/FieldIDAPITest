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
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "inspectionschedules")
public class InspectionSchedule extends EntityWithTenant implements Archivable, FilteredEntity {
	private static final long serialVersionUID = 1L;
	
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
	
	public enum ScheduleStatusGrouping  {
		NON_COMPLETE(ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS), COMPLETE(ScheduleStatus.COMPLETED);
		
		private ScheduleStatus[] members;
		
		private ScheduleStatusGrouping(ScheduleStatus...members) {
			this.members = members;
		}

		public ScheduleStatus[] getMembers() {
			return members;
		}
		
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	private InspectionType inspectionType;
	
 	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date nextDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;
	
	@Enumerated(EnumType.STRING)
	private ScheduleStatus status = ScheduleStatus.SCHEDULED;
	
	@OneToOne
	private Inspection inspection;
	
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Customer customer;

	@ManyToOne(fetch=FetchType.EAGER)
	private Division division;

	@ManyToOne(optional=true)
	private JobSite jobSite;
	
	private String location;
	
	@ManyToOne()
	private Project project;
	
	
	public InspectionSchedule() {}
	
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
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, "customer.id", "division.id", null, "state");
	}
	
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
		jobSite = this.product.getJobSite();
		customer = this.product.getOwner();
		division = this.product.getDivision();
		location = this.product.getLocation();
	}
	
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
	
	public PlainDate getNextDate() {
		return (nextDate != null) ? new PlainDate(nextDate) : null;
	}

	public boolean isPastDue() {
		return (status != ScheduleStatus.COMPLETED && isPastDue(nextDate));
	}
	
	/**
	 * A static method consolidating the logic for checking if a
	 * next inspection date is past due.
	 * @param nextInspectionDate	The next inspection date
	 * @return						True if nextInspectionDate is after {@link DateHelper#getToday() today}
	 */
	public static boolean isPastDue(Date nextInspectionDate) {
		return  DateHelper.getToday().after(nextInspectionDate);
	}
	
	public Long getDaysPastDue() {
		Long daysPast = null;
		if(isPastDue()) {
			daysPast = DateHelper.getDaysUntilToday(nextDate);
		}
		return daysPast;
	}
	
	public Long getDaysToDue() {
		Long daysTo = null;
		if(!isPastDue()) {
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

	public Date getCompletedDate() {
		return completedDate;
	}

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
		jobSite = inspection.getJobSite();
		customer = inspection.getCustomer();
		division = inspection.getDivision();
		location = inspection.getLocation();
		
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

	public Inspection getInspection() {
		return inspection;
	}

	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

	public EntityState getEntityState() {
		return state;
	}

	public void retireEntity() {
		state = EntityState.RETIRED;
	}
	
	public void setRetired( boolean retired ) {
		if( retired ) {
			retireEntity();
		} else  {
			activateEntity();
		}
	}
	
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	public void removeInspection() {
		status = ScheduleStatus.SCHEDULED;
		inspection = null;
		completedDate = null;
		updateOwnershipToProduct();
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public JobSite getJobSite() {
		return jobSite;
	}

	public void setJobSite(JobSite jobSite) {
		this.jobSite = jobSite;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
