
package com.n4systems.fieldid.actions.notifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.NotificationSettingManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.model.JobSite;
import com.n4systems.model.api.Listable;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.customer.CustomerListableLoader;
import com.n4systems.model.division.DivisionListableLoader;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingOwner;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerListLoader;
import com.n4systems.model.producttype.ProductTypeListableLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.SimpleListable;

public class NotificationSettingsCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(NotificationSettingsCrud.class);
	
	// these are the relative times that will be used in each period list (in order)
	private static final RelativeTime[] startTimes = {RelativeTime.TODAY, RelativeTime.TOMORROW, RelativeTime.THIS_WEEK, RelativeTime.NEXT_WEEK, RelativeTime.THIS_MONTH, RelativeTime.NEXT_MONTH};
	private static final RelativeTime[] endTimes = {RelativeTime.DAY_1, RelativeTime.DAY_2, RelativeTime.WEEK_1, RelativeTime.WEEK_2, RelativeTime.MONTH_1, RelativeTime.MONTH_2};
	
	private final NotificationSettingManager notificationSettingManager;
	
	private List<ListingPair> jobSites;
	private List<Listable<Long>> customers;
	private List<Listable<Long>> divisions;
	private List<Listable<String>> periodStartList;
	private List<Listable<String>> periodEndList;
	private List<Listable<Long>> productTypeList;
	private List<Listable<Long>> inspectionTypeList;
	private List<FrequencyGroupView> frequencyGroups;
	
	private List<NotificationSetting> settingsList;
	private NotificationSettingViewModelConverter converter;
	private NotificationSettingView view = new NotificationSettingView();
	
	public NotificationSettingsCrud(PersistenceManager persistenceManager, NotificationSettingManager notificationSettingManager) {
		super(persistenceManager);
		this.notificationSettingManager = notificationSettingManager;
	}
	
	private void initPeriodList(List<Listable<String>> periodList, RelativeTime[] allowedTimes) {
		// populate the period list with our allowed period times and resolve the label
		for (RelativeTime time: allowedTimes) {
			periodList.add(new SimpleListable<String>(time.getId(), getText(time.getDisplayName())));
		}
	}

	private void initFrequencyGroups() {
		frequencyGroups = new ArrayList<FrequencyGroupView>();
		
	    // build a list of frequency group views, these are used in the frequency select option groups
		// we'll create a temporary map to help with grouping.  Note it's LinkedHashMap so it itterates in order
		Map<String, FrequencyGroupView> freqMap = new LinkedHashMap<String, FrequencyGroupView>();
		for (SimpleFrequency freq: SimpleFrequency.values()) {
			// note that all labels, in the frequency will need to be resolved in to their real text
			if (!freqMap.containsKey(freq.getGroupLabel())) {
				freqMap.put(freq.getGroupLabel(), new FrequencyGroupView(getText(freq.getGroupLabel())));
			}
			
			// in one, step we get the group, create a simple listable from our frequency and resolve the label text
			freqMap.get(freq.getGroupLabel()).getFrequencies().add(new SimpleListable<String>(freq.getId(), getText(freq.getDisplayName())));
		}
		// populate our frequency groups and discard the map
		frequencyGroups.addAll(freqMap.values());
		freqMap = null;
    }

	@Override
	protected void initMemberFields() {
		converter = new NotificationSettingViewModelConverter(persistenceManager, getTenant(), getUser(), getSecurityFilter());
	}
	
	@Override
	protected void loadMemberFields(Long uniqueId) {
		converter = new NotificationSettingViewModelConverter(persistenceManager, getTenant(), getUser(), getSecurityFilter());
	}
	
	@SkipValidation
	public String doList() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		converter.populateView(new NotificationSetting(), new NotificationSettingOwner(), view);
		
		// default to the users email address
		view.getAddresses().add(getUser().getEmailAddress());
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doEdit() {
		FilteredIdLoader<NotificationSetting> settingLoader = getLoaderFactory().createFilteredIdLoader(NotificationSetting.class);
		settingLoader.setId(getUniqueID());
		
		NotificationSetting setting = settingLoader.load();
		
		if (setting == null) {
			addActionErrorText("error.nonotificationsetting");
			return MISSING;
		}
			
		NotificationSettingOwnerListLoader ownerLoader = new NotificationSettingOwnerListLoader();
		ownerLoader.setNotificationSettingId(setting.getId());
		List<NotificationSettingOwner> owners = ownerLoader.load();
		
		// we currently only support a single owner, so if there is one we will just take the first
		NotificationSettingOwner owner = (owners.isEmpty()) ? new NotificationSettingOwner() : owners.get(0);
		
		converter.populateView(setting, owner, view);
				
		return SUCCESS;
	}
	
	public String doSave() {
		try {
			ListHelper.clearEmpties(view.getAddresses());
			
			NotificationSetting setting = new NotificationSetting();
			NotificationSettingOwner owner = new NotificationSettingOwner();
			
			converter.populateModel(setting, owner, view);
			
			notificationSettingManager.saveOrUpdate(setting, new ArrayList<NotificationSettingOwner>(Arrays.asList(owner)), getSessionUserId());
			
			uniqueID = setting.getId();
			
			addFlashMessageText("message.notificationsettingsaved");
			
		} catch (MissingEntityException e) {
			return MISSING;
			
		} catch (Exception e) {
			logger.error("Could not save Notification Setting", e);
			addActionErrorText("error.notificationsettingssavefailed");
			
			return ERROR;
		}
		
		// null this out so it reloads
		settingsList = null;
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doDelete() {
		try {
			NotificationSetting setting = persistenceManager.find(NotificationSetting.class, getUniqueID(), getTenant());
			
			notificationSettingManager.remove(setting);
			
			addFlashMessageText("message.notificationsettingdeleted");
		} catch (MissingEntityException e) {
			return MISSING;
			
		} catch (Exception e) {
			logger.error("Could not delete Inspection Schedule", e);
			addActionErrorText("error.notificationsettingdeletefailed");
			
			return ERROR;
		}
		
		// null this out so it realoads
		settingsList = null;
		
		return SUCCESS;
	}
	
	public List<NotificationSetting> getSettingsList() {
		if (settingsList == null) {
			NotificationSettingByUserListLoader loader = getLoaderFactory().createNotificationSettingByUserListLoader();
			loader.setUserId(getSessionUserId());
			settingsList = loader.load();
		}
		return settingsList;
	}
	
	public List<FrequencyGroupView> getFrequencyGroups() {
		if (frequencyGroups == null) {
			initFrequencyGroups();
		}
		return frequencyGroups;
	}
	
	public List<Listable<String>> getPeriodStartList() {
		if (periodStartList == null) {
			periodStartList = new ArrayList<Listable<String>>();
			initPeriodList(periodStartList, startTimes);
		}		
    	return periodStartList;
    }

	public List<Listable<String>> getPeriodEndList() {
		if (periodEndList == null) {
			periodEndList = new ArrayList<Listable<String>>();
			initPeriodList(periodEndList, endTimes);
		}		
    	return periodEndList;
    }

	public List<Listable<Long>> getInspectionTypeList() {
		if (inspectionTypeList == null) {
			InspectionTypeListableLoader loader = getLoaderFactory().createInspectionTypeListableLoader();
			inspectionTypeList = loader.load();
		}
    	return inspectionTypeList;
    }

	public List<Listable<Long>> getProductTypeList() {
		if (productTypeList == null) {
			ProductTypeListableLoader loader = getLoaderFactory().createProductTypeListableLoader();
			productTypeList = loader.load();
		}
    	return productTypeList;
    }	
	
	public List<Listable<Long>> getCustomers() {
		if (customers == null) {
			CustomerListableLoader customerListLoader = getLoaderFactory().createCustomerListableLoader();
			customers = customerListLoader.load();
		}
		return customers;
	}
	
	public List<Listable<Long>> getDivisions() {
		if (divisions == null) {
			if (view.getCustomerId() != null) {
				DivisionListableLoader divisionListLoader = getLoaderFactory().createDivisionListableLoader();
				divisionListLoader.setCustomerId(view.getCustomerId());
				
				divisions = divisionListLoader.load();
			} else {
				divisions = new ArrayList<Listable<Long>>();
			}
		}
		return divisions;
	}
	
	public List<ListingPair> getJobSites() {
		if (jobSites == null) {
			jobSites = persistenceManager.findAllLP(JobSite.class, getSecurityFilter().prepareFor(JobSite.class), "name");
		}
		return jobSites;
	}
	
	public NotificationSettingView getView() {
		return view;
	}
	
	public void setView(NotificationSettingView view) {
		this.view = view;
	}

}
