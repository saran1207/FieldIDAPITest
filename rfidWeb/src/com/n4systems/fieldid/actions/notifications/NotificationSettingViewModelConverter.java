package com.n4systems.fieldid.actions.notifications;

import java.util.Date;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.notificationsettings.NotificationSetting;

public class NotificationSettingViewModelConverter {
	
	private final Tenant tenant;
	private final UserBean user;
	
	public NotificationSettingViewModelConverter(Tenant tenant, UserBean user) {
		this.tenant = tenant;
		this.user = user;
	}
	
	/**
	 * Compresses a NotificationSetting and NotificationSettingOwner into a single NotificationSettingView while
	 * converting entities and enums into id's
	 */
	public void populateView(NotificationSetting model, NotificationSettingView view) {
		
		// get fields from the model object
		view.setId(model.getId());
		view.setName(model.getName());
		view.setOwner(model.getOwner());
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
	}
	
	/**
	 * Expands a NotificationSettingView into a NotificationSetting and NotificationSettingOwner, resolving enums and entities.
	 */
	public void populateModel(NotificationSetting model, NotificationSettingView view) {
		
		// setup the model object
		model.setId(view.getId());
		model.setTenant(tenant);
		model.setUser(user);
		model.setName(view.getName());
		model.setOwner(view.getOwner());
		
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
	}
}
