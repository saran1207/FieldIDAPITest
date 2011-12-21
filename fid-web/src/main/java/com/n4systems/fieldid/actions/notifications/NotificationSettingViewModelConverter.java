package com.n4systems.fieldid.actions.notifications;

import java.util.Date;

import com.n4systems.model.Tenant;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.UpcomingEventReport;
import com.n4systems.model.user.User;

public class NotificationSettingViewModelConverter {
	
	private final Tenant tenant;
	private final User user;
	
	public NotificationSettingViewModelConverter(Tenant tenant, User user) {
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
		view.setPeriodStart(model.getUpcomingReport().getPeriodStart().getId());
		view.setPeriodEnd(model.getUpcomingReport().getPeriodEnd().getId());
		view.setIncludeOverdue(model.isIncludeOverdue());
		view.setIncludeUpcoming(model.getUpcomingReport().isIncludeUpcoming());
		view.setIncludeFailed(model.isIncludeFailed());
		view.setSendBlankReport(model.getSendBlankReport());
		view.setSmartFailure(model.isSmartFailure());
		
		// we'll need the created date to carry through on edit
		if (model.getCreated() != null) {
			view.setCreatedTimeStamp(model.getCreated().getTime());
		}
		
		view.setAssetTypeGroupId(model.getAssetTypeGroup());
		view.setEventTypeGroupId(model.getEventTypeGroup());
		
		if (!model.getAssetTypes().isEmpty()) {
			// we only support a single asset type right now
			view.setAssetTypeId(model.getAssetTypes().get(0));
		}
		
		view.setAssetStatus(model.getAssetStatus());
		
		if (!model.getEventTypes().isEmpty()) {
			// we only support a single event type right now
			view.setEventTypeId(model.getEventTypes().get(0));
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
		if(view.getIncludeUpcoming())
			model.setUpcomingReport(new UpcomingEventReport(RelativeTime.valueOf(view.getPeriodStart()), RelativeTime.valueOf(view.getPeriodEnd()), true));
		else
			model.setUpcomingReport(new UpcomingEventReport(RelativeTime.TODAY, RelativeTime.TOMORROW, false));
		model.setIncludeOverdue(view.getIncludeOverdue());
		model.setSmartFailure(view.getSmartFailure());
		model.setIncludeFailed(view.getIncludeFailed());
		model.setSendBlankReport(view.getSendBlankReport());
		
		if (view.getCreatedTimeStamp() != null) {
			model.setCreated(new Date(view.getCreatedTimeStamp()));
		}
		
		model.setAssetTypeGroup(view.getAssetTypeGroupId());

		model.setEventTypeGroup(view.getEventTypeGroupId());
		
		if (view.getAssetTypeId() != null) {
			model.getAssetTypes().add(view.getAssetTypeId());
		}
		
		model.setAssetStatus(view.getAssetStatus());
		
		if (view.getEventTypeId() != null) {
			model.getEventTypes().add(view.getEventTypeId());
		}
		
		
		
		model.getAddresses().addAll(view.getAddresses());
	}
}
