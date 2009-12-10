package com.n4systems.fieldid.actions.signup;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.signup.view.model.CreditCardDecorator;
import com.n4systems.handlers.creator.signup.UpgradeRequest;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.CurrentSubscription;
import com.n4systems.subscription.UpgradeCost;
import com.n4systems.subscription.UpgradeResponse;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.ValidationParameter;

public abstract class AbstractUpgradeCrud extends AbstractCrud {

	protected AccountHelper accountHelper;
	protected UpgradeCost upgradeCost;
	protected UpgradeResponse upgradeResponse;
	
	
	//billing fields
	protected String purchaseOrderNumber;
	protected boolean usingCreditCard = true;
	protected CreditCardDecorator creditCard = new CreditCardDecorator();

	public AbstractUpgradeCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void postInit() {
		super.postInit();
		accountHelper = new AccountHelper(getCreateHandlerFactory().getSubscriptionAgent(), getPrimaryOrg(), getNonSecureLoaderFactory().createSignUpPackageListLoader());
	}

	
	public UpgradeCost getUpgradeCost() {
		if (upgradeCost == null) {
			findUpgradeCost();
		}
		return upgradeCost;
	}
	
	protected abstract void findUpgradeCost();

	@FieldExpressionValidator(message = "", key = "error.po_number_is_required", expression = "pONumberCorrectlyEntered")
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public boolean isPONumberCorrectlyEntered() {
		if (accountHelper.getCurrentSubscription().isUpgradeRequiresBillingInformation() && !usingCreditCard) {
			return (purchaseOrderNumber != null && !purchaseOrderNumber.trim().isEmpty());
		}
		return true;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	@CustomValidator(type = "conditionalVisitorFieldValidator", message = "", parameters = { @ValidationParameter(name = "expression", value = "aNeedToCheckCreditCard == true") })
	public CreditCardDecorator getCreditCard() {
		return creditCard;
	}

	public boolean isANeedToCheckCreditCard() {
		return (isUsingCreditCard() && accountHelper.getCurrentSubscription().isUpgradeRequiresBillingInformation());
	}

	public void setCreditCard(CreditCardDecorator creditCard) {
		this.creditCard = creditCard;
	}

	public CurrentSubscription getCurrentSubscription() {
		try {
			return accountHelper.getCurrentSubscription();
		} catch (CommunicationException e) {
			return null;
		}
	}

	public boolean isUsingCreditCard() {
		return usingCreditCard;
	}

	public void setUsingCreditCard(boolean usingCreditCard) {
		this.usingCreditCard = usingCreditCard;
	}

	protected UpgradeRequest createUpgradeRequest() {
		
		
		UpgradeRequest upgradeRequest = new UpgradeRequest();
		upgradeRequest.setTenantExternalId(getPrimaryOrg().getExternalId());
		
		setUpgradeInformation(upgradeRequest);
		setBillingInformation(upgradeRequest);
		
		return upgradeRequest;
	}
	
	protected abstract void setUpgradeInformation(UpgradeRequest upgradeRequest);
	
	
	private void setBillingInformation(UpgradeRequest upgradeRequest) {
		upgradeRequest.setUpdatedBillingInformation(accountHelper.getCurrentSubscription().isUpgradeRequiresBillingInformation());
		upgradeRequest.setUsingCreditCard(usingCreditCard);
		upgradeRequest.setPurchaseOrderNumber(purchaseOrderNumber);
		upgradeRequest.setCreditCard(creditCard.getDelegateCard());
	}
}