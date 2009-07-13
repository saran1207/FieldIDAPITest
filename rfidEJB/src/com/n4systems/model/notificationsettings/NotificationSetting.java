package com.n4systems.model.notificationsettings;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

import rfid.ejb.entity.UserBean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "notificationsettings")
public class NotificationSetting extends EntityWithTenant implements FilteredEntity, Saveable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String name;
	
	@Column(name="usingjobsite", nullable=false)
	private boolean usingJobSite;
	
	@Column(name="frequency", nullable=false)
	@Enumerated(EnumType.STRING)
	private SimpleFrequency frequency = SimpleFrequency.DAILY;
	
	@Column(name="periodstart", nullable=false)
	@Enumerated(EnumType.STRING)
	private RelativeTime periodStart = RelativeTime.TODAY;
	
	@Column(name="periodend", nullable=false)
	@Enumerated(EnumType.STRING)
	private RelativeTime periodEnd = RelativeTime.NEXT_WEEK;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private UserBean user;
	
	@Column(name="addr", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
    private List<String> addresses = new ArrayList<String>();

	@Column(name="producttype_id", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<Long> productTypes = new ArrayList<Long>();
	
	@Column(name="inspectiontype_id", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<Long> inspectionTypes = new ArrayList<Long>();

	public NotificationSetting() {}

	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, null, null, "user.uniqueID", null);
	}

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public boolean isUsingJobSite() {
    	return usingJobSite;
    }

	public void setUsingJobSite(boolean usingJobSite) {
    	this.usingJobSite = usingJobSite;
    }

	public SimpleFrequency getFrequency() {
    	return frequency;
    }

	public void setFrequency(SimpleFrequency frequency) {
    	this.frequency = frequency;
    }

	public RelativeTime getPeriodStart() {
    	return periodStart;
    }

	public void setPeriodStart(RelativeTime periodStart) {
    	this.periodStart = periodStart;
    }

	public RelativeTime getPeriodEnd() {
    	return periodEnd;
    }

	public void setPeriodEnd(RelativeTime periodEnd) {
    	this.periodEnd = periodEnd;
    }

	public UserBean getUser() {
    	return user;
    }

	public void setUser(UserBean user) {
    	this.user = user;
    }

	public List<String> getAddresses() {
    	return addresses;
    }

	public void setAddresses(List<String> addresses) {
    	this.addresses = addresses;
    }
    
	public List<Long> getProductTypes() {
    	return productTypes;
    }

	public void setProductTypes(List<Long> productTypes) {
    	this.productTypes = productTypes;
    }

	public List<Long> getInspectionTypes() {
    	return inspectionTypes;
    }

	public void setInspectionTypes(List<Long> inspectionTypes) {
    	this.inspectionTypes = inspectionTypes;
    }
}
