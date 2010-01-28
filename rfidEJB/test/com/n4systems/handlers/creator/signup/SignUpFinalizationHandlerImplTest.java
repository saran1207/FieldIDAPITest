package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.signuppackage.ContractPricing;
import com.n4systems.model.signuppackage.SignUpPackage;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.user.UserSaver;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DataUnit;
import com.n4systems.util.NonDataSourceBackedConfigContext;


public class SignUpFinalizationHandlerImplTest extends TestUsesTransactionBase {

	private static final SignUpPackageDetails SIGN_UP_PACKAGE_BEING_USED = SignUpPackageDetails.Basic;
	private static final String SOME_PROMO_CODE = "SOME PROMO CODE";
	private ConfigContext oldContext;


	@Before
	public void setUp() {
		mockTransaction();
		oldContext = ConfigContext.getCurrentContext(); 
		ConfigContext.setCurrentContext(new NonDataSourceBackedConfigContext());
	}
	
	@After
	public void tearDown() {
		ConfigContext.setCurrentContext(oldContext);
	}
	
	
	@Test
	public void should_set_default_vendor_context_to_refering_org_if_refering_org_is_not_the_house_account() throws Exception {
		PrimaryOrg referrerOrg = aPrimaryOrg().build();
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		
		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(createSuccessfulExtendedFeatureResolver(SIGN_UP_PACKAGE_BEING_USED, null, new HashSet<ExtendedFeature>()), createSuccessfulOrgSaver(accountPlaceHolder), createSuccessfulUserSaver(accountPlaceHolder), createSuccessfulLimitResolver(SIGN_UP_PACKAGE_BEING_USED, null, new TenantLimit()), createSuccessfulLinkTenantHandler(accountPlaceHolder, referrerOrg));
		sut.setAccountInformation(new AccountCreationInformationStub(SIGN_UP_PACKAGE_BEING_USED)).setSubscriptionApproval(new SignUpTenantResponseStub());
		
		sut.setAccountPlaceHolder(accountPlaceHolder).setReferrerOrg(referrerOrg);
	
		// exercise
		sut.finalizeSignUp(mockTransaction);
		
		
		assertEquals(referrerOrg.getId(), accountPlaceHolder.getPrimaryOrg().getDefaultVendorContext());
	}
	
	@Test
	public void should_not_set_default_vendor_context_when_refering_org_is_the_house_account() throws Exception {
		PrimaryOrg houseAccountReferrerOrg = aPrimaryOrg().build();
		houseAccountReferrerOrg.setId(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID));
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		
		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(createSuccessfulExtendedFeatureResolver(SIGN_UP_PACKAGE_BEING_USED, null, new HashSet<ExtendedFeature>()), createSuccessfulOrgSaver(accountPlaceHolder), createSuccessfulUserSaver(accountPlaceHolder), createSuccessfulLimitResolver(SIGN_UP_PACKAGE_BEING_USED, null, new TenantLimit()), createSuccessfulLinkTenantHandler(accountPlaceHolder, houseAccountReferrerOrg));
		sut.setAccountInformation(new AccountCreationInformationStub(SIGN_UP_PACKAGE_BEING_USED)).setSubscriptionApproval(new SignUpTenantResponseStub());
		
		sut.setAccountPlaceHolder(accountPlaceHolder).setReferrerOrg(houseAccountReferrerOrg);
	
