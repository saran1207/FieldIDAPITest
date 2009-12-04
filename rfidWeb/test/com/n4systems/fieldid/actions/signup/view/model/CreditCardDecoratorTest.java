package com.n4systems.fieldid.actions.signup.view.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.util.DateHelper;

public class CreditCardDecoratorTest {

	
	@Test
	public void should_find_the_expiry_date_is_valid() {
		assertTrue(createCreditCardDecorator(DateHelper.getThisMonth(), DateHelper.getThisYear()).isExpiryValid());
		assertTrue(createCreditCardDecorator(DateHelper.getThisMonth(), DateHelper.getThisYear() + 1).isExpiryValid());
		assertTrue(createCreditCardDecorator(DateHelper.getThisMonth() + 1, DateHelper.getThisYear()).isExpiryValid());
	}
	
	
	@Test
	public void should_find_the_expiry_date_to_be_in_valid() throws Exception {
		assertFalse(createCreditCardDecorator(DateHelper.getThisMonth() - 1, DateHelper.getThisYear()).isExpiryValid());
		assertFalse(createCreditCardDecorator(DateHelper.getThisMonth(), DateHelper.getThisYear() - 1).isExpiryValid());
	}
	
	

	private CreditCardDecorator createCreditCardDecorator(int month, int year) {
		CreditCardDecorator sut = new CreditCardDecorator();
		sut.setExpiryMonth(month);
		sut.setExpiryYear(year);
		return sut;
	}

}
