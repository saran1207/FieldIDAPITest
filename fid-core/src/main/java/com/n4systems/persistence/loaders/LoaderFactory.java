package com.n4systems.persistence.loaders;

import java.io.Serializable;
import java.util.List;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.asset.AssetExtensionListLoader;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.assetstatus.AssetStatusFilteredLoader;
import com.n4systems.model.assetstatus.AssetStatusForNameExistsLoader;
import com.n4systems.model.assetstatus.AssetStatusListLoader;
import com.n4systems.model.assettype.AutoAttributeCriteriaByAssetTypeIdLoader;
import com.n4systems.model.autoattribute.AutoAttributeDefinitionListLoader;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.downloadlink.DownloadLinkListLoader;
import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;
import com.n4systems.model.fileattachment.FileAttachmentLoader;
import com.n4systems.model.inspection.LastInspectionLoader;
import com.n4systems.model.inspectionbook.InspectionBookByNameLoader;
import com.n4systems.model.inspectionbook.InspectionBookFindOrCreateLoader;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.inspectionschedule.IncompleteInspectionSchedulesListLoader;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.model.inspectiontype.AssociatedInspectionTypesLoader;
import com.n4systems.model.inspectiontype.InspectionTypeListableLoader;
import com.n4systems.model.jobs.EventJobListableLoader;
import com.n4systems.model.location.AllPredefinedLocationsPaginatedLoader;
import com.n4systems.model.location.PredefinedLocationByIdLoader;
import com.n4systems.model.location.PredefinedLocationListLoader;
import com.n4systems.model.location.PredefinedLocationTreeLoader;
import com.n4systems.model.messages.PaginatedMessageLoader;
import com.n4systems.model.messages.UnreadMessageCountLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrgWithArchivedPaginatedLoader;
import com.n4systems.model.orgs.EntityByIdIncludingArchivedLoader;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.InternalOrgListableLoader;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrgByTenantLoader;
import com.n4systems.model.orgs.SecondaryOrgByNameLoader;
import com.n4systems.model.orgs.SecondaryOrgListableLoader;
import com.n4systems.model.orgs.SecondaryOrgPaginatedLoader;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.orgs.internal.InternalOrgByNameLoader;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.asset.AssetAttachmentListLoader;
import com.n4systems.model.asset.SyncAssetListLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.assettype.AssetTypeByAttachmentLoader;
import com.n4systems.model.assettype.AssetTypeListableLoader;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.assettype.InspectionFrequencyListLoader;
import com.n4systems.model.safetynetwork.AssetAlreadyRegisteredLoader;
import com.n4systems.model.safetynetwork.AssetsByNetworkIdLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgListLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionsListLoader;
import com.n4systems.model.safetynetwork.HasLinkedAssetsLoader;
import com.n4systems.model.safetynetwork.InspectionsByAssetIdLoader;
import com.n4systems.model.safetynetwork.PaginatedConnectionListLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssignedAssetInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAttachmentLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkBackgroundSearchLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkPreAssignedAssetLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetAttachmentListLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetAttachmentLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetTypeLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredAssetCountLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredAssetInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredOrAssignedInspectionLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkSmartSearchLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkUnregisteredAssetCountLoader;
import com.n4systems.model.safetynetwork.TenantWideVendorOrgConnPaginatedLoader;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnectionListLoader;
import com.n4systems.model.safetynetwork.VendorLinkedOrgListLoader;
import com.n4systems.model.safetynetwork.VendorLinkedOrgLoader;
import com.n4systems.model.safetynetwork.VendorOrgConnectionLoader;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.signup.SignupReferralListLoader;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.tenant.PrimaryOrgsWithNameLikeLoader;
import com.n4systems.model.user.EmployeePaginatedLoader;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.tools.Pager;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory implements Serializable {
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
	
	public AllPredefinedLocationsPaginatedLoader createAllPredefinedLocationsPaginatedLoader() {
		return new AllPredefinedLocationsPaginatedLoader(filter);
	}

	public AssociatedInspectionTypesLoader createAssociatedInspectionTypesLoader() {
		return new AssociatedInspectionTypesLoader(filter);
	}
	
	public AutoAttributeCriteriaByAssetTypeIdLoader createAutoAttributeCriteriaByAssetTypeIdLoader() {
		return new AutoAttributeCriteriaByAssetTypeIdLoader(filter);
	}

	public AutoAttributeDefinitionListLoader createAutoAttributeDefinitionListLoader() {
		return new AutoAttributeDefinitionListLoader(filter);
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

	public UserListableLoader createCurrentEmployeesListableLoader() {
		return createHistoricalEmployeesListableLoader().setNoDeleted(true);
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

	public CustomerOrgListLoader createCustomerOrgListLoader() {
		return new CustomerOrgListLoader(filter);
	}

	public CustomerOrgPaginatedLoader createCustomerOrgPaginatedLoader() {
		return new CustomerOrgPaginatedLoader(filter);
	}

	public CustomerOrgWithArchivedPaginatedLoader createCustomerOrgWithArchivedPaginatedLoader() {
		return new CustomerOrgWithArchivedPaginatedLoader(filter);
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

	public EmployeePaginatedLoader createEmployeePaginatedLoader() {
		return new EmployeePaginatedLoader(filter);
	}

	public EventJobListableLoader createEventJobListableLoader() {
		return new EventJobListableLoader(filter);
	}

	public FileAttachmentLoader createFileAttachmentLoader() {
		return new FileAttachmentLoader(filter);
	}

	public <T extends AbstractEntity> FilteredIdLoader<T> createFilteredIdLoader(Class<T> clazz) {
		return new FilteredIdLoader<T>(filter, clazz);
	}

	public <T> FilteredInListLoader<T> createFilteredInListLoader(Class<T> clazz) {
		return new FilteredInListLoader<T>(filter, clazz);
	}

	public FilteredListableLoader createFilteredListableLoader(Class<? extends NamedEntity> clazz) {
		return new FilteredListableLoader(filter, clazz);
	}

	public <T extends Exportable> GlobalIdLoader<T> createGlobalIdLoader(Class<T> clazz) {
		return new GlobalIdLoader<T>(filter, clazz);
	}

	public HasLinkedAssetsLoader createHasLinkedAssetsLoader() {
		return new HasLinkedAssetsLoader(filter);
	}

	public UserListableLoader createHistoricalEmployeesListableLoader() {
		return createUserListableLoader().employeesOnly();
	}

	public IncompleteInspectionSchedulesListLoader createIncompleteInspectionSchedulesListLoader() {
		return new IncompleteInspectionSchedulesListLoader(filter);
	}

	public InspectionBookByNameLoader createInspectionBookByNameLoader() {
		return new InspectionBookByNameLoader(filter);
	}

	public InspectionBookFindOrCreateLoader createInspectionBookFindOrCreateLoader() {
		return new InspectionBookFindOrCreateLoader(filter);
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

	public InternalOrgByNameLoader createInternalOrgByNameLoader() {
		return new InternalOrgByNameLoader(filter);
	}

	public InternalOrgListableLoader createInternalOrgListableLoader() {
		return new InternalOrgListableLoader(filter);
	}

	public LastInspectionLoader createLastInspectionLoader() {
		return new LastInspectionLoader(filter);
	}
	
	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return new LatestEulaAcceptanceLoader(filter);
	}

	public NextInspectionDateByInspectionLoader createNextInspectionDateByInspectionLoader() {
		return new NextInspectionDateByInspectionLoader(filter);
	}

	public NotificationSettingByUserListLoader createNotificationSettingByUserListLoader() {
		return new NotificationSettingByUserListLoader(filter);
	}

	public OrgByNameLoader createOrgByNameLoader() {
		return new OrgByNameLoader(filter);
	}

	public Loader<Pager<TypedOrgConnection>> createPaginatedConnectionListLoader() {
		return new PaginatedConnectionListLoader(filter);
	}

	public PaginatedMessageLoader createPaginatedMessageLoader() {
		return new PaginatedMessageLoader(filter);
	}

	public <T> PassthruListLoader<T> createPassthruListLoader(List<T> entities) {
		return new PassthruListLoader<T>(entities);
	}

	public PredefinedLocationByIdLoader createPredefinedLocationByIdLoader() {
		return new PredefinedLocationByIdLoader(filter);
	}

	public PredefinedLocationLevelsLoader createPredefinedLocationLevelsLoader() {
		return new PredefinedLocationLevelsLoader(filter);
	}

	public PredefinedLocationListLoader createPredefinedLocationListLoader() {
		return new PredefinedLocationListLoader(filter);
	}

	public PredefinedLocationTreeLoader createPredefinedLocationTreeLoader() {
		return new PredefinedLocationTreeLoader(createPredefinedLocationListLoader());
	}

	public PrimaryOrgByTenantLoader createPrimaryOrgByTenantLoader() {
		return new PrimaryOrgByTenantLoader();
	}

	public AssetAttachmentListLoader createAssetAttachmentListLoader() {
		return new AssetAttachmentListLoader(filter);
	}

	public SyncAssetListLoader createAssetIdSearchListLoader() {
		return new SyncAssetListLoader(filter);
	}

	public AssetsByNetworkIdLoader createAssetsByNetworkIdLoader() {
		return new AssetsByNetworkIdLoader(filter);
	}

	public AssetExtensionListLoader createAssetExtensionListLoader() {
		return new AssetExtensionListLoader(filter);
	}

	public AssetStatusByNameLoader createAssetStatusByNameLoader() {
		return new AssetStatusByNameLoader(filter);
	}

	public AssetStatusFilteredLoader createAssetStatusFilteredLoader() {
		return new AssetStatusFilteredLoader(filter);
	}

	public AssetStatusForNameExistsLoader createAssetStatusForNameExistsLoader() {
		return new AssetStatusForNameExistsLoader(filter);
	}

	public AssetStatusListLoader createAssetStatusListLoader() {
		return new AssetStatusListLoader(filter);
	}

	public AssetTypeByAttachmentLoader createAssetTypeByAttachmentLoader() {
		return new AssetTypeByAttachmentLoader();
	}

	public AssetTypeListableLoader createAssetTypeListableLoader() {
		return new AssetTypeListableLoader(filter);
	}

	public AssetTypeLoader createAssetTypeLoader() {
		return new AssetTypeLoader(new TenantOnlySecurityFilter(filter.getTenantId()));
	}

	public SafetyNetworkInspectionLoader createSafetyNetworkAssignedAssetInspectionLoader() {
		return new SafetyNetworkAssignedAssetInspectionLoader(filter);
	}

	public SafetyNetworkAttachmentLoader createSafetyNetworkAttachmentLoader() {
		return new SafetyNetworkAttachmentLoader();
	}

	public SafetyNetworkBackgroundSearchLoader createSafetyNetworkBackgroundSearchLoader() {
		return new SafetyNetworkBackgroundSearchLoader(filter);
	}

	public SafetyNetworkInspectionLoader createSafetyNetworkInspectionLoader(boolean forAssignedAsset) {
		return (forAssignedAsset) ? createSafetyNetworkAssignedAssetInspectionLoader() : createSafetyNetworkRegisteredAssetInspectionLoader();
	}

	public SafetyNetworkRegisteredOrAssignedInspectionLoader createSafetyNetworkInspectionLoaderAssignedOrRegistered() {
		return new SafetyNetworkRegisteredOrAssignedInspectionLoader(filter);
	}

	public SafetyNetworkAssetAttachmentListLoader createSafetyNetworkAssetAttachmentListLoader() {
		return new SafetyNetworkAssetAttachmentListLoader();
	}

	public SafetyNetworkAssetAttachmentLoader createSafetyNetworkAssetAttachmentLoader() {
		return new SafetyNetworkAssetAttachmentLoader(filter);
	}

	public SafetyNetworkAssetLoader createSafetyNetworkAssetLoader() {
		return new SafetyNetworkAssetLoader(filter);
	}

	public SafetyNetworkAssetTypeLoader createSafetyNetworkAssetTypeLoader() {
		return new SafetyNetworkAssetTypeLoader();
	}

	public SafetyNetworkInspectionLoader createSafetyNetworkRegisteredAssetInspectionLoader() {
		return new SafetyNetworkRegisteredAssetInspectionLoader(filter);
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
	
	public SignupReferralListLoader createSignupReferralListLoader() {
		return new SignupReferralListLoader(filter);
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

	public UnreadMessageCountLoader createUnreadMessageCountLoader() {
		return new UnreadMessageCountLoader(filter);
	}

	public UserByFullNameLoader createUserByFullNameLoader() {
		return new UserByFullNameLoader(filter);
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

	public <T extends AbstractEntity> EntityByIdIncludingArchivedLoader<T> createEntityByIdLoader(Class<T> clazz) {
		return new EntityByIdIncludingArchivedLoader<T>(filter, clazz);
	}

    public PrimaryOrgsWithNameLikeLoader createPrimaryOrgsWithNameLikeLoader() {
        return new PrimaryOrgsWithNameLikeLoader(filter);
    }
    
    public SafetyNetworkPreAssignedAssetLoader createSafetyNetworkPreAssignedAssetLoader(){
    	return new SafetyNetworkPreAssignedAssetLoader();
    }

    public TypedOrgConnectionListLoader createdTypedOrgConnectionListLoader() {
        return new TypedOrgConnectionListLoader(filter);
    }

    public SafetyNetworkUnregisteredAssetCountLoader createUnregisteredAssetCountLoader() {
        return new SafetyNetworkUnregisteredAssetCountLoader();
    }

    public SafetyNetworkRegisteredAssetCountLoader createRegisteredAssetCountLoader() {
        return new SafetyNetworkRegisteredAssetCountLoader();
    }

    public InspectionsByAssetIdLoader createInspectionsByAssetIdLoader() {
        return new InspectionsByAssetIdLoader(filter);
    }

    public AssetAlreadyRegisteredLoader createAssetAlreadyRegisteredLoader() {
        return new AssetAlreadyRegisteredLoader(filter);
    }

}
