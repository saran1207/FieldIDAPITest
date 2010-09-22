package com.n4systems.fieldid.actions.subscriptions.view.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.n4systems.subscription.CreditCard;
import com.n4systems.subscription.CreditCardType;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

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
		delegateCard.setName(name.trim());
	}

	@RegexFieldValidator(expression="^((4\\d{3})|(5[1-5]\\d{2}))(-?\\s?\\d{4}){3}|(3[4,7])\\d{2}-?\\s?\\d{6}-?\\s?\\d{5}$", message = "", key="error.invalid_cc_number")
	@RequiredStringValidator(message="", key="error.valid_cc_number_required")
	public void setNumber(String number) {
		delegateCard.setNumber(number.trim());
	}

	@RequiredStringValidator(message="", key="error.cc_type_required")
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

	@FieldExpressionValidator(message="", key="error.expiry_in_past", expression="expiryValid")
	public int getExpiryYear() {
		return delegateCard.getExpiryYear();
	}
	
	
	public boolean isExpiryValid() {
		int thisMonth = DateHelper.getThisMonth();
		int thisYear = DateHelper.getThisYear();

		if (thisYear < getExpiryYear()) {
			return true;
		}
		
		return (thisMonth <= getExpiryMonth() && thisYear == getExpiryYear());
	}

	
	public void setExpiryMonth(int expiryMonth) {
		delegateCard.setExpiryMonth(expiryMonth);
	}
	
	
	public void setExpiryYear(int expiryYear) {
		delegateCard.setExpiryYear(expiryYear);
	}

	public CreditCard getDelegateCard() {
		return delegateCard;
	}

}
