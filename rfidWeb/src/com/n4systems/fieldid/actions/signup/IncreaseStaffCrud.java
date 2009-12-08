package com.n4systems.fieldid.actions.signup;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.handlers.creator.signup.UpgradeHandlerImpl;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.signuppackage.UpgradePackageFilter;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.UpgradeCost;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;

public class IncreaseStaffCrud extends AbstractCrud {

	private UpgradeCost upgradeCost;
	private AccountHelper accountHelper;
	private List<SignUpPackage> allSignUpPackages;
	private SignUpPackage currentPackage;
	
	private Integer additionalStaff = 0;
	
	public IncreaseStaffCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(), getNonSecureLoaderFactory().createSignUpPackageListLoader());
	}

	@SkipValidation
	public String doAdd() {
		if (accountHelper.currentPackageFilter().isLegacy()) {
			return "legacy";
		}
		
		if (packageHasUserLimit() && accountHasReachedUserLimit()) {
			return "limitExceeded";
		}
		
		additionalStaff = 1;
		
		findUpgradeCost();
		return SUCCESS;
	}
	
	private void findUpgradeCost() {
		UpgradeRequest upgradeRequest = createUpgradeRequest();
		upgradeRequest.setUpdatedBillingInformation(false);
		try {
			upgradeCost = getUpgradeHandler().priceForUpgrade(upgradeRequest);
		} catch (CommunicationException e) {
			upgradeCost = null;
		}
	}

	private UpgradeHandlerImpl getUpgradeHandler() {
		return new UpgradeHandlerImpl(getPrimaryOrg(), new OrgSaver(), getCreateHandlerFactory().getSubscriptionAgent());
	}

	
	private UpgradeRequest createUpgradeRequest() {

		
		UpgradeRequest upgradeRequest = new UpgradeRequest();
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		upgradeRequest.setNewUsers(additionalStaff);
		
		upgradeRequest.setUpgradePackage(getUpgradeFilter().getCurrentContract().getSignUpPackage());
		upgradeRequest.setContractExternalId(getUpgradeFilter().getCurrentContract().getExternalId());
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		
		
		upgradeRequest.setFrequency(getUpgradeFilter().getCurrentContract().getPaymentOption().getFrequency());
		upgradeRequest.setMonths(getUpgradeFilter().getCurrentContract().getPaymentOption().getTerm());
		upgradeRequest.setPurchasingPhoneSupport(accountHelper.getCurrentSubscription().isPhonesupport());
		
		upgradeRequest.setUpdatedBillingInformation(false);
		
		
		return upgradeRequest;
	}
	
	
	@SkipValidation
	public String doShowCost() {
		findUpgradeCost();
		return SUCCESS;
	}
	
	public String doCreate() {
		
		
		
		return SUCCESS;
	}

	private SignUpPackage getCurrentPackage() {
		if (currentPackage == null) {
			currentPackage = accountHelper.currentPackageFilter().getCurrentPackage(getAllSignUpPackages());
		}
		return currentPackage;
	}

	private boolean packageHasUserLimit() {
		return !getSignUpDetails().getUsers().equals(TenantLimit.UNLIMITED);
			
	}
	private boolean accountHasReachedUserLimit() {
		return getSignUpDetails().getUsers() <= getLimits().getEmployeeUsersMax();
	}

	private SignUpPackageDetails getSignUpDetails() {
		return getCurrentPackage().getSignPackageDetails();
	}
	
	public boolean isUnderStaffLimitReached() {
		Long limit = getStaffLimit();
		if (limit == null) {
			return true; 
		}

		return (staffTotal() <= limit);
	}

	private long staffTotal() {
		return getLimits().getEmployeeUsersMax() + additionalStaff;
	}
	
	public Long getStaffLimit() {
		if (!TenantLimit.isUnlimited(getSignUpDetails().getUsers())) {
			return null;
		}
		return getSignUpDetails().getUsers();
	}

	public UpgradeCost getUpgradeCost() {
		return upgradeCost;
	}
	
	
	private List<SignUpPackage> getAllSignUpPackages() {
		if (allSignUpPackages == null) {
			allSignUpPackages = getNonSecureLoaderFactory().createSignUpPackageListLoader().load();
		}
		return allSignUpPackages;
	}

	
	public Integer getAdditionalStaff() {
		return additionalStaff;
	}

	@IntRangeFieldValidator(message="", key="error.number_of_additional_staff_accounts_must_be_greater_than_zero", min="1")
	@FieldExpressionValidator(message="", key="error.you_may_only_x_have_staff_accounts_on_this_play", expression="staffLimit")
	public void setAdditionalStaff(Integer additionalStaff) {
		this.additionalStaff = additionalStaff;
	}
	
	public UpgradePackageFilter getUpgradeFilter() {
		return accountHelper.currentPackageFilter();
	}
	
}
