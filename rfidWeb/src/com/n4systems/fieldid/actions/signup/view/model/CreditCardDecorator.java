package com.n4systems.fieldid.actions.signup.view.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.CreditCardType;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public class CreditCardDecorator extends CreditCard {

	private static final int NUMBER_OF_YEARS_TO_SHOW_FOR_CREDIT_CARD_EXPIRY = 6;
	private CreditCard delegateCard;
	private List<Integer> years;
	
	public CreditCardDecorator() {
		this(new CreditCard());
	}
	
	public CreditCardDecorator(CreditCard creditCard) {
		this.delegateCard = creditCard;
	}

	public CreditCardType[] getCreditCardTypes() {
		return CreditCardType.values();
	}
	
	public Integer[] getMonths() {
		Integer[] months = {1,2,3,4,5,6,7,8,9,10,11,12};
		return months;
	}
	
	public List<Integer> getYears() {
		if (years == null) {
			years = new ArrayList<Integer>(NUMBER_OF_YEARS_TO_SHOW_FOR_CREDIT_CARD_EXPIRY);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			for (int i = 0; i < NUMBER_OF_YEARS_TO_SHOW_FOR_CREDIT_CARD_EXPIRY; i++) {
				years.add(currentYear + i);
			}
		}
		return years;
	}
	
	public String getExpiry() {
		return delegateCard.getExpiry();
	}

	public String getName() {
		return delegateCard.getName();
	}

	public String getNumber() {
		return delegateCard.getNumber();
	}

	public CreditCardType getType() {
		return delegateCard.getType();
	}

	@RequiredStringValidator(message="", key="error.cc_name_required")
	public void setName(String name) {
		delegateCard.setName(name);
	}

	@RequiredStringValidator(message="", key="error.valid_cc_number_required")
	@StringLengthFieldValidator(message="", key="error.valid_cc_number_required", minLength="13")
	public void setNumber(String number) {
		delegateCard.setNumber(number);
	}

	@RequiredStringValidator(message="", key="error.type_required")
	public void setCCType(String type) {
		setType(CreditCardType.valueOf(type));
	}
	
	public String getCCType() {
		return delegateCard.getType().name();
	}
	public void setType(CreditCardType type) {
		delegateCard.setType(type);
	}

	
	public int getExpiryMonth() {
		return delegateCard.getExpiryMonth();
	}

	public int getExpiryYear() {
		return delegateCard.getExpiryYear();
	}

	
	public void setExpiryMonth(int expiryMonth) {
		delegateCard.setExpiryMonth(expiryMonth);
	}
	
	
	public void setExpiryYear(int expiryYear) {
		delegateCard.setExpiryYear(expiryYear);
	}

}
