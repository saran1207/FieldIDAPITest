package com.n4systems.persistence.loaders;

import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.customer.CustomerListableLoader;
import com.n4systems.model.division.DivisionListableLoader;
import com.n4systems.model.division.DivisionUniqueNameUsedLoader;
import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;
import com.n4systems.model.inspectionschedulecount.InspectionScheduleCountListLoader;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypesLoader;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.jobsites.JobSiteListableLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingOwnerListLoader;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.model.product.ProductSerialExtensionListLoader;
import com.n4systems.model.productstatus.ProductStatusFilteredLoader;
import com.n4systems.model.productstatus.ProductStatusListLoader;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.model.producttype.InspectionFrequencyListLoader;
import com.n4systems.model.producttype.ProductTypeListableLoader;
import com.n4systems.model.producttype.ProductTypeScheduleLoader;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.util.SecurityFilter;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory {
	private final SecurityFilter filter;
	
	/*
	 * NOTE: this factory is not singleton(or thread local even) as it holds a SecurityFilter and should be explicitly
	 * constructed/destroyed.  Anything else would be insecure.
	 * 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */
	public LoaderFactory(SecurityFilter filter) {
		this.filter = filter;
	}
	
	public <T> AllEntityListLoader<T> createAllEntityListLoader(Class<T> clazz) {
		return new AllEntityListLoader<T>(clazz);
	}
	
	public AssociatedInspectionTypesLoader createAssociatedInspectionTypesLoader() {
		return new AssociatedInspectionTypesLoader(filter);
	}
	
	public AutoAttributeCriteriaByProductTypeIdLoader createAutoAttributeCriteriaByProductTypeIdLoader() {
		return new AutoAttributeCriteriaByProductTypeIdLoader(filter);
	}
	public CatalogLoader createCatalogLoader() {
		return new CatalogLoader();
	}
	
	public CommentTemplateListableLoader createCommentTemplateListableLoader() {
		return new CommentTemplateListableLoader(filter);
	}
	
	public CurrentEulaLoader createCurrentEulaLoader() {
		return new CurrentEulaLoader();
	}
	
	public CustomerListableLoader createCustomerListableLoader() {
		return new CustomerListableLoader(filter);
	}
	
	public DivisionListableLoader createDivisionListableLoader() {
		return new DivisionListableLoader(filter);
	}
	
	public DivisionUniqueNameUsedLoader createDivisionUniqueNameUsedLoader() {
		return new DivisionUniqueNameUsedLoader(filter);
	}
	
	public <T extends AbstractEntity & FilteredEntity> FilteredIdLoader<T> createFilteredIdLoader(Class<T> clazz) {
		return new FilteredIdLoader<T>(filter, clazz);
	}
	
	public InspectionFrequencyListLoader createInspectionFrequenciesListLoader() {
		return new InspectionFrequencyListLoader(filter);
	}
	
	public InspectionScheduleCountListLoader createInspectionScheduleCountListLoader() {
		return new InspectionScheduleCountListLoader(filter);
	}
	
	public InspectionTypeListableLoader createInspectionTypeListableLoader() {
		return new InspectionTypeListableLoader(filter);
	}
	
	public JobSiteListableLoader createJobSiteListableLoader() {
		return new JobSiteListableLoader(filter);
	}
	
	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return new LatestEulaAcceptanceLoader(filter);
	}
	
	public NotificationSettingByUserListLoader createNotificationSettingByUserListLoader() {
		return new NotificationSettingByUserListLoader(filter);
	}

	public NotificationSettingOwnerListLoader createNotificationSettingOwnerListLoader() {
		return new NotificationSettingOwnerListLoader();
	}
	
	public ProductAttachmentListLoader createProductAttachmentListLoader() {
		return new ProductAttachmentListLoader(filter);
	}
	
	public ProductSerialExtensionListLoader createProductSerialExtensionListLoader() {
		return new ProductSerialExtensionListLoader(filter);
	}
	
	public ProductStatusFilteredLoader createProductStatusFilteredLoader() {
		return new ProductStatusFilteredLoader(filter);
	}
	
	public ProductStatusListLoader createProductStatusListLoader() {
		return new ProductStatusListLoader(filter);
	}
	
	public ProductTypeListableLoader createProductTypeListableLoader() {
		return new ProductTypeListableLoader(filter);
	}
	
	public ProductTypeScheduleLoader createProductTypeScheduleLoader() {
		return new ProductTypeScheduleLoader(filter);
	}
	
	public TaskConfigLoader createTaskConfigLoader() {
		return new TaskConfigLoader();
	}
	
	public UserFilteredLoader createUserFilteredLoader() {
		return new UserFilteredLoader(filter);
	}
	
	public UserListableLoader createUserListableLoader() {
		return new UserListableLoader(filter);
	}
}