		// exercise
		sut.finalizeSignUp(mockTransaction);
		
		
		assertNull(accountPlaceHolder.getPrimaryOrg().getDefaultVendorContext());
	}
	
	@Test
	public void should_find_tenant_list_from_list_resolver_and_have_them_turned_on() {
		AccountCreationInformationStub accountCreationInformationStub = new AccountCreationInformationStub();
		accountCreationInformationStub.setSignUpPackage(new SignUpPackage(SIGN_UP_PACKAGE_BEING_USED, new ArrayList<ContractPricing>()));
		accountCreationInformationStub.setPromoCode(SOME_PROMO_CODE);
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		SignUpTenantResponseStub signUpTenantResponseStub = new SignUpTenantResponseStub();
		
		PrimaryOrg referrerOrg = aPrimaryOrg().build();
		
		TenantLimit expectedTenantLimit = new TenantLimit();
		expectedTenantLimit.setAssets(SIGN_UP_PACKAGE_BEING_USED.getAssets());
		expectedTenantLimit.setUsers(accountCreationInformationStub.getNumberOfUsers().longValue());
		expectedTenantLimit.setDiskSpaceInBytes(DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED.getDiskSpaceInMB(), DataUnit.BYTES));
		
		TenantLimit limitFromResolver = new TenantLimit();
		limitFromResolver.setAssets(SIGN_UP_PACKAGE_BEING_USED.getAssets());
		limitFromResolver.setDiskSpaceInBytes(DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED.getDiskSpaceInMB(), DataUnit.BYTES));
		
		Set<ExtendedFeature> featuresThatShouldBeAddedToPrimaryOrg = new FluentHashSet<ExtendedFeature>(ExtendedFeature.Integration);
		
		// fixture setup
		ExtendedFeatureListResolver mockExtendedFeatureListResolver = createSuccessfulExtendedFeatureResolver(SIGN_UP_PACKAGE_BEING_USED, SOME_PROMO_CODE, featuresThatShouldBeAddedToPrimaryOrg);
		LimitResolver mockLimitResolver = createSuccessfulLimitResolver(SIGN_UP_PACKAGE_BEING_USED, SOME_PROMO_CODE, limitFromResolver);
		
		OrgSaver mockOrganizationSaver = createSuccessfulOrgSaver(accountPlaceHolder);
		
		UserSaver mockUserSaver = createSuccessfulUserSaver(accountPlaceHolder);
		
		
		

		LinkTenantHandler mockLinkTenantHandler = createSuccessfulLinkTenantHandler(accountPlaceHolder, referrerOrg);
		
		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(mockExtendedFeatureListResolver, mockOrganizationSaver, mockUserSaver, mockLimitResolver, mockLinkTenantHandler);
		
		
		sut.setAccountInformation(accountCreationInformationStub)
				.setAccountPlaceHolder(accountPlaceHolder)
				.setSubscriptionApproval(signUpTenantResponseStub)
				.setReferrerOrg(referrerOrg);
			
		// exercise
		sut.finalizeSignUp(mockTransaction);

		// verify
		verify(mockExtendedFeatureListResolver);
		verify(mockOrganizationSaver);
		verify(mockUserSaver);
		verify(mockLimitResolver);
		verify(mockLinkTenantHandler);
		
		
		assertEquals(featuresThatShouldBeAddedToPrimaryOrg, accountPlaceHolder.getPrimaryOrg().getExtendedFeatures());
		
		assertNotNull(accountPlaceHolder.getPrimaryOrg().getExternalId());
		assertNotNull(accountPlaceHolder.getAdminUser().getExternalId());
		
		assertEquals(expectedTenantLimit, accountPlaceHolder.getPrimaryOrg().getLimits());
	}


	private LinkTenantHandler createSuccessfulLinkTenantHandler(AccountPlaceHolder accountPlaceHolder, PrimaryOrg referrerOrg) {
		LinkTenantHandler mockLinkTenantHandler = createMock(LinkTenantHandler.class);
		expect(mockLinkTenantHandler.setAccountPlaceHolder(accountPlaceHolder)).andReturn(mockLinkTenantHandler);
		expect(mockLinkTenantHandler.setReferrerOrg(referrerOrg)).andReturn(mockLinkTenantHandler);
		mockLinkTenantHandler.link(mockTransaction);
		replay(mockLinkTenantHandler);
		return mockLinkTenantHandler;
	}


	private LimitResolver createSuccessfulLimitResolver(SignUpPackageDetails signUpPackageBeingUsed, String somePromoCode, TenantLimit limitFromResolver) {
		LimitResolver mockLimitResolver = createMock(LimitResolver.class);
		expect(mockLimitResolver.withSignUpPackageDetails(signUpPackageBeingUsed)).andReturn(mockLimitResolver);
		expect(mockLimitResolver.withPromoCode(somePromoCode)).andReturn(mockLimitResolver);
		expect(mockLimitResolver.resolve(mockTransaction)).andReturn(limitFromResolver);
		replay(mockLimitResolver);
		return mockLimitResolver;
	}


	private ExtendedFeatureListResolver createSuccessfulExtendedFeatureResolver(SignUpPackageDetails signUpPackageBeingUsed, String somePromoCode,
			Set<ExtendedFeature> featuresThatShouldBeAddedToPrimaryOrg) {
		ExtendedFeatureListResolver mockExtendedFeatureListResolver = createMock(ExtendedFeatureListResolver.class);
		expect(mockExtendedFeatureListResolver.withSignUpPackageDetails(signUpPackageBeingUsed)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.withPromoCode(somePromoCode)).andReturn(mockExtendedFeatureListResolver);
		expect(mockExtendedFeatureListResolver.resolve(mockTransaction)).andReturn(featuresThatShouldBeAddedToPrimaryOrg);
		replay(mockExtendedFeatureListResolver);
		return mockExtendedFeatureListResolver;
	}


	private UserSaver createSuccessfulUserSaver(AccountPlaceHolder accountPlaceHolder) {
		UserSaver mockUserSaver = createMock(UserSaver.class);
		expect(mockUserSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getAdminUser())).andReturn(accountPlaceHolder.getAdminUser());
		replay(mockUserSaver);
		return mockUserSaver;
	}


	private OrgSaver createSuccessfulOrgSaver(AccountPlaceHolder accountPlaceHolder) {
		OrgSaver mockOrganizationSaver = createMock(OrgSaver.class);
		expect(mockOrganizationSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getPrimaryOrg())).andReturn(accountPlaceHolder.getPrimaryOrg());
		replay(mockOrganizationSaver);
		return mockOrganizationSaver;
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
