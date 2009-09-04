package com.n4systems.fieldid.actions.signup;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.signup.view.model.SignUpRequestDecorator;
import com.n4systems.subscription.CommunicationException;

public class ValidPromoCodeCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ValidPromoCodeCrud.class);
	private SignUpRequestDecorator signUpPromoCode;
	private boolean validPromoCode;

	public ValidPromoCodeCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	
	@Override
	protected void initMemberFields() {
		signUpPromoCode = new SignUpRequestDecorator(getNonSecureLoaderFactory().createTenantUniqueAvailableNameLoader(), getCreateHandlerFactory().getSubscriptionAgent());
	}


	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}
	
	
	public String doShow() {
		try {
			validPromoCode = signUpPromoCode.isValidPromoCodeWithExceptions();
			return SUCCESS;
		
		} catch (CommunicationException e) {
			addActionErrorText("error.could_not_contact_billing_provider");
			logger.error(getLogLinePrefix() + "could not contact billing provider (netsuite)", e);
		} catch (Exception e) {
			addActionErrorText("error.could_not_find_");
			logger.error(getLogLinePrefix() + "could not contact billing provider (netsuite)", e);
		}
		return ERROR;
	}
	
	public SignUpRequestDecorator getSignUp() {
		return signUpPromoCode;
	}


	public boolean isValidPromoCode() {
		return validPromoCode;
	}
	
}
