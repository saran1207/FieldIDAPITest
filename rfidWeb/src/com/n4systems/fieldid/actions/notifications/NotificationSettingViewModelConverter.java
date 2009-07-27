package com.n4systems.fieldid.actions.notifications;

import java.util.Date;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.JobSite;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingOwner;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.SecurityFilter;

public class NotificationSettingViewModelConverter {
	
	private final PersistenceManager pm;
	private final TenantOrganization tenant;
	private final UserBean user;
	private final SecurityFilter filter;
	private final boolean usingJobSites;
	
	public NotificationSettingViewModelConverter(PersistenceManager pm, TenantOrganization tenant, UserBean user, SecurityFilter filter) {
		this.pm = pm;
		this.tenant = tenant;
		this.user = user;
		this.filter = filter;
		this.usingJobSites = tenant.hasExtendedFeature(ExtendedFeature.JobSites);
	}
	
	/**
	 * Compresses a NotificationSetting and NotificationSettingOwner into a single NotificationSettingView while
	 * converting entities and enums into id's
	 */
	public void populateView(NotificationSetting model, NotificationSettingOwner owner, NotificationSettingView view) {
		
		// get fields from the model object
		view.setId(model.getId());
		view.setName(model.getName());
		
		view.setFrequency(model.getFrequency().getId());
		view.setPeriodStart(model.getPeriodStart().getId());
		view.setPeriodEnd(model.getPeriodEnd().getId());
		
		// we'll need the created date to carry through on edit
		if (model.getCreated() != null) {
			view.setCreatedTimeStamp(model.getCreated().getTime());
		}
		
		if (!model.getProductTypes().isEmpty()) {
			// we only support a single product type right now
			view.setProductTypeId(model.getProductTypes().get(0));
		}
		
		if (!model.getInspectionTypes().isEmpty()) {
			// we only support a single inspection type right now
			view.setInspectionTypeId(model.getInspectionTypes().get(0));
		}
		
		view.getAddresses().addAll(model.getAddresses());
		
		// get fields from the owner object
		view.setOwnerId(owner.getId());
		
		if (usingJobSites) {
			if (owner.getJobSite() != null) {
				view.setJobSiteId(owner.getJobSite().getId());
			}
			view.setCustomerId(null);
			view.setDivisionId(null);
		} else {
			if (owner.getCustomer() != null) {
				view.setCustomerId(owner.getCustomer().getId());
				
				if (owner.getDivision() != null) {
					view.setDivisionId(owner.getDivision().getId());
				}
			}
			view.setJobSiteId(null);
		}
	}
	
	/**
	 * Expands a NotificationSettingView into a NotificationSetting and NotificationSettingOwner, resolving enums and entities.
	 */
	public void populateModel(NotificationSetting model, NotificationSettingOwner owner, NotificationSettingView view) {
		
		// setup the model object
		model.setId(view.getId());
		model.setTenant(tenant);
		model.setUser(user);
		model.setUsingJobSite(usingJobSites);
		model.setName(view.getName());
		
		model.setFrequency(SimpleFrequency.valueOf(view.getFrequency()));
		model.setPeriodStart(RelativeTime.valueOf(view.getPeriodStart()));
		model.setPeriodEnd(RelativeTime.valueOf(view.getPeriodEnd()));
		
		if (view.getCreatedTimeStamp() != null) {
			model.setCreated(new Date(view.getCreatedTimeStamp()));
		}
		
		if (view.getProductTypeId() != null) {
			model.getProductTypes().add(view.getProductTypeId());
		}
		
		if (view.getInspectionTypeId() != null) {
			model.getInspectionTypes().add(view.getInspectionTypeId());
		}
		
		model.getAddresses().addAll(view.getAddresses());
		
		// setup the single owner object
		owner.setId(view.getOwnerId());
		owner.setNotificationSetting(model);
		
		if (usingJobSites) {
			if (view.getJobSiteId() != null) {
				owner.setJobSite(pm.find(JobSite.class, view.getJobSiteId(), filter.prepareFor(JobSite.class)));
			}
		} else {
			if (view.getCustomerId() != null) {
				FilteredIdLoader<Customer> customerLoader = new FilteredIdLoader<Customer>(filter, Customer.class);
				customerLoader.setId(view.getCustomerId());

				owner.setCustomer(customerLoader.load());
				
				if (view.getDivisionId() != null) {
					FilteredIdLoader<Division> divisionLoader = new FilteredIdLoader<Division>(filter, Division.class);
					divisionLoader.setId(view.getDivisionId());
					
					owner.setDivision(divisionLoader.load());
				}
			}
		}
		
	}
}
