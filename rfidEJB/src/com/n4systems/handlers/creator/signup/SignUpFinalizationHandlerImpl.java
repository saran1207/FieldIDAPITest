package com.n4systems.handlers.creator.signup;

import java.util.Set;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class SignUpFinalizationHandlerImpl implements SignUpFinalizationHandler {

	private final ExtendedFeatureListResolver extendedFeatureListResolver;
	private final LimitResolver limitResolver;
	private final OrgSaver orgSaver;
	private final UserSaver userSaver;
	private final LinkTenantHandler linkTenantHandler;
	
	private AccountCreationInformation accountInformation;
	private AccountPlaceHolder accountPlaceHolder;
	private SignUpTenantResponse subscriptionApproval;
	private PrimaryOrg referrerOrg;

	public SignUpFinalizationHandlerImpl(ExtendedFeatureListResolver extendedFeatureListResolver, OrgSaver orgSaver, UserSaver userSaver, LimitResolver limitResolver, 
					LinkTenantHandler linkTenantHandler) {
		super();
		this.extendedFeatureListResolver = extendedFeatureListResolver;
		this.limitResolver = limitResolver;
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
		this.linkTenantHandler = linkTenantHandler;
		
	}

	
	public void finalizeSignUp(Transaction transaction) {
		guards();
		
		linkTenants(transaction);
		applyChangesToPlaceHolders(transaction);
	}


	private void applyChangesToPlaceHolders(Transaction transaction) {
		processExtendedFeatures(transaction);
		processLimits(transaction);
		applyExternalIds();
		setDefaultVendorContext();
		saveChanges(transaction);
	}


	private void setDefaultVendorContext() {
		if (!getConfigContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID).equals(referrerOrg.getId())) {
			getPrimaryOrg().setDefaultVendorContext(referrerOrg.getId());
		}
	}


	private ConfigContext getConfigContext() {
		return ConfigContext.getCurrentContext();
	}


	private void processLimits(Transaction transaction) {
		
		limitResolver.withPromoCode(accountInformation.getPromoCode())
								.withSignUpPackageDetails(accountInformation.getSignUpPackage().getSignPackageDetails());
		TenantLimit limits = limitResolver.resolve(transaction);
		
		limits.setUsers(new Long(accountInformation.getNumberOfUsers()));
		getPrimaryOrg().setLimits(limits);
	}
	
	

	private void linkTenants(Transaction transaction) {
		linkTenantHandler.setAccountPlaceHolder(accountPlaceHolder)
						.setReferrerOrg(referrerOrg);
		
		linkTenantHandler.link(transaction);
	}

	

	private void saveChanges(Transaction transaction) {
		orgSaver.saveOrUpdate(transaction, getPrimaryOrg());
		userSaver.saveOrUpdate(transaction, getAdminUser());
	}

	private void applyExternalIds() {
		getAdminUser().setExternalId(subscriptionApproval.getClient().getExternalId());
		getPrimaryOrg().setExternalId(subscriptionApproval.getTenant().getExternalId());
	}


	private UserBean getAdminUser() {
		return accountPlaceHolder.getAdminUser();
	}


	private PrimaryOrg getPrimaryOrg() {
		return accountPlaceHolder.getPrimaryOrg();
	}
	
	private void guards() {
		if (accountInformation == null) {
			throw new InvalidArgumentException("you must give an Account Creation Information");
		}
		
		if (accountPlaceHolder == null) {
			throw new InvalidArgumentException("you must give an Account Place Holder");
		}
		
		if (subscriptionApproval == null) {
			throw new InvalidArgumentException("you must give a SignUpTenantResponse");
		}
		
		if (referrerOrg == null) {
			throw new InvalidArgumentException("you must give a referrer PrimaryOrg");
		}
	}

	private void processExtendedFeatures(Transaction transaction) {
		Set<ExtendedFeature> extendedFeaturesToTurnOn = extendedFeatureListResolver.withPromoCode(accountInformation.getPromoCode())
												.withSignUpPackageDetails(accountInformation.getSignUpPackage().getSignPackageDetails())
												.resolve(transaction);
		
		for (ExtendedFeature feature : extendedFeaturesToTurnOn) {
			ExtendedFeatureSwitch featureSwitch = getSwitchFor(feature);
			featureSwitch.enableFeature(transaction);
		}
	}



	public SignUpFinalizationHandler setAccountInformation(AccountCreationInformation accountInformation) {
		this.accountInformation = accountInformation;
		return this;
	}
	
	public SignUpFinalizationHandler setAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder) {
		this.accountPlaceHolder = accountPlaceHolder;
		return this;
	}
	
	public SignUpFinalizationHandler setSubscriptionApproval(SignUpTenantResponse subscriptionApproval) {
		this.subscriptionApproval = subscriptionApproval;
		return this;
	}
	
	public SignUpFinalizationHandler setReferrerOrg(PrimaryOrg referrerOrg) {
		this.referrerOrg = referrerOrg;
		return this;
	}
	
	protected ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature) {
		return ExtendedFeatureFactory.getSwitchFor(feature, getPrimaryOrg());
	}

}
