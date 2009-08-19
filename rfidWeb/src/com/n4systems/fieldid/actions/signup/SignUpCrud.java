package com.n4systems.fieldid.actions.signup;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.jboss.xb.binding.metadata.AddMethodMetaData;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.view.model.SignUp;
import com.n4systems.fieldid.view.model.SignUpPackage;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class SignUpCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SignUpCrud.class);
	
	private SignUpPackage signUpPackage;
	private SignUp signUp;
	

	public SignUpCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		signUp = (SignUp)getSessionVar("signUp");
		
		if (signUp == null) {
			signUp = new SignUp();
		}
		
		setSignUpPackageId(signUp.getSignUpPackageId());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}


	
	private void testRequiredEntities(boolean exists) {
		if (exists && signUp.isNew()) {
			addFlashErrorText("error.you_must_go_through_sign_up");
			throw new MissingEntityException("you must go through the sign up process");
		}
		
		if (signUpPackage == null) {
			addFlashErrorText("error.no_sign_up_package");
			throw new MissingEntityException("you must select a package");
		}
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);
		clearSessionVar("signUp");
		return SUCCESS;
	}
	
	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}
	
	public String doCreate() {
		testRequiredEntities(false);
		setSessionVar("signUp", signUp);
		addActionMessageText("message.your_account_has_been_created");
		return SUCCESS;
	}


	public SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}
	
	public Long getSignUpPackageId() {
		return signUpPackage.getId();
	}

	public void setSignUpPackageId(Long signUpPackageId) {
		signUp.setSignUpPackageId(signUpPackageId);
		this.signUpPackage = new SignUpPackage(signUpPackageId, "basic", 0, false, 1L);
	}


	@VisitorFieldValidator(message="")
	public SignUp getSignUp() {
		return signUp;
	}

}
