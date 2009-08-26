package com.n4systems.fieldid.actions.signup;

import java.util.Random;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.view.model.SignUpPackage;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;

public class SignUpPackagePriceCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;

	private SignUpPackage signUpPackage;
	
	private SignUpRequest priceModifier = new SignUpRequest();
	
	public SignUpPackagePriceCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	
	@Override
	protected void loadMemberFields(Long uniqueId) {
	}
	
	
	private void testRequiredEntities() {
		if (signUpPackage == null) {
			addActionErrorText("error.no_sign_up_package");
			throw new MissingEntityException("you need a valid sign up package");
		}
	}

	
	@SkipValidation
	public String doShow() {
		testRequiredEntities();
		
		return SUCCESS;
	}
	
	
	public Long getPrice() {
		return new Random().nextLong();
	}

	public int getNumberOfUsers() {
		return priceModifier.getNumberOfUsers();
	}

	public Long getSignUpPackageId() {
		return priceModifier.getSignUpPackageId();
	}

	public boolean isPurchasingPhoneSupport() {
		return priceModifier.isPurchasingPhoneSupport();
	}

	public void setNumberOfUsers(int numberOfUsers) {
		priceModifier.setNumberOfUsers(numberOfUsers);
	}

	public void setPurchasingPhoneSupport(boolean phoneSupport) {
		priceModifier.setPurchasingPhoneSupport(phoneSupport);
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		priceModifier.setSignUpPackageId(signUpPackageId);
		this.signUpPackage =  new SignUpPackage(signUpPackageId, "basic", 40, false, 1L);
	}

}
