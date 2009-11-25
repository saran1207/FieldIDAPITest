package com.n4systems.model.signuppackage;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.builders.SignUpPackageBuilder;
import com.n4systems.subscription.PaymentOption;


public class SignUpPackageTest {

	
	@Test
	public void should_get_the_contract_with_the_same_price_option_as_the_one_given() throws Exception {
		ContractPricing contractToFind = new ContractPricing();
		contractToFind.setPaymentOption(PaymentOption.ONE_YEAR_UP_FRONT);
		
		ContractPricing otherContract = new ContractPricing();
		otherContract.setPaymentOption(PaymentOption.MONTH_TO_MONTH);
		
		SignUpPackage signUpPackage = SignUpPackageBuilder.createSignUpPackage(SignUpPackageDetails.Enterprise).withContracts(otherContract, contractToFind).build();
		
		ContractPricing actualContract = signUpPackage.getContract(PaymentOption.ONE_YEAR_UP_FRONT);
		
		assertEquals(contractToFind, actualContract);
	}
	

	@Test(expected=InvalidArgumentException.class)
	public void should_not_find_contract_when_on() throws Exception {
		ContractPricing contractToFind = new ContractPricing();
		contractToFind.setPaymentOption(PaymentOption.ONE_YEAR_UP_FRONT);
		
		SignUpPackage signUpPackage = SignUpPackageBuilder.createSignUpPackage(SignUpPackageDetails.Enterprise).withContracts(contractToFind).build();
		
		signUpPackage.getContract(PaymentOption.MONTH_TO_MONTH);
	}
}
