package com.n4systems.fieldidadmin.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.subscriptions.AccountHelper;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.fieldidadmin.utils.TenantPathUpdater;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.activesession.LastActiveSessionLoader;
import com.n4systems.model.asset.AssetCountLoader;
import com.n4systems.model.event.EventCountLoader;
import com.n4systems.model.orgs.AllPrimaryOrgsPaginatedLoader;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.TenantNameAvailabilityChecker;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.tenant.extendedfeatures.ToggleExendedFeatureMethod;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.TenantFinder;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.tools.Pager;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Validations
public class OrganizationAction extends AbstractCrud implements Preparable, HasDuplicateValueValidator {

	public OrganizationAction(com.n4systems.ejb.PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OrganizationAction.class);
	private static final int RESULTS_PER_PAGE = 15;

	private Long id;
	private Tenant tenant;
	private PrimaryOrg primaryOrg;
	private AccountHelper accountHelper;
	private TenantNameAvailabilityChecker tenantNameAvailablityChecker = new TenantNameAvailabilityChecker();

	private String title;
	private String note;
	private Map<String, Boolean> extendedFeatures = new HashMap<String, Boolean>();
	
	private AssetCountLoader assetCountLoader = new AssetCountLoader();
	private EventCountLoader eventCountLoader = new EventCountLoader();
	private LastActiveSessionLoader lastActiveSessionLoader = new LastActiveSessionLoader();

	Collection<PrimaryOrg> primaryOrgs = null;
	private Map<Long, Long> totalAssets = new HashMap<Long, Long>();
	private Map<Long, Long> total30DayAssets = new HashMap<Long, Long>();
	private Map<Long, Long> totalEvents = new HashMap<Long, Long>();
	private Map<Long, Long> total30DayEvents = new HashMap<Long, Long>();
	private Map<Long, ActiveSession> lastActiveSessions = new HashMap<Long, ActiveSession>();

	private String nameFilter;
	private String sortColumn;
	private String sortDirection;
	private String featureName;
	private boolean featureOn;
	private boolean showReminder = false;
	
	@Override
	protected void loadMemberFields(Long uniqueId) {}

	@Override
	protected void initMemberFields() {}
	
	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantFinder.getInstance().findTenant(id);
			
			getSecurityContext().setTenantSecurityFilter(new TenantOnlySecurityFilter(id));
			
