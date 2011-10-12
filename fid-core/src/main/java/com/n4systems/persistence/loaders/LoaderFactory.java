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
import com.n4systems.model.downloadlink.DownloadsByDownloadIdLoader;
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
import com.n4systems.model.orgs.*;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.orgs.customer.CustomerOrgsWithNameLoader;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.orgs.internal.InternalOrgByNameLoader;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.legacy.LegacyEntityCreateModifyDate;
import com.n4systems.model.safetynetwork.*;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.signup.SignupReferralListLoader;
import com.n4systems.model.taskconfig.TaskConfigLoader;
import com.n4systems.model.tenant.PrimaryOrgsWithNameLikeLoader;
import com.n4systems.model.tenant.SetupDataLastModDatesLoader;
import com.n4systems.model.user.EmployeePaginatedLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByEmailLoader;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.model.user.UserFilteredLoader;
import com.n4systems.model.user.UserListLoader;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.tools.Pager;

/**
 * Provides simple access to creation of loaders.
 */
public class LoaderFactory implements Serializable {
	private static final long serialVersionUID = 5454994648412210255L;
	
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
	
	public SecurityFilter getSecurityFilter() {
		return filter;
	}

	public <T> AllEntityListLoader<T> createAllEntityListLoader(Class<T> clazz) {
		return new AllEntityListLoader<T>(clazz);
	}

	public AllEventBookListLoader createAllEventBookListLoader() {
		return new AllEventBookListLoader(filter);
	}

	public AllOrgsWithArchivedListLoader createAllOrgsWithArchivedListLoader() {
		return new AllOrgsWithArchivedListLoader(new TenantOnlySecurityFilter(filter));
	}

	public AllPredefinedLocationsPaginatedLoader createAllPredefinedLocationsPaginatedLoader() {
		return new AllPredefinedLocationsPaginatedLoader(filter);
	}

	public AssetAlreadyRegisteredLoader createAssetAlreadyRegisteredLoader() {
		return new AssetAlreadyRegisteredLoader(filter);
	}

	public AssetAttachmentListLoader createAssetAttachmentListLoader() {
		return new AssetAttachmentListLoader(filter);
	}

	public AssetExtensionListLoader createAssetExtensionListLoader() {
		return new AssetExtensionListLoader(filter);
	}

	public SyncAssetListLoader createAssetIdSearchListLoader() {
		return new SyncAssetListLoader(filter);
	}

