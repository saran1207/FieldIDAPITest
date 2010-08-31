package com.n4systems.model.signuppackage;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.tenant.TenantLimit;


@RunWith(Parameterized.class)
public class SignUpPackageDetailsTest {

	final SignUpPackageDetails signUpPackageDetails;
	
	public SignUpPackageDetailsTest(SignUpPackageDetails signUpPackageDetails) {
		super();
		this.signUpPackageDetails = signUpPackageDetails;
	}


	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> result = new ArrayList<Object[]>();
		
		for (SignUpPackageDetails signUpPackageDetails : SignUpPackageDetails.values()) {
			result.add(new Object[] { signUpPackageDetails });
		}
		
		return result;
	}
	
	
	
	@Test
	public void should_secondary_org_limit_should_be_0_when_there_is_no_extended_feature_multi_location() throws Exception {
		assertThat(signUpPackageDetails, hasAZeroedSecondaryOrgLimitIfMultiLocationIsNotAnExtendedFeature() );
	
	}

	@SuppressWarnings("unchecked")
	private Matcher<Object> hasAZeroedSecondaryOrgLimitIfMultiLocationIsNotAnExtendedFeature() {
		return anyOf(hasProperty("extendedFeatures", hasItemInArray(ExtendedFeature.MultiLocation)), hasProperty("secondaryOrgs", equalTo(TenantLimit.NONE)));
	}
	
	
	@Test
	public void should_secondary_org_limit_should_be_unlimited_when_there_is_the_extended_feature_multi_location() throws Exception {
		assertThat(signUpPackageDetails, hasAnUnlimitedSecondaryOrgLimitIfMultiLocationIsAnExtendedFeature() );
	}
	
	@SuppressWarnings("unchecked")
	private Matcher<Object> hasAnUnlimitedSecondaryOrgLimitIfMultiLocationIsAnExtendedFeature() {
		return anyOf(hasProperty("extendedFeatures", not(hasItemInArray(ExtendedFeature.MultiLocation))), hasProperty("secondaryOrgs", equalTo(TenantLimit.UNLIMITED)));
	}
}
