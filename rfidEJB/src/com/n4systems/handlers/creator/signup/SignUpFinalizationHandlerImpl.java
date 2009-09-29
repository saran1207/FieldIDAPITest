package com.n4systems.handlers.creator.signup;

import java.util.Set;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;

public class SignUpFinalizationHandlerImpl implements SignUpFinalizationHandler {

	private final ExtendedFeatureListResolver extendedFeatureListResolver;
	private final LimitResolver limitResolver;
	private final OrgSaver orgSaver;
	private final UserSaver userSaver;
	private final OrgConnectionSaver orgConnectionSaver;
	
	private AccountCreationInformation accountInformation;
	private AccountPlaceHolder accountPlaceHolder;
	private SignUpTenantResponse subscriptionApproval;
	private PrimaryOrg referrerOrg;

	public SignUpFinalizationHandlerImpl(ExtendedFeatureListResolver extendedFeatureListResolver, OrgSaver orgSaver, UserSaver userSaver, LimitResolver limitResolver, OrgConnectionSaver orgConnectionSaver) {
		super();
		this.extendedFeatureListResolver = extendedFeatureListResolver;
		this.limitResolver = limitResolver;
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
		this.orgConnectionSaver = orgConnectionSaver;
	}

	
	public void finalizeSignUp(Transaction transaction) {
		guards();
		
		processExtendedFeatures(transaction);
		processLimits(transaction);
		applyExternalIds();
		
		saveChanges(transaction);
		linkTenants(transaction);
	}


	private void processLimits(Transaction transaction) {
		
		limitResolver.withPromoCode(accountInformation.getPromoCode())
								.withSignUpPackageDetails(accountInformation.getSignUpPackage().getSignPackageDetails());
		TenantLimit limits = limitResolver.resolve(transaction);
		
		limits.setUsers(new Long(accountInformation.getNumberOfUsers()));
		getPrimaryOrg().setLimits(limits);
	}
	
	protected OrgConnection createOrgConnection() {
		OrgConnection conn = new OrgConnection();
		conn.setModifiedBy(getAdminUser());
		conn.setVendor(referrerOrg);
		conn.setCustomer(getPrimaryOrg());
		return conn;
	}

	private void linkTenants(Transaction transaction) {
		orgConnectionSaver.save(transaction, createOrgConnection());
	}

	private void saveChanges(Transaction transaction) {
		orgSaver.saveOrUpdate(transaction, getPrimaryOrg());
		userSaver.saveOrUpdate(transaction, getAdminUser());
	}

	private void applyExternalIds() {
		getAdminUser().setExternalId(subscriptionApproval.getClient().getExternalId());
		getPrimaryOrg().setExternalId(subscriptionApproval.getTenant().getExternalId());
	}


	protected UserBean getAdminUser() {
		return accountPlaceHolder.getAdminUser();
	}


	protected PrimaryOrg getPrimaryOrg() {
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
