package com.n4systems.fieldidadmin.actions;

import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.service.tenant.ExtendedFeatureService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.fieldidadmin.utils.TenantPathUpdater;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.activesession.LastLoggedInUserByTenantLoader;
import com.n4systems.model.asset.AssetCountLoader;
import com.n4systems.model.event.EventCountLoader;
import com.n4systems.model.orgs.AllPrimaryOrgsPaginatedLoader;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantNameAvailabilityChecker;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.User;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.search.AssetIndexerService;
import com.n4systems.services.search.CriteriaTrendsIndexerService;
import com.n4systems.services.search.EventIndexerService;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.timezone.CountryList;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

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
	private TenantNameAvailabilityChecker tenantNameAvailablityChecker = new TenantNameAvailabilityChecker();
    private String inactiveSince;
    private boolean activeOnly;

	private String title;
	private String note;
	private Map<String, Boolean> extendedFeatures;

	private AssetCountLoader assetCountLoader = new AssetCountLoader();
	private EventCountLoader eventCountLoader = new EventCountLoader();
	private LastLoggedInUserByTenantLoader lastLoggedInUserByTenantLoader = new LastLoggedInUserByTenantLoader();

	private String nameFilter;
	private String sortColumn;
	private String sortDirection;
	private String featureName;
	private boolean featureOn;
	private boolean showReminder = false;

    private Pager<PrimaryOrg> pager;

	@Autowired
	private TenantSettingsService tenantSettingsService;
    @Autowired
    private ExtendedFeatureService extendedFeatureService;
    @Autowired
    private AssetIndexerService assetIndexerService;
    @Autowired
    private EventIndexerService eventIndexerService;
    @Autowired
    private CriteriaTrendsIndexerService criteriaTrendsIndexerService;

	@Override
	protected void loadMemberFields(Long uniqueId) {}

	@Override
	protected void initMemberFields() {}

	public void prepare() throws Exception {
		if (id != null) {
			tenant = TenantFinder.getInstance().findTenant(id);
			getSecurityContext().setTenantSecurityFilter(new TenantOnlySecurityFilter(id));
			primaryOrg = TenantFinder.getInstance().findPrimaryOrg(id);
		}
	}

    @SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doAjax() {
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
        ExtendedFeature feature = ExtendedFeature.valueOf(featureName);
        extendedFeatureService.setExtendedFeatureEnabled(primaryOrg.getTenant().getId(), feature, featureOn);

        // Must reload the primary org to display properly updated extended feature status.
        prepare();

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
        updateTenantSettings();
        return SUCCESS;
	}

    @SkipValidation
	public String doUpdateTenantStatus() {
		updateTenant();
		return SUCCESS;
	}

	@SkipValidation
	public String doUpdateSignUp() {
		updatePrimaryOrg();
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

	@SkipValidation
	public String doEditSignUp() {
		return SUCCESS;
	}

	@SkipValidation
	public String doCancelSignUp() {
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
			userLimitService.updateUserLimits(newSettings.getMaxEmployeeUsers(),
                                            newSettings.getMaxLiteUsers(),
                                            newSettings.getMaxReadOnlyUsers(),
                                            newSettings.isUsageBasedUsersEnabled(),
                                            newSettings.getUsageBasedUserEvents());
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Failed to update user limits", e);
			return ERROR;
		}
	}

    @SkipValidation
    public String doRebuildIndex() {
        Tenant t = persistenceManager.find(Tenant.class, getTenantId());
        t.setAssetIndexerStarted(true);
        persistenceManager.update(t);
        assetIndexerService.reindexTenant(getTenant());
        return SUCCESS;
    }

    @SkipValidation
    public String doRebuildCriteriaTrendsIndex() {
        criteriaTrendsIndexerService.placeItemInQueueForTenant(tenant.getId());
        return SUCCESS;
    }

    @SkipValidation
    public String doRebuildEventIndex() {
        eventIndexerService.placeItemInQueueForTenant(tenant.getId());
        return SUCCESS;
    }

    @SkipValidation
    public String doToggleInspectionsEnabled() {
        updateTenantSettings();
        return SUCCESS;
    }

    @SkipValidation
    public String doToggleLotoEnabled() {
        updateTenantSettings();
        return SUCCESS;
    }

    private void updateTenantSettings() {
        tenantSettingsService.update(tenant.getSettings());
    }


    private void updatePrimaryOrg() {
		new OrgSaver().update(primaryOrg);
	}

	private void updateTenant() {
		new TenantSaver().update(tenant);
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
        if (pager == null) {
            pager = new AllPrimaryOrgsPaginatedLoader().setPage(getCurrentPage()).setPageSize(RESULTS_PER_PAGE).setNameFilter(nameFilter)
                    .setInactiveSince(StringUtils.isBlank(inactiveSince) ? null : RangeType.valueOf(inactiveSince))
                    .setActiveOnly(this.activeOnly)
                    .setOrder(sortColumn, sortDirection != null ? sortDirection.equalsIgnoreCase("asc") : true).load();
        }
        return pager;
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
		if (extendedFeatures == null) {
			extendedFeatures = new HashMap<String, Boolean>();
			for (ExtendedFeature feature : primaryOrg.getExtendedFeatures()) {
				extendedFeatures.put(feature.name(), true);
			}
		}
		return extendedFeatures;
	}

	public String getExtendedFeatureLabel(String name) {
		return ExtendedFeature.valueOf(name).getLabel();
	}

	public Boolean getExtendedFeatureState(String key) {
		Map<String, Boolean> extFeatures = getExtendedFeatures();
		return extFeatures.get(key) == null ? false : extFeatures.get(key);
	}

	public void setExtendedFeatures(Map<String, Boolean> extendedFeatures) {
		this.extendedFeatures = extendedFeatures;
	}

	public List<ExtendedFeature> getAvailableExtendedFeatures() {
		return Arrays.asList(ExtendedFeature.values());
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

	public Long getTotalAssets(PrimaryOrg org) {
		return loadTotalAssets(org.getTenant().getId(), false);
	}

	public Long getTotal30DayAssets(PrimaryOrg org) {
		return loadTotalAssets(org.getTenant().getId(), true);
	}

	public Long getTotal30DayAssets() {
		return loadTotalAssets(id, true);
	}

	public Long getTotalEvents(PrimaryOrg org) {
		return loadTotalEvents(org.getTenant().getId(), false);
	}

	public Long getTotal30DayEvents(PrimaryOrg org) {
		return loadTotalEvents(org.getTenant().getId(), true);
	}

	public Long getTotal30DayEvents() {
		return loadTotalEvents(id, true);
	}

	public User getLastLoggedInUser(PrimaryOrg org) {
		return lastLoggedInUserByTenantLoader.setTenant(org.getTenant().getId()).excludeN4User().load();
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

    public Collection<RangeType> getDateRanges() {
        return new ArrayList<RangeType>(Arrays.asList(RangeType.THIRTY_DAYS, RangeType.SIXTY_DAYS, RangeType.NINETY_DAYS));
    }

    public String getInactiveSince() {
        return inactiveSince;
    }

    public void setInactiveSince(String inactiveSince) {
        this.inactiveSince = inactiveSince;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    @Override
    public String convertDateTime(Date date) {
        TimeZone timezone = CountryList.getInstance().getRegionByFullId(ConfigEntry.DEFAULT_TIMEZONE_ID.getDefaultValue()).getTimeZone();
        return DateHelper.date2String("MM/dd/yy hh:mm:ss a z", date, timezone);
    }
}
