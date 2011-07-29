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
