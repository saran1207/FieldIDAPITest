package com.n4systems.handlers.creator.signup;

import static com.n4systems.test.helpers.creationmethods.PromoCodeCreationMethods.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.promocode.PromoCode;
import com.n4systems.model.promocode.PromoCodeByCodeLoader;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.LimitAdjuster;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.util.DataUnit;


public class LimitResolverTest extends TestUsesTransactionBase {

	private static final long DEFAULT_USER_LIMIT = 0L;
	private static SignUpPackageDetails SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED = SignUpPackageDetails.Basic;
	
	@Before
	public void setUp() {
		mockTransaction();
	}
	
	@Test
	public void should_set_limits_to_match_the_package_when_no_promo_code_given() {
		// fixture setup
		PromoCodeByCodeLoader mockPromoByCodeLoader = createMockPromoCodeLoader(null, NO_PROMO_CODE);
		
		TenantLimit expectedTenantLimit = new TenantLimit(DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getDiskSpaceInMB(), DataUnit.BYTES), 
															SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getAssets(), 
															DEFAULT_USER_LIMIT, SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getSecondaryOrgs(), 0L, 0L);
		
		LimitResolver sut = new LimitResolver(mockPromoByCodeLoader);
		sut.withPromoCode(NO_PROMO_CODE).withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED);
		// exercise
		TenantLimit actualLimits = sut.resolve(mockTransaction);
		// verify
		assertEquals(expectedTenantLimit, actualLimits);
		verify(mockPromoByCodeLoader);
	}
	
	@Test
	public void should_set_limits_to_match_the_package_when_promo_code_does_not_exist() {
		// fixture setup
		PromoCodeByCodeLoader mockPromoByCodeLoader = createMockPromoCodeLoader(null, PROMO_CODE_THAT_DOES_NOT_EXIST);
		
		TenantLimit expectedTenantLimit = new TenantLimit(DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getDiskSpaceInMB(), DataUnit.BYTES), 
															SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getAssets(), 
															DEFAULT_USER_LIMIT, SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getSecondaryOrgs(), 0L, 0L);
		
		LimitResolver sut = new LimitResolver(mockPromoByCodeLoader);
		sut.withPromoCode(PROMO_CODE_THAT_DOES_NOT_EXIST).withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED);
		
		// exercise
		TenantLimit actualLimits = sut.resolve(mockTransaction);

		// verify
		assertEquals(expectedTenantLimit, actualLimits);
		verify(mockPromoByCodeLoader);
	}
	
	@Test
	public void should_set_limits_to_match_the_package_when_the_promo_code_has_all_limits_set_to_zero() {
		PromoCodeByCodeLoader mockPromoByCodeLoader = createMockPromoCodeLoader(new PromoCode(), PROMO_CODE_THAT_EXISTS);
		
		TenantLimit expectedTenantLimit = new TenantLimit(DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getDiskSpaceInMB(), DataUnit.BYTES), 
															SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getAssets(), 
															DEFAULT_USER_LIMIT, SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getSecondaryOrgs(), 0L, 0L);
		
		LimitResolver sut = new LimitResolver(mockPromoByCodeLoader);
		sut.withPromoCode(PROMO_CODE_THAT_EXISTS).withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED);
		// exercise
		TenantLimit actualLimits = sut.resolve(mockTransaction);
		// verify
		assertEquals(expectedTenantLimit, actualLimits);
		verify(mockPromoByCodeLoader);
	}
	
	
	@Test
	public void should_set_limits_to_the_addition_of_the_package_and_promo_code() {
		// fixture setup
		PromoCode promoCode = new PromoCode();
		promoCode.setLimitAdjuster(new LimitAdjuster(10L, 0L, 20L));
		PromoCodeByCodeLoader mockPromoByCodeLoader = createMockPromoCodeLoader(promoCode, PROMO_CODE_THAT_EXISTS);
		
		long totalDiskSpace = DataUnit.MEBIBYTES.convertTo(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getDiskSpaceInMB(), DataUnit.BYTES) + 20L;
		long totalAssets = SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getAssets() + 10L;
		TenantLimit expectedTenantLimit = new TenantLimit(totalDiskSpace, totalAssets, DEFAULT_USER_LIMIT, SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED.getSecondaryOrgs(), 0L, 0L);
		
		LimitResolver sut = new LimitResolver(mockPromoByCodeLoader);
		sut.withPromoCode(PROMO_CODE_THAT_EXISTS).withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED_NO_LIMIT_SET_UNLIMITED);
		
		// exercise
		TenantLimit actualLimits = sut.resolve(mockTransaction);

		// verify
		assertEquals(expectedTenantLimit, actualLimits);
		verify(mockPromoByCodeLoader);
	}
	
	
	
	private PromoCodeByCodeLoader createMockPromoCodeLoader(PromoCode loadedPromoCode, String promoCode) {
		PromoCodeByCodeLoader mockLoader = createMock(PromoCodeByCodeLoader.class);
		
		expect(mockLoader.setCode(promoCode)).andReturn(mockLoader);
		
		expect(mockLoader.load(mockTransaction)).andReturn(loadedPromoCode);
		
		replay(mockLoader);
		
		return mockLoader;
	}
}
