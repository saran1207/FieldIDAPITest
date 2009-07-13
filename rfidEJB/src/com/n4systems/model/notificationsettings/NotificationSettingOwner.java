package com.n4systems.model.notificationsettings;

import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.JobSite;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithCustomerDivision;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
	name="notificationsettings_owner", 
	uniqueConstraints={
		@UniqueConstraint(columnNames={"notificationsettings_id", "customer_id", "division_id"}),
		@UniqueConstraint(columnNames={"notificationsettings_id", "jobsite_id"})
	}
)
public class NotificationSettingOwner implements Serializable, EntityWithCustomerDivision, Saveable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "notificationsettings_id", nullable=false)
	private NotificationSetting notificationSetting;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "division_id")
	private Division division;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "jobsite_id")
	private JobSite jobSite;	
	
	public NotificationSettingOwner() {}
	
	public boolean isNew() {
	    return (id == null);
    }
	
	public Long getId() {
    	return id;
    }

	public void setId(Long id) {
    	this.id = id;
    }

	public Long getID() {
    	return id;
    }

	public void setID(Long id) {
    	this.id = id;
    }
	
	public NotificationSetting getNotificationSetting() {
    	return notificationSetting;
    }

	public void setNotificationSetting(NotificationSetting notificationSetting) {
    	this.notificationSetting = notificationSetting;
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
	
}