			primaryOrg = TenantFinder.getInstance().findPrimaryOrg(id);
			for (ExtendedFeature feature : primaryOrg.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}
			accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(),
					getNonSecureLoaderFactory().createSignUpPackageListLoader());
		} else {
			for (PrimaryOrg org : getPage().getList()) {
				totalAssets.put(org.getId(), loadTotalAssets(org.getTenant().getId(), false));
				total30DayAssets.put(org.getId(), loadTotalAssets(org.getTenant().getId(), true));
				totalEvents.put(org.getId(), loadTotalEvents(org.getTenant().getId(), false));
				total30DayEvents.put(org.getId(), loadTotalEvents(org.getTenant().getId(), true));
				lastActiveSessions.put(org.getId(), loadLastActiveSession(org.getTenant().getId()));
			}
		}
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		if (tenant == null) {
			addActionError("You can only edit tenants");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		if (tenant == null) {
			addActionError("You can only edit tenants");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doUpdateExtendedFeature() throws Exception {
		OrgSaver orgSaver = new OrgSaver();

		Transaction transaction = PersistenceManager.startTransaction();

		try {
			if (primaryOrg.getId() != null) {
				processFeature(featureName, featureOn, transaction);
				extendedFeatures.put(featureName, featureOn);
			}

			orgSaver.update(transaction, primaryOrg);

			PersistenceManager.finishTransaction(transaction);

		} catch (Exception e) {
			PersistenceManager.rollbackTransaction(transaction);

			logger.error("Failed creating tenant", e);
			addActionError("Failed Creating Tenant: " + e.getMessage());
			return INPUT;

		}

		return SUCCESS;
	}

	@SkipValidation
	public String doEditNote() {
		return SUCCESS;
	}

	@SkipValidation
	public String doCancelNote() {
		return SUCCESS;
	}

	@SkipValidation
	public String doUpdateOrg() {
		updatePrimaryOrg();
		return SUCCESS;
	}

	@SkipValidation
	public String doUpdatePlansAndPricing() {
		updatePrimaryOrg();
		return SUCCESS;
	}
	
	@SkipValidation
	public String doUpdateSecondaryOrgs() {
		updateTenant();
		return SUCCESS;
	}

	@SkipValidation
	public String doUpdateTenantStatus() {
		updateTenant();
		return SUCCESS;
	}

	@SkipValidation
	public String doEditTenantName() {
		setShowReminder(false);
		return SUCCESS;
	}

	@SkipValidation
	public String doCancelTenantName() {
		setShowReminder(false);
		return SUCCESS;
	}

	public String doUpdateTenantName() {
		try {
			updateTenant();
			TenantPathUpdater tenantPathUpdater = new TenantPathUpdater();
			tenantPathUpdater.renameTenantPaths(primaryOrg.getTenant(), tenant.getName());
			primaryOrg = TenantFinder.getInstance().findPrimaryOrg(id);
			setShowReminder(true);
		} catch (Exception e) {
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEditPlan() {
		return SUCCESS;
	}

	@SkipValidation
	public String doCancelPlan() {
		return SUCCESS;
	}

	@SkipValidation
	public String doSavePlan() {
		try {
			UserLimits newSettings = tenant.getSettings().getUserLimits();
			userLimitService.updateUserLimits(newSettings.getMaxEmployeeUsers(), newSettings.getMaxLiteUsers(), newSettings.getMaxReadOnlyUsers());
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Failed to update user limits", e);
			return ERROR;
		}
	}

	private void updatePrimaryOrg() {
		new OrgSaver().update(primaryOrg);
	}

	private void updateTenant() {
		new TenantSaver().update(tenant);
	}

	private void processFeature(String featureName, boolean featureOn, Transaction transaction) {

		try {
			ExtendedFeature feature = ExtendedFeature.valueOf(featureName);

			new ToggleExendedFeatureMethod(feature, featureOn).applyTo(primaryOrg, transaction);

		} catch (IllegalArgumentException e) {
			addFieldError("extendedFeatures", "incorrect type of extended feature");
		} catch (Exception e) {
			addFieldError("extendedFeatures", "could not setup");
			throw new RuntimeException("problem while switching on or off an extended feature", e);
		}
	}

	public String doNote() throws Exception {
		SubscriptionAgent subscriptionAgent = getCreateHandlerFactory().getSubscriptionAgent();

		if (primaryOrg != null) {
			if (primaryOrg.getExternalId() != null) {
				subscriptionAgent.attachNote(primaryOrg.getExternalId(), title, note);
				title = null;
				note = null;
				addActionMessage("The note has been successfully attached");
			} else {
				addActionError("This organization is not attached to a netsuite account.");
				return INPUT;
			}
		} else {
			addActionError("You can only add a note to an organization that exists");
			return INPUT;
		}

		return SUCCESS;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}

	public void setPrimaryOrg(PrimaryOrg primaryOrg) {
		this.primaryOrg = primaryOrg;
	}
	
	public UserLimits getUserLimits() {
		return tenant.getSettings().getUserLimits();
	}

	public Pager<PrimaryOrg> getPage() {
		return new AllPrimaryOrgsPaginatedLoader().setPage(getCurrentPage()).setPageSize(RESULTS_PER_PAGE).setNameFilter(nameFilter)
				.setOrder(sortColumn, sortDirection != null ? sortDirection.equalsIgnoreCase("asc") : true).load();
	}

	public String getOtherDateFormat() {
		return (primaryOrg.getDateFormat() != null) ? DateHelper.java2Unix(primaryOrg.getDateFormat()) : "";
	}

	public String getFormattedDate() {
		return (primaryOrg.getDateFormat() != null) ? (new SimpleDateFormat(primaryOrg.getDateFormat())).format(new Date()) : "";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Map<String, Boolean> getExtendedFeatures() {
		return extendedFeatures;
	}

	public String getExtendedFeatureLabel(String name) {
		return ExtendedFeature.valueOf(name).getLabel();
	}

	public Boolean getExtendedFeatureState(String key) {
		return extendedFeatures.get(key) == null ? false : extendedFeatures.get(key);
	}

	public void setExtendedFeatures(Map<String, Boolean> extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	public List<ExtendedFeature> getAvailableExtendedFeatures() {
		List<ExtendedFeature> availableFeatures = new ArrayList<ExtendedFeature>();
		List<ExtendedFeature> removedFeatures = Arrays.asList(ExtendedFeature.CustomCert, ExtendedFeature.DedicatedProgramManager,
				ExtendedFeature.AllowIntegration);
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			if (!removedFeatures.contains(feature)) {
				availableFeatures.add(feature);
			}
		}
		return availableFeatures;
	}

	public boolean isSecondaryOrgsEnabled() {
		return tenant.getSettings().isSecondaryOrgsEnabled();
	}

	public void setDisabled(boolean disabled) {
		tenant.setDisabled(disabled);
	}

	public boolean isDisabled() {
		return tenant.isDisabled();
	}

	private Long loadTotalAssets(Long tenantId, boolean limit30Days) {
		return assetCountLoader.setTenantId(tenantId).setLimit30Days(limit30Days).load();
	}

	private Long loadTotalEvents(Long tenantId, boolean limit30Days) {
		return eventCountLoader.setTenantId(tenantId).setLimit30Days(limit30Days).load();
	}

	private ActiveSession loadLastActiveSession(Long tenantId) {
		return lastActiveSessionLoader.setTenant(tenantId).excludeN4User().load();
	}

	public Long getTotalAssets(Long orgId) {
		Long total = totalAssets.get(orgId);
		if (total == null)
			return loadTotalAssets(orgId, false);
		else
			return total;
	}

	public Long getTotal30DayAssets(Long orgId) {
		Long total = total30DayAssets.get(orgId);
		if (total == null)
			return loadTotalAssets(orgId, true);
		else
			return total30DayAssets.get(orgId);
	}

	public Long getTotalEvents(Long orgId) {
		Long total = totalEvents.get(orgId);
		if (total == null)
			return loadTotalEvents(orgId, false);
		else
			return total;
	}

	public Long getTotal30DayEvents(Long orgId) {
		Long total = total30DayEvents.get(orgId);
		if (total == null)
			return loadTotalEvents(orgId, true);
		else
			return total;
	}

	public ActiveSession getLastActiveSession(Long orgId) {
		ActiveSession activeSession = lastActiveSessions.get(orgId);
		if (activeSession == null)
			return loadLastActiveSession(orgId);
		else
			return activeSession;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public String getNameFilter() {
		return nameFilter;
	}

	public void setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
	}

	public UpgradePackageFilter currentPackageFilter() {
		return accountHelper.currentPackageFilter();
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public boolean isFeatureOn() {
		return featureOn;
	}

	public void setFeatureOn(boolean featureOn) {
		this.featureOn = featureOn;
	}

	public String getTenantName() {
		return tenant.getName();
	}

	@RequiredStringValidator(message = "", key = "error.tenant_name_required", shortCircuit = true)
	@StringLengthFieldValidator(message = "", key = "error.tenant_name_length", minLength = "3", maxLength = "255")
	@FieldExpressionValidator(expression = "validTenantName", message = "", key = "error.tenant_name_format")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.name_already_used")
	public void setTenantName(String name) {
		tenant.setName(name);
	}

	public boolean isValidTenantName() {
		String regex = "^[A-Za-z0-9][A-Za-z0-9\\-]+[A-Za-z0-9]$";
		return getTenantName().matches(regex);
	}

	public boolean duplicateValueExists(String formValue) {
		return !tenantNameAvailablityChecker.isAvailable(formValue);
	}

	public boolean isShowReminder() {
		return showReminder;
	}

	public void setShowReminder(boolean showReminder) {
		this.showReminder = showReminder;
	}

}
