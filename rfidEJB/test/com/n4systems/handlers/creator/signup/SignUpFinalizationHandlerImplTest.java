package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
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
		
		TenantLimit limitFromResolver = new TenantLimit();
		limitFromResolver.setAssets(SIGN_UP_PACKAGE_BEING_USED.getAssets());
		limitFromResolver.setDiskSpaceInBytes(DataUnit.MEGABYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED.getDiskSpaceInMB(), DataUnit.BYTES));
		
		
		
		Set<ExtendedFeature> featuresThatShouldBeAddedToPrimaryOrg = new FluentHashSet<ExtendedFeature>(ExtendedFeature.Integration);
		
		// fixture setup
		ExtendedFeatureListResolver mockExtendedFeatureListResolver = createMock(ExtendedFeatureListResolver.class);
		expect(mockExtendedFeatureListResolver.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.withPromoCode(SOME_PROMO_CODE)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.resolve(mockTransaction)).andReturn(featuresThatShouldBeAddedToPrimaryOrg);
		replay(mockExtendedFeatureListResolver);
		
		OrgSaver mockOrganizationSaver = createMock(OrgSaver.class);
		expect(mockOrganizationSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getPrimaryOrg())).andReturn(accountPlaceHolder.getPrimaryOrg());
		replay(mockOrganizationSaver);
		
		UserSaver mockUserSaver = createMock(UserSaver.class);
		expect(mockUserSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getAdminUser())).andReturn(accountPlaceHolder.getAdminUser());
		replay(mockUserSaver);
		
		LimitResolver mockLimitResolver = createMock(LimitResolver.class);
		expect(mockLimitResolver.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED)).andReturn(mockLimitResolver);
		expect(mockLimitResolver.withPromoCode(SOME_PROMO_CODE)).andReturn(mockLimitResolver);
		expect(mockLimitResolver.resolve(mockTransaction)).andReturn(limitFromResolver);
		replay(mockLimitResolver);
		
		OrgConnectionSaver mockConnSaver = createMock(OrgConnectionSaver.class);
		
		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(mockExtendedFeatureListResolver, mockOrganizationSaver, mockUserSaver, mockLimitResolver, mockConnSaver);
		sut.setAccountInformation(accountCreationInformationStub)
				.setAccountPlaceHolder(accountPlaceHolder)
				.setSubscriptionApproval(signUpTenantResponseStub)
				.setReferrerOrg((PrimaryOrg)OrgBuilder.aPrimaryOrg().build());
			
		// exercise
		sut.finalizeSignUp(mockTransaction);

		// verify
		verify(mockExtendedFeatureListResolver);
		verify(mockOrganizationSaver);
		verify(mockUserSaver);
		verify(mockLimitResolver);
		
		assertEquals(featuresThatShouldBeAddedToPrimaryOrg, accountPlaceHolder.getPrimaryOrg().getExtendedFeatures());
		
		assertNotNull(accountPlaceHolder.getPrimaryOrg().getExternalId());
		assertNotNull(accountPlaceHolder.getAdminUser().getExternalId());
		
		assertEquals(expectedTenantLimit, accountPlaceHolder.getPrimaryOrg().getLimits());
	}
	
	@Test
	public void create_org_connection_uses_referrer_as_vendor_and_signup_as_customer() {
		final UserBean adminUser = UserBuilder.anEmployee().build();
		final PrimaryOrg referrerOrg = (PrimaryOrg)OrgBuilder.aPrimaryOrg().build();
		final PrimaryOrg signupOrg =  (PrimaryOrg)OrgBuilder.aPrimaryOrg().build();
		
		class CreateConnectionTestClass extends SignUpFinalizationHandlerImpl {
			public CreateConnectionTestClass() {
				super(null, null, null, null, null);
			}

			@Override
			public UserBean getAdminUser() { return adminUser; }

			@Override
			public PrimaryOrg getPrimaryOrg() { return signupOrg; }
			
			@Override
			public OrgConnection createOrgConnection() {
				return super.createOrgConnection();
			}
		};
		
		CreateConnectionTestClass orgTestClass = new CreateConnectionTestClass();
		orgTestClass.setReferrerOrg(referrerOrg);
		
		OrgConnection conn = orgTestClass.createOrgConnection();
		
		assertSame(adminUser, conn.getModifiedBy());
		assertSame(referrerOrg, conn.getVendor());
		assertSame(signupOrg, conn.getCustomer());
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void throws_exception_on_null_referrer() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());
		
		signupFinal.finalizeSignUp(null);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void throws_exception_on_null_account_info() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null, null, null);
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());
		signupFinal.setReferrerOrg(new PrimaryOrg());
		
		signupFinal.finalizeSignUp(null);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void throws_exception_on_null_place_holder() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());
		signupFinal.setReferrerOrg(new PrimaryOrg());
		
		signupFinal.finalizeSignUp(null);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void throws_exception_on_null_approval() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setReferrerOrg(new PrimaryOrg());
		
		signupFinal.finalizeSignUp(null);
	}
	
}