	public AssetsByNetworkIdLoader createAssetsByNetworkIdLoader() {
		return new AssetsByNetworkIdLoader(filter);
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

	public AssetTypeGroupsLoader createAssetTypeGroupsLoader() {
		return new AssetTypeGroupsLoader(filter);
	}

	public AssetTypeListableLoader createAssetTypeListableLoader() {
		return new AssetTypeListableLoader(filter);
	}
	
	public AssetTypeListLoader createAssetTypeListLoader() {
		return new AssetTypeListLoader(filter);
	}

	public AssetTypeLoader createAssetTypeLoader() {
		return new AssetTypeLoader(new TenantOnlySecurityFilter(filter.getTenantId()));
	}

	public AssetTypesByAssetGroupIdLoader createAssetTypesByGroupListLoader() {
		return new AssetTypesByAssetGroupIdLoader(filter);
	}

	public AssociatedEventTypesLoader createAssociatedEventTypesLoader() {
		return new AssociatedEventTypesLoader(filter);
	}

	public AutoAttributeCriteriaByAssetTypeIdLoader createAutoAttributeCriteriaByAssetTypeIdLoader() {
		return new AutoAttributeCriteriaByAssetTypeIdLoader(filter);
	}

	public AutoAttributeCriteriaListLoader createAutoAttributeCriteriaListLoader() {
		return new AutoAttributeCriteriaListLoader(filter);
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

	public UserListableLoader createCombinedUserListableLoader() {
		return createUserListableLoader().setNoDeleted(true);
	}

	public CommentTemplateIdLoader createCommentTemplateIdLoader() {
		return new CommentTemplateIdLoader(filter);
	}

	public CommentTemplateListableLoader createCommentTemplateListableLoader() {
		return new CommentTemplateListableLoader(filter);
	}

	public CommentTemplateListLoader createCommentTemplateListLoader() {
		return new CommentTemplateListLoader(filter);
	}

	public UserListableLoader createCurrentCombinedUserListableLoader() {
		return createHistoricalCombinedUserListableLoader().setNoDeleted(true);
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

	public TypedOrgConnectionListLoader createdTypedOrgConnectionListLoader() {
		return new TypedOrgConnectionListLoader(filter);
	}

	public EmployeePaginatedLoader createEmployeePaginatedLoader() {
		return new EmployeePaginatedLoader(filter);
	}

	public <T extends AbstractEntity> EntityByIdIncludingArchivedLoader<T> createEntityByIdLoader(Class<T> clazz) {
		return new EntityByIdIncludingArchivedLoader<T>(filter, clazz);
	}

	public EventBookByNameLoader createEventBookByNameLoader() {
		return new EventBookByNameLoader(filter);
	}

	public EventBookFindOrCreateLoader createEventBookFindOrCreateLoader() {
		return new EventBookFindOrCreateLoader(filter);
	}

	public EventBookListLoader createEventBookListLoader() {
		return new EventBookListLoader(filter);
	}

	public EventFrequencyListLoader createEventFrequenciesListLoader() {
		return new EventFrequencyListLoader(filter);
	}

	public EventJobListableLoader createEventJobListableLoader() {
		return new EventJobListableLoader(filter);
	}

	public EventsByAssetIdLoader createEventsByAssetIdLoader() {
		return new EventsByAssetIdLoader(filter);
	}

	public EventTypeListableLoader createEventTypeListableLoader() {
		return new EventTypeListableLoader(filter);
	}

	public EventTypeListLoader createEventTypeListLoader() {
		return new EventTypeListLoader(filter);
	}

	public EventTypesByEventGroupIdLoader createEventTypesByGroupListLoader() {
		return new EventTypesByEventGroupIdLoader(filter);
	}

	public FileAttachmentLoader createFileAttachmentLoader() {
		return new FileAttachmentLoader(filter);
	}
	
	public DownloadsByDownloadIdLoader createPublicDownloadLinkLoader(){
		return new DownloadsByDownloadIdLoader(filter);
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

	public UserListableLoader createHistoricalCombinedUserListableLoader() {
		return createUserListableLoader();
	}

	public UserListableLoader createHistoricalEmployeesListableLoader() {
		return createUserListableLoader().employeesOnly();
	}

	public IncompleteEventSchedulesListLoader createIncompleteEventSchedulesListLoader() {
		return new IncompleteEventSchedulesListLoader(filter);
	}

	public InternalOrgByNameLoader createInternalOrgByNameLoader() {
		return new InternalOrgByNameLoader(filter);
	}

	public InternalOrgListableLoader createInternalOrgListableLoader() {
		return new InternalOrgListableLoader(filter);
	}

	public LastEventLoader createLastEventLoader() {
		return new LastEventLoader(filter);
	}

	public LastModifiedListLoader createLastModifiedListLoader(Class<? extends AbstractEntity> clazz) { 
		return new LastModifiedListLoader(filter, clazz);
	}

	public LatestEulaAcceptanceLoader createLatestEulaAcceptanceLoader() {
		return new LatestEulaAcceptanceLoader(filter);
	}

	public <T extends LegacyEntityCreateModifyDate> LegacyLastModifiedListLoader<T> createLegacyLastModifiedListLoader(Class<T> clazz) {
		return new LegacyLastModifiedListLoader<T>(filter, clazz);
	}

	public NextEventDateByEventLoader createNextEventDateByEventLoader() {
		return new NextEventDateByEventLoader(filter);
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

	public PrimaryOrgsWithNameLikeLoader createPrimaryOrgsWithNameLikeLoader() {
		return new PrimaryOrgsWithNameLikeLoader(filter);
	}

	public SafetyNetworkRegisteredAssetCountLoader createRegisteredAssetCountLoader() {
		return new SafetyNetworkRegisteredAssetCountLoader();
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

	public SafetyNetworkEventLoader createSafetyNetworkAssignedAssetEventLoader() {
		return new SafetyNetworkAssignedAssetEventLoader(filter);
	}

	public SafetyNetworkAttachmentLoader createSafetyNetworkAttachmentLoader() {
		return new SafetyNetworkAttachmentLoader();
	}

	public SafetyNetworkBackgroundSearchLoader createSafetyNetworkBackgroundSearchLoader() {
		return new SafetyNetworkBackgroundSearchLoader(filter);
	}
	
	public SafetyNetworkEventLoader createSafetyNetworkEventLoader(boolean forAssignedAsset) {
		return (forAssignedAsset) ? createSafetyNetworkAssignedAssetEventLoader() : createSafetyNetworkRegisteredAssetEventLoader();
	}

	public SafetyNetworkRegisteredOrAssignedEventLoader createSafetyNetworkEventLoaderAssignedOrRegistered() {
		return new SafetyNetworkRegisteredOrAssignedEventLoader(filter);
	}

	public SafetyNetworkPreAssignedAssetLoader createSafetyNetworkPreAssignedAssetLoader() {
		return new SafetyNetworkPreAssignedAssetLoader();
	}

    public AllPreAssignedAssetsLoader createAllPreAssignedAssetsLoader() {
        return new AllPreAssignedAssetsLoader(filter);
    }

	public SafetyNetworkEventLoader createSafetyNetworkRegisteredAssetEventLoader() {
		return new SafetyNetworkRegisteredAssetEventLoader(filter);
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

	public SetupDataLastModDatesLoader createSetupDataLastModDatesLoader() {
		return new SetupDataLastModDatesLoader(filter);
	}

	public SignupReferralListLoader createSignupReferralListLoader() {
		return new SignupReferralListLoader(filter);
	}

	public SmartSearchLoader createSmartSearchListLoader() {
		return new SmartSearchLoader(filter);
	}

	public SmartSearchPagedLoader createSmartSearchPagedLoader(){
		return new SmartSearchPagedLoader(filter);
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

	public SafetyNetworkUnregisteredAssetCountLoader createUnregisteredAssetCountLoader() {
		return new SafetyNetworkUnregisteredAssetCountLoader();
	}

	public UserByFullNameLoader createUserByFullNameLoader() {
		return new UserByFullNameLoader(filter);
	}
	
	public UserByEmailLoader createUserByEmailLoader(){
		return new UserByEmailLoader(filter);
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

    public CustomerOrgsWithNameLoader createCustomerOrgWithNameLoader() {
        return new CustomerOrgsWithNameLoader(filter);
    }

	public ListLoader<User> createUserListLoader() {
		return new UserListLoader(filter);
	}

	public AssetsByIdOwnerTypeLoader createAssetByIdOwnerTypeLoader() { 
		return new AssetsByIdOwnerTypeLoader(filter);
	}
}
