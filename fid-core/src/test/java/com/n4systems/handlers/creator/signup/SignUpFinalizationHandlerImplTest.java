package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.anAccountPlaceHolder;
import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
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
import com.n4systems.model.user.UserSaver;
import com.n4systems.test.helpers.FluentHashSet;
import com.n4systems.util.ConfigContext;
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
	public void should_find_tenant_list_from_list_resolver_and_have_them_turned_on() {
		AccountCreationInformationStub accountCreationInformationStub = new AccountCreationInformationStub();
		accountCreationInformationStub.setSignUpPackage(new SignUpPackage(SIGN_UP_PACKAGE_BEING_USED, new ArrayList<ContractPricing>()));
		accountCreationInformationStub.setPromoCode(SOME_PROMO_CODE);
		accountCreationInformationStub.setNumberOfUsers(100);
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		SignUpTenantResponseStub signUpTenantResponseStub = new SignUpTenantResponseStub();

		PrimaryOrg referrerOrg = aPrimaryOrg().build();

		Set<ExtendedFeature> featuresThatShouldBeAddedToPrimaryOrg = new FluentHashSet<ExtendedFeature>(ExtendedFeature.EmailAlerts);

		OrgSaver mockOrganizationSaver = createSuccessfulOrgSaver(accountPlaceHolder);

		UserSaver mockUserSaver = createSuccessfulUserSaver(accountPlaceHolder);

		LinkTenantHandler mockLinkTenantHandler = createSuccessfulLinkTenantHandler(accountPlaceHolder, referrerOrg);

		SignUpFinalizationHandler sut = new SignUpFinalizationHandlerImpl(mockOrganizationSaver, mockUserSaver, mockLinkTenantHandler);

		sut.setAccountInformation(accountCreationInformationStub).setAccountPlaceHolder(accountPlaceHolder)
				.setSubscriptionApproval(signUpTenantResponseStub).setReferrerOrg(referrerOrg);

		// exercise
		sut.finalizeSignUp(mockTransaction);

		// verify
		verify(mockOrganizationSaver);
		verify(mockUserSaver);
		verify(mockLinkTenantHandler);

		assertEquals(featuresThatShouldBeAddedToPrimaryOrg, accountPlaceHolder.getPrimaryOrg().getExtendedFeatures());

		assertNotNull(accountPlaceHolder.getPrimaryOrg().getExternalId());
		assertNotNull(accountPlaceHolder.getAdminUser().getExternalId());

		assertEquals(100, accountPlaceHolder.getTenant().getSettings().getMaxEmployeeUsers());
	}

	private LinkTenantHandler createSuccessfulLinkTenantHandler(AccountPlaceHolder accountPlaceHolder, PrimaryOrg referrerOrg) {
		LinkTenantHandler mockLinkTenantHandler = createMock(LinkTenantHandler.class);
		expect(mockLinkTenantHandler.setAccountPlaceHolder(accountPlaceHolder)).andReturn(mockLinkTenantHandler);
		expect(mockLinkTenantHandler.setReferrerOrg(referrerOrg)).andReturn(mockLinkTenantHandler);
		mockLinkTenantHandler.link(mockTransaction);
		replay(mockLinkTenantHandler);
		return mockLinkTenantHandler;
	}

	private UserSaver createSuccessfulUserSaver(AccountPlaceHolder accountPlaceHolder) {
		UserSaver mockUserSaver = createMock(UserSaver.class);
		expect(mockUserSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getAdminUser())).andReturn(accountPlaceHolder.getAdminUser());
		replay(mockUserSaver);
		return mockUserSaver;
	}

	private OrgSaver createSuccessfulOrgSaver(AccountPlaceHolder accountPlaceHolder) {
		OrgSaver mockOrganizationSaver = createMock(OrgSaver.class);
		expect(mockOrganizationSaver.saveOrUpdate(mockTransaction, accountPlaceHolder.getPrimaryOrg())).andReturn(
				accountPlaceHolder.getPrimaryOrg());
		replay(mockOrganizationSaver);
		return mockOrganizationSaver;
	}

	@Test(expected = InvalidArgumentException.class)
	public void throws_exception_on_null_referrer() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());

		signupFinal.finalizeSignUp(null);
	}

	@Test(expected = InvalidArgumentException.class)
	public void throws_exception_on_null_account_info() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null);
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());
		signupFinal.setReferrerOrg(new PrimaryOrg());

		signupFinal.finalizeSignUp(null);
	}

	@Test(expected = InvalidArgumentException.class)
	public void throws_exception_on_null_place_holder() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setSubscriptionApproval(new SignUpTenantResponseStub());
		signupFinal.setReferrerOrg(new PrimaryOrg());

		signupFinal.finalizeSignUp(null);
	}

	@Test(expected = InvalidArgumentException.class)
	public void throws_exception_on_null_approval() {
		SignUpFinalizationHandlerImpl signupFinal = new SignUpFinalizationHandlerImpl(null, null, null);
		signupFinal.setAccountInformation(new AccountCreationInformationStub());
		signupFinal.setAccountPlaceHolder(new AccountPlaceHolder(null, null, null, null));
		signupFinal.setReferrerOrg(new PrimaryOrg());

		signupFinal.finalizeSignUp(null);
	}

}
