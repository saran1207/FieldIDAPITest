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
		List<SignUpPackageDetails> expectedPackages = new FluentArrayList<SignUpPackageDetails>(SignUpPackageDetails.Basic, SignUpPackageDetails.Plus, SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackageDetails> actualPackages = new UpgradePackageFilter(currentPackage).availablePackages();
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_all_package_above_plus_for_a_plus_account() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Plus;
		List<SignUpPackageDetails> expectedPackages = new FluentArrayList<SignUpPackageDetails>(SignUpPackageDetails.Enterprise, SignUpPackageDetails.Unlimited);
		
		List<SignUpPackageDetails> actualPackages = new UpgradePackageFilter(currentPackage).availablePackages();
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_find_no_package_above_an_unlimited_account() throws Exception {
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Unlimited;
		List<SignUpPackageDetails> expectedPackages = new FluentArrayList<SignUpPackageDetails>();
		
		List<SignUpPackageDetails> actualPackages = new UpgradePackageFilter(currentPackage).availablePackages();
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	@Test
	public void should_reduce_list_of_sign_up_packages_to_the_list() throws Exception {
		List<SignUpPackage> allFullPackages = getSignUpPackages();
		
		
		SignUpPackageDetails currentPackage = SignUpPackageDetails.Free;
		List<SignUpPackage> expectedPackages = new ArrayList<SignUpPackage>(allFullPackages);
		expectedPackages.remove(0);
		
		
		List<SignUpPackage> actualPackages = new UpgradePackageFilter(currentPackage).reduceToAvailablePackages(allFullPackages);
		
		assertEquals(expectedPackages, actualPackages);
	}
	
	
	
	private List<SignUpPackage> getSignUpPackages() {
		List<SignUpPackage> packages = new ArrayList<SignUpPackage>();
		for (SignUpPackageDetails signUpPackageDetail : SignUpPackageDetails.values()) {
			packages.add(new SignUpPackage(signUpPackageDetail, new ArrayList<ContractPricing>()));
		}
		
		return packages;
	}
}
