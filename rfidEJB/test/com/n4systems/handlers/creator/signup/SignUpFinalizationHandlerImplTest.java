package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.user.UserSaver;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.DataUnit;


public class SignUpFinalizationHandlerImplTest extends TestUsesTransactionBase {

	private static final SignUpPackageDetails SIGN_UP_PACKAGE_BEING_USED = SignUpPackageDetails.Basic;
	private static final String SOME_PROMO_CODE = "SOME PROMO CODE";


	@Before
	public void setUp() {
		mockTransaction();
	}
	
	
	@Test
	public void should_find_tenant_list_from_list_resolver_and_have_them_turned_on() {
		AccountCreationInformationStub accountCreationInformationStub = new AccountCreationInformationStub();
		accountCreationInformationStub.setSignUpPackage(new SignUpPackage(SIGN_UP_PACKAGE_BEING_USED, new ArrayList<ContractPricing>()));
		accountCreationInformationStub.setPromoCode(SOME_PROMO_CODE);
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		SignUpTenantResponseStub signUpTenantResponseStub = new SignUpTenantResponseStub();
		
		TenantLimit expectedTenantLimit = new TenantLimit();
		expectedTenantLimit.setAssets(SIGN_UP_PACKAGE_BEING_USED.getAssets());
		expectedTenantLimit.setUsers(accountCreationInformationStub.getNumberOfUsers().longValue());
		expectedTenantLimit.setDiskSpaceInBytes(DataUnit.MEGABYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED.getDiskSpaceInMB(), DataUnit.BYTES));
		
		Set<ExtendedFeature> featuresThatShouldBeAddedToPrimaryOrg = new FluentHashSet<ExtendedFeature>(ExtendedFeature.Integration);
		
		// fixture setup
		ExtendedFeatureListResolver mockExtendedFeatureListResolver = createMock(ExtendedFeatureListResolver.class);
		expect(mockExtendedFeatureListResolver.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.withPromoCode(SOME_PROMO_CODE)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.resolve(mockTransaction)).andReturn(featuresThatShouldBeAddedToPrimaryOrg);
		replay(mockExtendedFeatureListResolver);
		
		OrganizationSaver mockOrganizationSaver = createMock(OrganizationSaver.class);
		expect(mockOrganizationSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getPrimaryOrg())).andReturn(accountPlaceHolder.getPrimaryOrg());
		replay(mockOrganizationSaver);
		
		UserSaver mockUserSaver = createMock(UserSaver.class);
		expect(mockUserSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getAdminUser())).andReturn(accountPlaceHolder.getAdminUser());
		replay(mockUserSaver);
		
		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(mockExtendedFeatureListResolver, mockOrganizationSaver, mockUserSaver);
		sut.setAccountInformation(accountCreationInformationStub)
				.setAccountPlaceHolder(accountPlaceHolder)
				.setSubscriptionApproval(signUpTenantResponseStub);
		
	
		// exercise
		sut.finalizeSignUp(mockTransaction);

		// verify
		verify(mockExtendedFeatureListResolver);
		verify(mockOrganizationSaver);
		verify(mockUserSaver);
		
		assertEquals(featuresThatShouldBeAddedToPrimaryOrg, accountPlaceHolder.getPrimaryOrg().getExtendedFeatures());
		
		assertNotNull(accountPlaceHolder.getPrimaryOrg().getExternalId());
		assertNotNull(accountPlaceHolder.getAdminUser().getExternalId());
		
		assertEquals(expectedTenantLimit, accountPlaceHolder.getPrimaryOrg().getLimits());
	}
}
