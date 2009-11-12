package com.n4systems.persistence.loaders;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.downloadlink.DownloadLinkListLoader;
import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypesLoader;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.messages.PaginatedMessageLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.ExternalOrgCodeExistsLoader;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.SecondaryOrgByNameLoader;
import com.n4systems.model.orgs.SecondaryOrgListableLoader;
import com.n4systems.model.orgs.SecondaryOrgPaginatedLoader;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.product.ProductAttachmentListLoader;
import com.n4systems.model.product.ProductSerialExtensionListLoader;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.productstatus.ProductStatusFilteredLoader;
import com.n4systems.model.productstatus.ProductStatusListLoader;
import com.n4systems.model.producttype.AutoAttributeCriteriaByProductTypeIdLoader;
import com.n4systems.model.producttype.InspectionFrequencyListLoader;
import com.n4systems.model.producttype.ProductTypeListableLoader;
import com.n4systems.model.producttype.ProductTypeLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgListLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionsListLoader;
import com.n4systems.model.safetynetwork.HasLinkedProductsLoader;
import com.n4systems.model.safetynetwork.PaginatedConnectionListLoader;
import com.n4systems.model.safetynetwork.ProductsByNetworkId;
import com.n4systems.model.safetynetwork.SafetyNetworkBackgroundSearchLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkProductLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;
import com.n4systems.model.safetynetwork.TenantWideVendorOrgConnPaginatedLoader;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.safetynetwork.VendorLinkedOrgListLoader;
import com.n4systems.model.safetynetwork.VendorLinkedOrgLoader;
import com.n4systems.model.safetynetwork.VendorOrgConnectionLoader;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.tools.Pager;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory {
	private final SecurityFilter filter;

	/*
	 * NOTE: this factory is not singleton(or thread local even) as it holds a
	 * SecurityFilter and should be explicitly constructed/destroyed. Anything
	 * else would be insecure.
	 * 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods
	 * to this factory.
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

	public BaseOrgParentFilterListLoader createBaseParentFilterLoader() {
		return new BaseOrgParentFilterListLoader(filter);
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
	
	public CustomerLinkedOrgListLoader createCustomerLinkedOrgListLoader() {
		return new CustomerLinkedOrgListLoader(filter);
	}

	public CustomerLinkedOrgLoader createCustomerLinkedOrgLoader() {
		return new CustomerLinkedOrgLoader(filter);
	}
	
	public CustomerOrgConnectionLoader createCustomerOrgConnectionLoader() {
		return new CustomerOrgConnectionLoader(filter);
	}

	public CustomerOrgConnectionsListLoader createCustomerOrgConnectionsListLoader() {
		return new CustomerOrgConnectionsListLoader(filter);
	}

	public CustomerOrgPaginatedLoader createCustomerOrgPaginatedLoader() {
		return new CustomerOrgPaginatedLoader(filter);
	}

	public DivisionOrgByCustomerListLoader createDivisionOrgByCustomerListLoader() {
		return new DivisionOrgByCustomerListLoader(filter);
	}

	public DivisionOrgPaginatedLoader createDivisionOrgPaginatedLoader() {
		return new DivisionOrgPaginatedLoader(filter);
	}

	public DownloadLinkListLoader createDownloadLinkListLoader() {
		return new DownloadLinkListLoader(filter);
	}

	public <T extends ExternalOrg> ExternalOrgCodeExistsLoader<T> createExternalOrgCodeExistsLoader(Class<T> orgClass) {
		return new ExternalOrgCodeExistsLoader<T>(filter, orgClass);
	}

	public <T extends AbstractEntity> FilteredIdLoader<T> createFilteredIdLoader(Class<T> clazz) {
		return new FilteredIdLoader<T>(filter, clazz);
	}
	
	public FilteredListableLoader createFilteredListableLoader(Class<? extends NamedEntity> clazz) {
		return new FilteredListableLoader(filter, clazz);
	}
	
	public HasLinkedProductsLoader createHasLinkedProductsLoader() {
		return new HasLinkedProductsLoader(filter);
	}

	public InspectionBookListLoader createInspectionBookListLoader() {
		return new InspectionBookListLoader(filter);
	}

	public InspectionFrequencyListLoader createInspectionFrequenciesListLoader() {
		return new InspectionFrequencyListLoader(filter);
	}

	public InspectionTypeListableLoader createInspectionTypeListableLoader() {
		return new InspectionTypeListableLoader(filter);
	}

	public InternalOrgListableLoader createInternalOrgListableLoader() {
		return new InternalOrgListableLoader(filter);
	}

	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return new LatestEulaAcceptanceLoader(filter);
	}

	public NotificationSettingByUserListLoader createNotificationSettingByUserListLoader() {
		return new NotificationSettingByUserListLoader(filter);
	}

	public Loader<Pager<TypedOrgConnection>> createPaginatedConnectionListLoader() {
		return new PaginatedConnectionListLoader(filter);
	}
	
	public PaginatedMessageLoader createPaginatedMessageLoader() {
		return new PaginatedMessageLoader(filter);
	}
	
	public PrimaryOrgByTenantLoader createPrimaryOrgByTenantLoader() {
		return new PrimaryOrgByTenantLoader();
	}
	
	public ProductAttachmentListLoader createProductAttachmentListLoader() {
		return new ProductAttachmentListLoader(filter);
	}
	
	public ProductsByNetworkId createProductsByNetworkId() {
		return new ProductsByNetworkId(filter);
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
	
	public ProductTypeLoader createProductTypeLoader() {
		return new ProductTypeLoader(new TenantOnlySecurityFilter(filter.getTenantId()));
	}
	
	public SafetyNetworkBackgroundSearchLoader createSafetyNetworkBackgroundSearchLoader() {
		return new SafetyNetworkBackgroundSearchLoader(filter);
	}
	
	public SafetyNetworkInspectionLoader createSafetyNetworkInspectionLoader() {
		return new SafetyNetworkInspectionLoader(filter);
	}
	
	public SafetyNetworkProductLoader createSafetyNetworkProductLoader() {
		return new SafetyNetworkProductLoader(filter);
	}
	
	public SafetyNetworkSmartSearchLoader createSafetyNetworkSmartSearchLoader() {
		return new SafetyNetworkSmartSearchLoader(filter);
	}
	
	public SecondaryOrgByNameLoader createSecondaryOrgByNameLoader() {
		return new SecondaryOrgByNameLoader(filter);
	}
	
	public SecondaryOrgListableLoader createSecondaryOrgListableLoader() {
		return new SecondaryOrgListableLoader(filter);
	}
	
	public SecondaryOrgPaginatedLoader createSecondaryOrgPaginatedLoader() {
		return new SecondaryOrgPaginatedLoader(filter);
	}
	
	public SmartSearchLoader createSmartSearchListLoader() {
		return new SmartSearchLoader(filter);
	}
	
	public TaskConfigLoader createTaskConfigLoader() {
		return new TaskConfigLoader();
	}

	public TenantWideVendorOrgConnPaginatedLoader createTenantWideVendorOrgConnPaginatedLoader() {
		return new TenantWideVendorOrgConnPaginatedLoader(filter);
	}

	public UserFilteredLoader createUserFilteredLoader() {
		return new UserFilteredLoader(filter);
	}

	public UserListableLoader createUserListableLoader() {
		return new UserListableLoader(filter);
	}
	
	public VendorLinkedOrgListLoader createVendorLinkedOrgListLoader() {
		return new VendorLinkedOrgListLoader(filter);
	}
	
	public VendorLinkedOrgLoader createVendorLinkedOrgLoader() {
		return new VendorLinkedOrgLoader(filter);
	}
	
	public VendorOrgConnectionLoader createVendorOrgConnectionLoader() {
		return new VendorOrgConnectionLoader(filter);
	}

	public VendorOrgConnectionsListLoader createVendorOrgConnectionsListLoader() {
		return new VendorOrgConnectionsListLoader(filter);
	}
}
