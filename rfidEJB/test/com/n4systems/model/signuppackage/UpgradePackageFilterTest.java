package com.n4systems.model.signuppackage;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.test.helpers.FluentArrayList;

public class UpgradePackageFilterTest {

	@Test
	public void should_find_all_package_above_free_for_a_free_account() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Free;
		List<SignUpPackage> expectedPackages = getSignUpPackages(SignUpPackageDetails.Basic, SignUpPackageDetails.Plus, SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentPackage).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_all_package_above_plus_for_a_plus_account() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Plus;
		List<SignUpPackage> expectedPackages = getSignUpPackages(SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentPackage).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_no_package_above_an_unlimited_account() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Unlimited;
		List<SignUpPackage> expectedPackages = new FluentArrayList<SignUpPackage>();
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentPackage).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_reduce_list_of_sign_up_packages_to_the_list_minus_the_free_account() throws Exception {
		List<SignUpPackage> allFullPackages = getSignUpPackages();
		
		
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Free;
		List<SignUpPackage> expectedPackages = new ArrayList<SignUpPackage>(allFullPackages);
		expectedPackages.remove(0);
		
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentPackage).reduceToAvailablePackages(allFullPackages);
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_no_packages_if_the_current_package_is_null_which_means_legacy() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.getLegacyPackage();
		List<SignUpPackage> expectedPackages = new FluentArrayList<SignUpPackage>();
		
		List<SignUpPackage> actualPackages = UpgradePackageFilter.createUpgradePackageFilter(currentPackage).reduceToAvailablePackages(getSignUpPackages());
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	
	
	@Test
	public void should_find_name_of_current_package() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Free;
		
		assertEquals(currentPackage.getName(), UpgradePackageFilter.createUpgradePackageFilter(currentPackage).getPackageName());
	}
	
	@Test
	public void should_find_name_of_current_package_when_it_is_legacy_package() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.getLegacyPackage();
		
		assertEquals("Legacy", UpgradePackageFilter.createUpgradePackageFilter(currentPackage).getPackageName());
	}
	
	
	
	@Test
	public void should_not_be_upgradable() throws Exception {
		
		assertFalse(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.getLegacyPackage()).isUpgradable());
		assertFalse(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.Unlimited).isUpgradable());
	}
	
	@Test
	public void should_be_upgradable() throws Exception {
		
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.Free).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.Basic).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.Plus).isUpgradable());
		assertTrue(UpgradePackageFilter.createUpgradePackageFilter(SignUpPackageDetails.Enterprise).isUpgradable());
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
}
