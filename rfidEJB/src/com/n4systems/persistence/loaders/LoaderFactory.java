package com.n4systems.persistence.loaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.customer.CustomerFilteredLoader;
import com.n4systems.model.customer.CustomerListableLoader;
import com.n4systems.model.division.DivisionFilteredLoader;
import com.n4systems.model.division.DivisionListableLoader;
import com.n4systems.model.division.DivisionUniqueNameUsedLoader;
import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCountListLoader;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.jobsites.JobSiteFilteredLoader;
import com.n4systems.model.jobsites.JobSiteListableLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByIdLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerListLoader;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.model.product.ProductSerialExtensionListLoader;
import com.n4systems.model.productstatus.ProductStatusFilteredLoader;
import com.n4systems.model.productstatus.ProductStatusListLoader;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.model.producttype.ProductTypeFilteredLoader;
import com.n4systems.model.producttype.ProductTypeListableLoader;
import com.n4systems.model.taskconfig.TaskConfigListLoader;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.util.SecurityFilter;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory {
	private final PersistenceManager pm;
	private final SecurityFilter filter;
	
	/*
	 * NOTE: this factory is not singleton(or thread local even) as it holds a SecurityFilter and should be explicitly
	 * constructed/destroyed.  Anything else would be insecure.
	 * 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */
	public LoaderFactory(PersistenceManager pm, SecurityFilter filter) {
		this.pm = pm;
		this.filter = filter;
	}
	
	public AutoAttributeCriteriaByProductTypeIdLoader createAutoAttributeCriteriaByProductTypeIdLoader() {
		return new AutoAttributeCriteriaByProductTypeIdLoader(pm, filter);
	}
	public CommentTemplateListableLoader createCommentTemplateListableLoader() {
		return new CommentTemplateListableLoader(pm, filter);
	}
	
	public CurrentEulaLoader createCurrentEulaLoader() {
		return new CurrentEulaLoader(pm);
	}
	
	public CustomerFilteredLoader createCustomerFilteredLoader() {
		return new CustomerFilteredLoader(pm, filter);
	}
	
	public CustomerListableLoader createCustomerListableLoader() {
		return new CustomerListableLoader(pm, filter);
	}
	
	public DivisionFilteredLoader createDivisionFilteredLoader() {
		return new DivisionFilteredLoader(pm, filter);
	}
	
	public DivisionListableLoader createDivisionListableLoader() {
		return new DivisionListableLoader(pm, filter);
	}
	
	public DivisionUniqueNameUsedLoader createDivisionUniqueNameUsedLoader() {
		return new DivisionUniqueNameUsedLoader(pm, filter);
	}
	
	public InspectionScheduleCountListLoader createInspectionScheduleCountListLoader() {
		return new InspectionScheduleCountListLoader(pm, filter);
	}
	
	public InspectionTypeListableLoader createInspectionTypeListableLoader() {
		return new InspectionTypeListableLoader(pm, filter);
	}
	
	public JobSiteFilteredLoader createJobSiteFilteredLoader() {
		return new JobSiteFilteredLoader(pm, filter);
	}
	
	public JobSiteListableLoader createJobSiteListableLoader() {
		return new JobSiteListableLoader(pm, filter);
	}
	
	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return new LatestEulaAcceptanceLoader(pm, filter);
	}
	
	public NotificationSettingByIdLoader createNotificationSettingByIdLoader() {
		return new NotificationSettingByIdLoader(pm , filter);
	}
	
	public NotificationSettingByUserListLoader createNotificationSettingByUserListLoader() {
		return new NotificationSettingByUserListLoader(pm, filter);
	}
	
	public NotificationSettingListLoader createNotificationSettingListLoader() {
		return new NotificationSettingListLoader(pm, filter);
	}
	
	public NotificationSettingOwnerListLoader createNotificationSettingOwnerListLoader() {
		return new NotificationSettingOwnerListLoader(pm, filter);
	}
	
	public ProductAttachmentListLoader createProductAttachmentListLoader() {
		return new ProductAttachmentListLoader(pm, filter);
	}

	public ProductSerialExtensionListLoader createProductSerialExtensionListLoader() {
		return new ProductSerialExtensionListLoader(pm, filter);
	}
	
	public ProductStatusFilteredLoader createProductStatusFilteredLoader() {
		return new ProductStatusFilteredLoader(pm, filter);
	}
	
	public ProductStatusListLoader createProductStatusListLoader() {
		return new ProductStatusListLoader(pm, filter);
	}
	
	public ProductTypeFilteredLoader createProductTypeFilteredLoader() {
		return new ProductTypeFilteredLoader(pm, filter);
	}
	
	public ProductTypeListableLoader createProductTypeListableLoader() {
		return new ProductTypeListableLoader(pm, filter);
	}
	
	public TaskConfigListLoader createTaskConfigListLoader() {
		return new TaskConfigListLoader(pm, filter);
	}
	
	public TaskConfigLoader createTaskConfigLoader() {
		return new TaskConfigLoader(pm);
	}
	
	public UserFilteredLoader createUserFilteredLoader() {
		return new UserFilteredLoader(pm, filter);
	}
	
	public UserListableLoader createUserListableLoader() {
		return new UserListableLoader(pm, filter);
	}
}
