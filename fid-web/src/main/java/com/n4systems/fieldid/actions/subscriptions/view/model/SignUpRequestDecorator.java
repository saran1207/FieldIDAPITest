package com.n4systems.fieldid.actions.subscriptions.view.model;

import java.util.List;

import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.TenantNameAvailabilityChecker;
import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Company;
import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.Person;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.ValidatePromoCodeResponse;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class SignUpRequestDecorator implements Subscription, AccountCreationInformation, HasDuplicateValueValidator, Company, Person {
	private final TenantNameAvailabilityChecker tenantNameAvailablityChecker;
	private final SubscriptionAgent subscriptionAgent;
	
	private final SignUpRequest signUpRequest;
	private final CreditCardDecorator creditCard;
	private final AddressInfoDecorator address;

	public SignUpRequestDecorator() {
		this(new SignUpRequest(), null, null);
	}
	
	public SignUpRequestDecorator(TenantNameAvailabilityChecker tenantNameAvailablityChecker, SubscriptionAgent subscriptionAgent) {
		this(new SignUpRequest(), tenantNameAvailablityChecker, subscriptionAgent);
	}
	
	public SignUpRequestDecorator(SignUpRequest signUpRequest, TenantNameAvailabilityChecker tenantNameAvailablityChecker, SubscriptionAgent subscriptionAgent) {
		this.signUpRequest = signUpRequest;
		this.tenantNameAvailablityChecker = tenantNameAvailablityChecker;
		this.subscriptionAgent = subscriptionAgent;
		this.creditCard = new CreditCardDecorator(signUpRequest.getCreditCard());
		this.address = new AddressInfoDecorator(signUpRequest.getBillingAddress());
	}
	
	public boolean duplicateValueExists(String formValue) {
		return !tenantNameAvailablityChecker.isAvailable(formValue);
	}

	public String getCompanyName() {
		return signUpRequest.getCompanyName();
	}

	public Country getCountry() {
		return signUpRequest.getCountry();
	}

	public String getCountryId() {
		return signUpRequest.getCountryId();
	}

	public String getEmail() {
		return signUpRequest.getEmail();
	}

	public String getExternalPassword() {
		return signUpRequest.getExternalPassword();
	}
	
	public String getFirstName() {
		return signUpRequest.getFirstName();
	}

	public String getFullTimeZone() {
		return signUpRequest.getFullTimeZone();
	}

	public String getLastName() {
		return signUpRequest.getLastName();
	}

	public Integer getNumberOfUsers() {
		return signUpRequest.getNumberOfUsers();
	}

	public String getPassword() {
		return signUpRequest.getPassword();
	}

	public String getPasswordConfirm() {
		return signUpRequest.getPasswordConfirm();
	}

	public String getPhoneNumber() {
		return signUpRequest.getPhoneNumber();
	}

	public Region getRegion() {
		return signUpRequest.getRegion();
	}

	public SignUpPackage getSignUpPackage() {
		return signUpRequest.getSignUpPackage();
	}


	public SignUpRequest getSignUpRequest() {
		return signUpRequest;
	}

	public String getTenantName() {
		return signUpRequest.getTenantName();
	}

	public String getTimeZone() {
		return signUpRequest.getTimeZone();
	}

	public String getUsername() {
		return signUpRequest.getUsername();
	}

	public boolean isNew() {
		return signUpRequest.isNew();
	}

	

	@RequiredStringValidator(message="", key="error.company_name_required")
	public void setCompanyName(String companyName) {
		signUpRequest.setCompanyName(companyName.trim());
	}

	public void setCountryId(String countryId) {
		signUpRequest.setCountryId(countryId);
	}

	@RequiredStringValidator(message="", key="error.email")
	@EmailValidator(message="", key="error.email")
	public void setEmail(String email) {
		signUpRequest.setEmail(email.trim());
	}

	@RequiredStringValidator(message="", key="error.first_name_required")
	@StringLengthFieldValidator(message="", key="error.first_name_length", maxLength="255")
	public void setFirstName(String firstName) {
		signUpRequest.setFirstName(firstName.trim());
	}

	@RequiredStringValidator(message="", key="error.last_name_required")
	@StringLengthFieldValidator(message="", key="error.last_name_length", maxLength="255")
	public void setLastName(String lastName) {
		signUpRequest.setLastName(lastName.trim());
	}

	@RequiredFieldValidator(message="", key="error.number_of_users_required")
	@IntRangeFieldValidator(message="", key="error.number_of_users_minimum", min="1")
	@FieldExpressionValidator(message="", key="error.number_of_users_max", expression="(usersBelowMax == true)", fieldName="numberOfUsers")
	public void setNumberOfUsers(Integer numberOfUsers) {
		signUpRequest.setNumberOfUsers(numberOfUsers);
	}
	
	public boolean isUsersBelowMax() {
		if (getSignUpPackage().getUsers() == TenantLimit.UNLIMITED) return true;
		return getUsers() <= getSignUpPackage().getUsers();
	}
	

	@RequiredStringValidator(message="", key="error.passwordrequired")
	@StringLengthFieldValidator(message="", key="errors.passwordlength", minLength="5") 
	public void setPassword(String password) {
		signUpRequest.setPassword(password);
	}
	
	@FieldExpressionValidator(expression="passwordConfirm == password", message="", key="error.passwordsmustmatch")
	public void setPasswordConfirm(String passwordConfirm) {
		signUpRequest.setPasswordConfirm(passwordConfirm);
	}

	@RequiredStringValidator(message="", key="error.phone_number_required")
	@RegexFieldValidator(expression="^(1\\s*[-\\/\\.]?)?(\\((\\d{3})\\)|(\\d{3}))\\s*[-\\/\\.]?\\s*(\\d{3})\\s*[-\\/\\.]?\\s*(\\d{4})\\s*(([xX]|[eE][xX][tT])\\.?\\s*(\\d+))*$", message = "", key="error.phonenumberinvalid")	
	public void setPhoneNumber(String phoneNumber) {
		signUpRequest.setPhoneNumber(phoneNumber.trim());
	}

	public void setSignUpPackage(SignUpPackage signUpPackageId) {
		signUpRequest.setSignUpPackage(signUpPackageId);
	}

	@RequiredStringValidator(message="", key="error.tenant_name_required", shortCircuit=true)
	@StringLengthFieldValidator(message="", key="error.tenant_name_length", minLength="3", maxLength="255")
	@FieldExpressionValidator(expression="validTenantName", message = "", key="error.tenant_name_format")
	@CustomValidator(type="uniqueValue", message = "", key="error.name_already_used")
	public void setTenantName(String tenantName) {
		signUpRequest.setTenantName(tenantName.trim());
	}
	
	public boolean isValidTenantName() {
		String regex = "^[A-Za-z0-9][A-Za-z0-9\\-]+[A-Za-z0-9]$";
		return getTenantName().matches(regex);
	}

	
	@RequiredStringValidator(message="", key="error.time_zone_name_required")
	public void setTimeZone(String regionId) {
		signUpRequest.setTimeZone(regionId);
	}

	@RequiredStringValidator(message="", key="error.username_required")
	@FieldExpressionValidator(expression="(username != restrictedUsername)", message="", key="error.reserved_username")
	@StringLengthFieldValidator(message = "" , key="errors.useridlength", maxLength="15")
	public void setUsername(String username) {
		signUpRequest.setUsername(username.trim());
		ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}
	
	public String getRestrictedUsername() {
		return ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME);
	}
	
	public AddressInfo getBillingAddress() {
		return address;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public Long getContractExternalId() {
		return signUpRequest.getContractExternalId();
	}


	public PaymentFrequency getFrequency() {
		return signUpRequest.getFrequency();
	}

	public Integer getMonths() {
		return signUpRequest.getMonths();
	}

	public String getPhone() {
		return signUpRequest.getPhone();
	}


	public AddressInfo getShippingAddress() {
		return signUpRequest.getShippingAddress();
	}

	public String getUrl() {
		return signUpRequest.getUrl();
	}

	public Integer getUsers() {
		return signUpRequest.getUsers();
	}

	public boolean isPurchasingPhoneSupport() {
		return signUpRequest.isPurchasingPhoneSupport();
	}

	public boolean isUsingCreditCard() {
		return signUpRequest.isUsingCreditCard();
	}
	
	public void setUsingCreditCard(boolean usingCreditCard) {
		signUpRequest.setUsingCreditCard(usingCreditCard);
	}
	
	public String getPurchaseOrderNumber() {
		return signUpRequest.getPurchaseOrderNumber();
	}
	
	@FieldExpressionValidator(message="", key="error.ponumberrequired", expression="(purchaseOrderNumberValid == true)")	
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		signUpRequest.setPurchaseOrderNumber(purchaseOrderNumber.trim());
	}	

	public boolean isPurchaseOrderNumberValid() {
		if (isUsingCreditCard() || getSignUpPackage().isFree()) return true;
		
		return ((getPurchaseOrderNumber() != null) && (getPurchaseOrderNumber().trim().length() > 0));
	}
	
	public String getCompanyN4Id() {
		return signUpRequest.getCompanyN4Id();
	}

	public String getPromoCode() {
		return signUpRequest.getPromoCode();
	}
	
	public void setPromoCode(String promoCode) {
		signUpRequest.setPromoCode(promoCode);
	}

	public String getPaymentOption() {
		return signUpRequest.getPaymentOption();
	}

	public void setPaymentOption(String paymentOption) {
		signUpRequest.setPaymentOption(paymentOption);
	}

	public List<ContractPricing> getPaymentOptions() {
		return getSignUpPackage().getPaymentOptions();
	}

	public String getUserN4Id() {
		return signUpRequest.getUserN4Id();
	}
	
	@FieldExpressionValidator(message="", key="error.promo_code_not_valid", expression="(validPromoCode == true)", fieldName="promoCode")
	public boolean isValidPromoCode() {
		try {
			return isValidPromoCodeWithExceptions();
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isValidPromoCodeWithExceptions() throws CommunicationException{
		if (blankPromoCode()) {
			return true;
		}
		
		ValidatePromoCodeResponse response = subscriptionAgent.validatePromoCode(getPromoCode());
		return response.isValid();
	}

	private boolean blankPromoCode() {
		return getPromoCode() == null || getPromoCode().trim().length() == 0;
	}
	
	

}
