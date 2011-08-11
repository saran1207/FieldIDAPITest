package com.n4systems.model.signuppackage;

import static com.n4systems.model.builders.SignUpPackageBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.builders.ContractPricingBuilder;
import com.n4systems.subscription.PaymentOption;
import com.n4systems.test.helpers.FluentArrayList;

public class UpgradePackageFilterTest {

	@Test
	public void should_find_all_package_above_free_for_a_free_account() throws Exception {
		ContractPricing currentContract = createCurrentContract(SignUpPackageDetails.Free);
		List<SignUpPackage> expectedPackages = getSignUpPackages(SignUpPackageDetails.Basic, SignUpPackageDetails.Plus, SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentContract).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_all_package_above_plus_for_a_plus_account() throws Exception {
		ContractPricing currentContract = createCurrentContract(SignUpPackageDetails.Plus);
		
		List<SignUpPackage> expectedPackages = getSignUpPackages(SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentContract).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_no_package_above_an_unlimited_account() throws Exception {
		ContractPricing currentContract = createCurrentContract(SignUpPackageDetails.Unlimited);
		List<SignUpPackage> expectedPackages = new FluentArrayList<SignUpPackage>();
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentContract).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_reduce_list_of_sign_up_packages_to_the_list_minus_the_free_account() throws Exception {
		List<SignUpPackage> allFullPackages = getSignUpPackages();
		
		ContractPricing currentContract = createCurrentContract(SignUpPackageDetails.Free);
		List<SignUpPackage> expectedPackages = new ArrayList<SignUpPackage>(allFullPackages);
		expectedPackages.remove(0);
        expectedPackages.remove(0);
		
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentContract).reduceToAvailablePackages(allFullPackages);
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_no_packages_if_the_current_package_is_null_which_means_legacy() throws Exception {
		ContractPricing currentContract = ContractPricing.getLegacyContractPricing(); 
		List<SignUpPackage> expectedPackages = new FluentArrayList<SignUpPackage>();
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentContract).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	
	
	@Test
	public void should_find_name_of_current_package() throws Exception {
		ContractPricing currentContract = createCurrentContract(SignUpPackageDetails.Free);
		
		assertEquals(currentContract.getSignUpPackage().getName(), UpgradePackageFilter.createUpgradePackageFilter(currentContract).getPackageName());
	}

	
	
	@Test
	public void should_find_name_of_current_package_when_it_is_legacy_package() throws Exception {
		ContractPricing currentContract = ContractPricing.getLegacyContractPricing(); 
		assertEquals("Legacy", UpgradePackageFilter.createUpgradePackageFilter(currentContract).getPackageName());
	}
	
	
	
	@Test
	public void should_not_be_upgradable() throws Exception {
		assertFalse(UpgradePackageFilter.createUpgradePackageFilter(ContractPricing.getLegacyContractPricing()).isUpgradable());
		assertFalse(UpgradePackageFilter.createUpgradePackageFilter(createCurrentContract(SignUpPackageDetails.Unlimited)).isUpgradable());
	}
	
	@Test
	public void should_be_upgradable() throws Exception {
		
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(createCurrentContract(SignUpPackageDetails.Free)).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(createCurrentContract(SignUpPackageDetails.Basic)).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(createCurrentContract(SignUpPackageDetails.Plus)).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(createCurrentContract(SignUpPackageDetails.Enterprise)).isUpgradable());
	}

	
	@Test
	public void should_find_upgrade_contract_only_one_that_matches() throws Exception {
		ContractPricing currentContract = createContract(SignUpPackageDetails.Free, 10L, PaymentOption.ONE_YEAR_UP_FRONT);
		
		SignUpPackage upgradePackage = createSignUpPackage(SignUpPackageDetails.Basic).withContracts(createContract(SignUpPackageDetails.Basic, 30L, PaymentOption.ONE_YEAR_UP_FRONT)).build();
		
		ContractPricing actualUpgradeContract = UpgradePackageFilter.createUpgradePackageFilter(currentContract).getUpgradeContractForPackage(upgradePackage);
		
		assertEquals(upgradePackage.getContractId(PaymentOption.ONE_YEAR_UP_FRONT), actualUpgradeContract.getExternalId());
	}
	

	@Test
	public void should_retrun_current_sign_up_package() throws Exception {
		ContractPricing currentContract = ContractPricingBuilder.aContractPricing()
			.withExternalId(10L)
			.withPackage(SignUpPackageDetails.Free)
			.withPaymentOption(PaymentOption.TWO_YEARS_UP_FRONT).build();

		
		SignUpPackage currentPackage = createSignUpPackage(SignUpPackageDetails.Free).withContracts(currentContract).build();
		
		
		UpgradePackageFilter sut = UpgradePackageFilter.createUpgradePackageFilter(currentContract);
		SignUpPackage actualCurrentPackage = sut.getCurrentPackage(new FluentArrayList<SignUpPackage>(currentPackage));
		
		assertEquals(currentPackage, actualCurrentPackage);
	}
	
	
	@Test
	public void should_find_per_user_price_of_current_package_to_be_100() {
		
		ContractPricing currentContract = ContractPricingBuilder.aContractPricing()
																.withPackage(SignUpPackageDetails.Free)
																.withPricePerUserPerMonth(100F)
																.build();
			
		SignUpPackage currentPackage = createSignUpPackage(SignUpPackageDetails.Free).withContracts(currentContract).build();
			
			
		UpgradePackageFilter sut = UpgradePackageFilter.createUpgradePackageFilter(currentContract);
		sut.getCurrentPackage(new FluentArrayList<SignUpPackage>(currentPackage));
		
		assertEquals(100F, sut.getCurrentCostPerUserPerMonth(), 0.001);
	}
	
	private ContractPricing createContract(SignUpPackageDetails signUpPackage, long contractId, PaymentOption paymentOption) {
		ContractPricing currentContract = new ContractPricing();
		currentContract.setSignUpPackage(signUpPackage);
		currentContract.setExternalId(contractId);
		currentContract.setPaymentOption(paymentOption);
		currentContract.setPricePerUserPerMonth(100.00F);
		return currentContract;
		
	}

	private List<SignUpPackage> getSignUpPackages() {
		return getSignUpPackages(SignUpPackageDetails.values());
	}
	
	private List<SignUpPackage> getSignUpPackages(SignUpPackageDetails...packageDetails) {
		List<SignUpPackage> packages = new ArrayList<SignUpPackage>();
		for (SignUpPackageDetails signUpPackageDetail : packageDetails) {
			packages.add(new SignUpPackage(signUpPackageDetail, new ArrayList<ContractPricing>()));
		}
		
		return packages;
	}
	
	private ContractPricing createCurrentContract(SignUpPackageDetails signUpAccount) {
		ContractPricing currentContract = new ContractPricing();
		currentContract.setSignUpPackage(signUpAccount);
		return currentContract;
	}
}
