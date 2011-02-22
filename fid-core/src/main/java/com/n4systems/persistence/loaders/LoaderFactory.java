package com.n4systems.persistence.loaders;

import java.io.Serializable;
import java.util.List;

import com.n4systems.model.api.Exportable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.asset.AssetAttachmentListLoader;
import com.n4systems.model.asset.AssetExtensionListLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.asset.SmartSearchPagedLoader;
import com.n4systems.model.asset.SyncAssetListLoader;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.assetstatus.AssetStatusFilteredLoader;
import com.n4systems.model.assetstatus.AssetStatusForNameExistsLoader;
import com.n4systems.model.assetstatus.AssetStatusListLoader;
import com.n4systems.model.assettype.AssetTypeByAttachmentLoader;
import com.n4systems.model.assettype.AssetTypeGroupsLoader;
import com.n4systems.model.assettype.AssetTypeListLoader;
import com.n4systems.model.assettype.AssetTypeListableLoader;
import com.n4systems.model.assettype.AssetTypeLoader;
import com.n4systems.model.assettype.AssetTypesByAssetGroupIdLoader;
import com.n4systems.model.assettype.AutoAttributeCriteriaByAssetTypeIdLoader;
import com.n4systems.model.assettype.EventFrequencyListLoader;
import com.n4systems.model.autoattribute.AutoAttributeCriteriaListLoader;
import com.n4systems.model.autoattribute.AutoAttributeDefinitionListLoader;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.commenttemplate.CommentTemplateIdLoader;
import com.n4systems.model.commenttemplate.CommentTemplateListLoader;
import com.n4systems.model.commenttemplate.CommentTemplateListableLoader;
import com.n4systems.model.downloadlink.DownloadLinkListLoader;
import com.n4systems.model.eula.CurrentEulaLoader;
import com.n4systems.model.eula.LatestEulaAcceptanceLoader;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;
import com.n4systems.model.event.LastEventLoader;
import com.n4systems.model.eventbook.AllEventBookListLoader;
import com.n4systems.model.eventbook.EventBookByNameLoader;
import com.n4systems.model.eventbook.EventBookFindOrCreateLoader;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.eventschedule.IncompleteEventSchedulesListLoader;
import com.n4systems.model.eventschedule.NextEventDateByEventLoader;
import com.n4systems.model.eventtype.AssociatedEventTypesLoader;
import com.n4systems.model.eventtype.EventTypeListLoader;
import com.n4systems.model.eventtype.EventTypeListableLoader;
import com.n4systems.model.fileattachment.FileAttachmentLoader;
import com.n4systems.model.jobs.EventJobListableLoader;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.lastmodified.LegacyLastModifiedListLoader;
import com.n4systems.model.location.AllPredefinedLocationsPaginatedLoader;
import com.n4systems.model.location.PredefinedLocationByIdLoader;
import com.n4systems.model.location.PredefinedLocationListLoader;
import com.n4systems.model.location.PredefinedLocationTreeLoader;
import com.n4systems.model.messages.PaginatedMessageLoader;
import com.n4systems.model.messages.UnreadMessageCountLoader;
import com.n4systems.model.notificationsettings.NotificationSettingByUserListLoader;
import com.n4systems.model.orgs.AllOrgsWithArchivedListLoader;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.CustomerOrgWithArchivedPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.EntityByIdIncludingArchivedLoader;
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
import com.n4systems.model.parents.legacy.LegacyEntityCreateModifyDate;
import com.n4systems.model.safetynetwork.AssetAlreadyRegisteredLoader;
import com.n4systems.model.safetynetwork.AssetsByNetworkIdLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgListLoader;
import com.n4systems.model.safetynetwork.CustomerLinkedOrgLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionLoader;
import com.n4systems.model.safetynetwork.CustomerOrgConnectionsListLoader;
import com.n4systems.model.safetynetwork.EventsByAssetIdLoader;
import com.n4systems.model.safetynetwork.HasLinkedAssetsLoader;
import com.n4systems.model.safetynetwork.PaginatedConnectionListLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetAttachmentListLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetAttachmentLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssetTypeLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAssignedAssetEventLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkAttachmentLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkBackgroundSearchLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkEventLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkPreAssignedAssetLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredAssetCountLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredAssetEventLoader;
import com.n4systems.model.safetynetwork.SafetyNetworkRegisteredOrAssignedEventLoader;
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
import com.n4systems.model.tenant.SetupDataLastModDatesLoader;
import com.n4systems.model.user.EmployeePaginatedLoader;
import com.n4systems.model.user.UserByEmailLoader;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.tools.Pager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory implements Serializable {

	private SecurityFilter filter;

    @PersistenceContext
    private EntityManager entityManager;

    public LoaderFactory() {

    }

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

	public SecurityFilter getSecurityFilter() {
		return filter;
	}

    public void setSecurityFilter(SecurityFilter filter) {
        this.filter = filter;
    }

	public <T> AllEntityListLoader<T> createAllEntityListLoader(Class<T> clazz) {
		return injectLoader(new AllEntityListLoader<T>(clazz));
	}

	public AllEventBookListLoader createAllEventBookListLoader() {
		return injectLoader(new AllEventBookListLoader(filter));
	}

	public AllOrgsWithArchivedListLoader createAllOrgsWithArchivedListLoader() {
		return injectLoader(new AllOrgsWithArchivedListLoader(new TenantOnlySecurityFilter(filter)));
	}

	public AllPredefinedLocationsPaginatedLoader createAllPredefinedLocationsPaginatedLoader() {
		return injectLoader(new AllPredefinedLocationsPaginatedLoader(filter));
	}

	public AssetAlreadyRegisteredLoader createAssetAlreadyRegisteredLoader() {
		return injectLoader(new AssetAlreadyRegisteredLoader(filter));
	}

	public AssetAttachmentListLoader createAssetAttachmentListLoader() {
		return injectLoader(new AssetAttachmentListLoader(filter));
	}

	public AssetExtensionListLoader createAssetExtensionListLoader() {
		return injectLoader(new AssetExtensionListLoader(filter));
	}

	public SyncAssetListLoader createAssetIdSearchListLoader() {
		return injectLoader(new SyncAssetListLoader(filter));
	}

	public AssetsByNetworkIdLoader createAssetsByNetworkIdLoader() {
		return injectLoader(new AssetsByNetworkIdLoader(filter));
	}

	public AssetStatusByNameLoader createAssetStatusByNameLoader() {
		return injectLoader(new AssetStatusByNameLoader(filter));
	}

	public AssetStatusFilteredLoader createAssetStatusFilteredLoader() {
		return injectLoader(new AssetStatusFilteredLoader(filter));
	}

	public AssetStatusForNameExistsLoader createAssetStatusForNameExistsLoader() {
		return injectLoader(new AssetStatusForNameExistsLoader(filter));
	}

	public AssetStatusListLoader createAssetStatusListLoader() {
		return injectLoader(new AssetStatusListLoader(filter));
	}

	public AssetTypeByAttachmentLoader createAssetTypeByAttachmentLoader() {
		return injectLoader(new AssetTypeByAttachmentLoader());
	}

	public AssetTypeGroupsLoader createAssetTypeGroupsLoader() {
		return injectLoader(new AssetTypeGroupsLoader(filter));
	}

	public AssetTypeListableLoader createAssetTypeListableLoader() {
		return injectLoader(new AssetTypeListableLoader(filter));
	}
	
	public AssetTypeListLoader createAssetTypeListLoader() {
		return injectLoader(new AssetTypeListLoader(filter));
	}

	public AssetTypeLoader createAssetTypeLoader() {
		return injectLoader(new AssetTypeLoader(new TenantOnlySecurityFilter(filter.getTenantId())));
	}

	public AssetTypesByAssetGroupIdLoader createAssetTypesByGroupListLoader() {
		return injectLoader(new AssetTypesByAssetGroupIdLoader(filter));
	}

	public AssociatedEventTypesLoader createAssociatedEventTypesLoader() {
		return injectLoader(new AssociatedEventTypesLoader(filter));
	}

	public AutoAttributeCriteriaByAssetTypeIdLoader createAutoAttributeCriteriaByAssetTypeIdLoader() {
		return injectLoader(new AutoAttributeCriteriaByAssetTypeIdLoader(filter));
	}

	public AutoAttributeCriteriaListLoader createAutoAttributeCriteriaListLoader() {
		return injectLoader(new AutoAttributeCriteriaListLoader(filter));
	}

	public AutoAttributeDefinitionListLoader createAutoAttributeDefinitionListLoader() {
		return injectLoader(new AutoAttributeDefinitionListLoader(filter));
	}

	public BaseOrgParentFilterListLoader createBaseParentFilterLoader() {
		return injectLoader(new BaseOrgParentFilterListLoader(filter));
	}

	public CatalogLoader createCatalogLoader() {
		return injectLoader(new CatalogLoader());
	}

	public UserListableLoader createCombinedUserListableLoader() {
		return injectLoader(createUserListableLoader().setNoDeleted(true));
	}

	public CommentTemplateIdLoader createCommentTemplateIdLoader() {
		return injectLoader(new CommentTemplateIdLoader(filter));
	}

	public CommentTemplateListableLoader createCommentTemplateListableLoader() {
		return injectLoader(new CommentTemplateListableLoader(filter));
	}

	public CommentTemplateListLoader createCommentTemplateListLoader() {
		return injectLoader(new CommentTemplateListLoader(filter));
	}

	public UserListableLoader createCurrentCombinedUserListableLoader() {
		return injectLoader(createHistoricalCombinedUserListableLoader().setNoDeleted(true));
	}

	public UserListableLoader createCurrentEmployeesListableLoader() {
		return injectLoader(createHistoricalEmployeesListableLoader().setNoDeleted(true));
	}

	public CurrentEulaLoader createCurrentEulaLoader() {
		return injectLoader(new CurrentEulaLoader());
	}

	public CustomerLinkedOrgListLoader createCustomerLinkedOrgListLoader() {
		return injectLoader(new CustomerLinkedOrgListLoader(filter));
	}

	public CustomerLinkedOrgLoader createCustomerLinkedOrgLoader() {
		return injectLoader(new CustomerLinkedOrgLoader(filter));
	}

	public CustomerOrgConnectionLoader createCustomerOrgConnectionLoader() {
		return injectLoader(new CustomerOrgConnectionLoader(filter));
	}

	public CustomerOrgConnectionsListLoader createCustomerOrgConnectionsListLoader() {
		return injectLoader(new CustomerOrgConnectionsListLoader(filter));
	}

	public CustomerOrgListLoader createCustomerOrgListLoader() {
		return injectLoader(new CustomerOrgListLoader(filter));
	}

	public CustomerOrgPaginatedLoader createCustomerOrgPaginatedLoader() {
		return injectLoader(new CustomerOrgPaginatedLoader(filter));
	}

	public CustomerOrgWithArchivedPaginatedLoader createCustomerOrgWithArchivedPaginatedLoader() {
		return injectLoader(new CustomerOrgWithArchivedPaginatedLoader(filter));
	}

	public DivisionOrgByCustomerListLoader createDivisionOrgByCustomerListLoader() {
		return injectLoader(new DivisionOrgByCustomerListLoader(filter));
	}

	public DivisionOrgPaginatedLoader createDivisionOrgPaginatedLoader() {
		return injectLoader(new DivisionOrgPaginatedLoader(filter));
	}

	public DownloadLinkListLoader createDownloadLinkListLoader() {
		return injectLoader(new DownloadLinkListLoader(filter));
	}

	public TypedOrgConnectionListLoader createdTypedOrgConnectionListLoader() {
		return injectLoader(new TypedOrgConnectionListLoader(filter));
	}

	public EmployeePaginatedLoader createEmployeePaginatedLoader() {
		return injectLoader(new EmployeePaginatedLoader(filter));
	}

	public <T extends AbstractEntity> EntityByIdIncludingArchivedLoader<T> createEntityByIdLoader(Class<T> clazz) {
		return injectLoader(new EntityByIdIncludingArchivedLoader<T>(filter, clazz));
	}

	public EventBookByNameLoader createEventBookByNameLoader() {
		return injectLoader(new EventBookByNameLoader(filter));
	}

	public EventBookFindOrCreateLoader createEventBookFindOrCreateLoader() {
		return injectLoader(new EventBookFindOrCreateLoader(filter));
	}

	public EventBookListLoader createEventBookListLoader() {
		return injectLoader(new EventBookListLoader(filter));
	}

	public EventFrequencyListLoader createEventFrequenciesListLoader() {
		return injectLoader(new EventFrequencyListLoader(filter));
	}

	public EventJobListableLoader createEventJobListableLoader() {
		return injectLoader(new EventJobListableLoader(filter));
	}

	public EventsByAssetIdLoader createEventsByAssetIdLoader() {
		return injectLoader(new EventsByAssetIdLoader(filter));
	}

	public EventTypeListableLoader createEventTypeListableLoader() {
		return injectLoader(new EventTypeListableLoader(filter));
	}

	public EventTypeListLoader createEventTypeListLoader() {
		return injectLoader(new EventTypeListLoader(filter));
	}

	public EventTypesByEventGroupIdLoader createEventTypesByGroupListLoader() {
		return injectLoader(new EventTypesByEventGroupIdLoader(filter));
	}

	public FileAttachmentLoader createFileAttachmentLoader() {
		return injectLoader(new FileAttachmentLoader(filter));
	}

	public <T extends AbstractEntity> FilteredIdLoader<T> createFilteredIdLoader(Class<T> clazz) {
		return injectLoader(new FilteredIdLoader<T>(filter, clazz));
	}

	public <T> FilteredInListLoader<T> createFilteredInListLoader(Class<T> clazz) {
		return injectLoader(new FilteredInListLoader<T>(filter, clazz));
	}

	public FilteredListableLoader createFilteredListableLoader(Class<? extends NamedEntity> clazz) {
		return injectLoader(new FilteredListableLoader(filter, clazz));
	}
	
	public <T extends Exportable> GlobalIdLoader<T> createGlobalIdLoader(Class<T> clazz) {
		return injectLoader(new GlobalIdLoader<T>(filter, clazz));
	}

	public HasLinkedAssetsLoader createHasLinkedAssetsLoader() {
		return injectLoader(new HasLinkedAssetsLoader(filter));
	}

	public UserListableLoader createHistoricalCombinedUserListableLoader() {
		return injectLoader(createUserListableLoader());
	}

	public UserListableLoader createHistoricalEmployeesListableLoader() {
		return injectLoader(createUserListableLoader().employeesOnly());
	}

	public IncompleteEventSchedulesListLoader createIncompleteEventSchedulesListLoader() {
		return injectLoader(new IncompleteEventSchedulesListLoader(filter));
	}

	public InternalOrgByNameLoader createInternalOrgByNameLoader() {
		return injectLoader(new InternalOrgByNameLoader(filter));
	}

	public InternalOrgListableLoader createInternalOrgListableLoader() {
		return injectLoader(new InternalOrgListableLoader(filter));
	}

	public LastEventLoader createLastEventLoader() {
		return injectLoader(new LastEventLoader(filter));
	}

	public LastModifiedListLoader createLastModifiedListLoader(Class<? extends AbstractEntity> clazz) { 
		return injectLoader(new LastModifiedListLoader(filter, clazz));
	}

	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return injectLoader(new LatestEulaAcceptanceLoader(filter));
	}

	public <T extends LegacyEntityCreateModifyDate> LegacyLastModifiedListLoader<T> createLegacyLastModifiedListLoader(Class<T> clazz) {
		return injectLoader(new LegacyLastModifiedListLoader<T>(filter, clazz));
	}

	public NextEventDateByEventLoader createNextEventDateByEventLoader() {
		return injectLoader(new NextEventDateByEventLoader(filter));
	}

	public NotificationSettingByUserListLoader createNotificationSettingByUserListLoader() {
		return injectLoader(new NotificationSettingByUserListLoader(filter));
	}

	public OrgByNameLoader createOrgByNameLoader() {
		return injectLoader(new OrgByNameLoader(filter));
	}

	public Loader<Pager<TypedOrgConnection>> createPaginatedConnectionListLoader() {
		return injectLoader(new PaginatedConnectionListLoader(filter));
	}

	public PaginatedMessageLoader createPaginatedMessageLoader() {
		return injectLoader(new PaginatedMessageLoader(filter));
	}

	public <T> PassthruListLoader<T> createPassthruListLoader(List<T> entities) {
		return injectLoader(new PassthruListLoader<T>(entities));
	}

	public PredefinedLocationByIdLoader createPredefinedLocationByIdLoader() {
		return injectLoader(new PredefinedLocationByIdLoader(filter));
	}

	public PredefinedLocationLevelsLoader createPredefinedLocationLevelsLoader() {
		return injectLoader(new PredefinedLocationLevelsLoader(filter));
	}

	public PredefinedLocationListLoader createPredefinedLocationListLoader() {
		return injectLoader(new PredefinedLocationListLoader(filter));
	}

	public PredefinedLocationTreeLoader createPredefinedLocationTreeLoader() {
		return injectLoader(new PredefinedLocationTreeLoader(createPredefinedLocationListLoader()));
	}

	public PrimaryOrgByTenantLoader createPrimaryOrgByTenantLoader() {
		return injectLoader(new PrimaryOrgByTenantLoader());
	}

	public PrimaryOrgsWithNameLikeLoader createPrimaryOrgsWithNameLikeLoader() {
		return injectLoader(new PrimaryOrgsWithNameLikeLoader(filter));
	}

	public SafetyNetworkRegisteredAssetCountLoader createRegisteredAssetCountLoader() {
		return injectLoader(new SafetyNetworkRegisteredAssetCountLoader());
	}

	public SafetyNetworkAssetAttachmentListLoader createSafetyNetworkAssetAttachmentListLoader() {
		return injectLoader(new SafetyNetworkAssetAttachmentListLoader());
	}

	public SafetyNetworkAssetAttachmentLoader createSafetyNetworkAssetAttachmentLoader() {
		return injectLoader(new SafetyNetworkAssetAttachmentLoader(filter));
	}

	public SafetyNetworkAssetLoader createSafetyNetworkAssetLoader() {
		return injectLoader(new SafetyNetworkAssetLoader(filter));
	}

	public SafetyNetworkAssetTypeLoader createSafetyNetworkAssetTypeLoader() {
		return injectLoader(new SafetyNetworkAssetTypeLoader());
	}

	public SafetyNetworkEventLoader createSafetyNetworkAssignedAssetEventLoader() {
		return injectLoader(new SafetyNetworkAssignedAssetEventLoader(filter));
	}

	public SafetyNetworkAttachmentLoader createSafetyNetworkAttachmentLoader() {
		return injectLoader(new SafetyNetworkAttachmentLoader());
	}

	public SafetyNetworkBackgroundSearchLoader createSafetyNetworkBackgroundSearchLoader() {
		return injectLoader(new SafetyNetworkBackgroundSearchLoader(filter));
	}
	
	public SafetyNetworkEventLoader createSafetyNetworkEventLoader(boolean forAssignedAsset) {
		return (forAssignedAsset) ? createSafetyNetworkAssignedAssetEventLoader() : createSafetyNetworkRegisteredAssetEventLoader();
	}

	public SafetyNetworkRegisteredOrAssignedEventLoader createSafetyNetworkEventLoaderAssignedOrRegistered() {
		return injectLoader(new SafetyNetworkRegisteredOrAssignedEventLoader(filter));
	}

	public SafetyNetworkPreAssignedAssetLoader createSafetyNetworkPreAssignedAssetLoader() {
		return injectLoader(new SafetyNetworkPreAssignedAssetLoader());
	}

	public SafetyNetworkEventLoader createSafetyNetworkRegisteredAssetEventLoader() {
		return injectLoader(new SafetyNetworkRegisteredAssetEventLoader(filter));
	}

	public SafetyNetworkSmartSearchLoader createSafetyNetworkSmartSearchLoader() {
		return injectLoader(new SafetyNetworkSmartSearchLoader(filter));
	}

	public SecondaryOrgByNameLoader createSecondaryOrgByNameLoader() {
		return injectLoader(new SecondaryOrgByNameLoader(filter));
	}

	public SecondaryOrgListableLoader createSecondaryOrgListableLoader() {
		return injectLoader(new SecondaryOrgListableLoader(filter));
	}

	public SecondaryOrgPaginatedLoader createSecondaryOrgPaginatedLoader() {
		return injectLoader(new SecondaryOrgPaginatedLoader(filter));
	}

	public SetupDataLastModDatesLoader createSetupDataLastModDatesLoader() {
		return injectLoader(new SetupDataLastModDatesLoader(filter));
	}

	public SignupReferralListLoader createSignupReferralListLoader() {
		return injectLoader(new SignupReferralListLoader(filter));
	}

	public SmartSearchLoader createSmartSearchListLoader() {
		return injectLoader(new SmartSearchLoader(filter));
	}

	public SmartSearchPagedLoader createSmartSearchPagedLoader(){
		return injectLoader(new SmartSearchPagedLoader(filter));
	}

	public TaskConfigLoader createTaskConfigLoader() {
		return injectLoader(new TaskConfigLoader());
	}

	public TenantWideVendorOrgConnPaginatedLoader createTenantWideVendorOrgConnPaginatedLoader() {
		return injectLoader(new TenantWideVendorOrgConnPaginatedLoader(filter));
	}

	public UnreadMessageCountLoader createUnreadMessageCountLoader() {
		return injectLoader(new UnreadMessageCountLoader(filter));
	}

	public SafetyNetworkUnregisteredAssetCountLoader createUnregisteredAssetCountLoader() {
		return injectLoader(new SafetyNetworkUnregisteredAssetCountLoader());
	}

	public UserByFullNameLoader createUserByFullNameLoader() {
		return injectLoader(new UserByFullNameLoader(filter));
	}
	
	public UserByEmailLoader createUserByEmailLoader(){
		return injectLoader(new UserByEmailLoader(filter));
	}

	public UserFilteredLoader createUserFilteredLoader() {
		return injectLoader(new UserFilteredLoader(filter));
	}
	
	public UserListableLoader createUserListableLoader() {
		return injectLoader(new UserListableLoader(filter));
	}
	
	public VendorLinkedOrgListLoader createVendorLinkedOrgListLoader() {
		return injectLoader(new VendorLinkedOrgListLoader(filter));
	}
	
	public VendorLinkedOrgLoader createVendorLinkedOrgLoader() {
		return injectLoader(new VendorLinkedOrgLoader(filter));
	}
	
	public VendorOrgConnectionLoader createVendorOrgConnectionLoader() {
		return injectLoader(new VendorOrgConnectionLoader(filter));
	}
	
	public VendorOrgConnectionsListLoader createVendorOrgConnectionsListLoader() {
		return injectLoader(new VendorOrgConnectionsListLoader(filter));
	}

    private <T extends Loader> T injectLoader(T loader) {
        loader.setEntityManager(entityManager);
        return loader;
    }
}
