package com.n4systems.handlers.creator.signup;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import com.n4systems.handlers.TestUsesTransactionBase;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.promocode.PromoCode;
import com.n4systems.model.promocode.PromoCodeByCodeLoader;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.test.helpers.FluentHashSet;



public class ExtendedFeatureListResolverTest  extends TestUsesTransactionBase{

	private static final String PROMO_CODE_THAT_DOES_NOT_EXIST = "PROMO CODE THAT DOES NOT EXIST";
	private static final String PROMO_CODE_THAT_EXISTS = "PROMO CODE THAT EXISTS";
	private static final ExtendedFeature FEATURE_NOT_INCLUDED_IN_PACKAGE = ExtendedFeature.Integration;
	private static final String NO_PROMO_CODE = "";
	private static final SignUpPackageDetails SIGN_UP_PACKAGE_BEING_USED = SignUpPackageDetails.Basic;
	private static final String SOME_INVALIDE_PROMO_CODE = "";

	@Test
	public void should_return_only_get_extended_features_of_package_when_no_promo_code_given() {

		Set<ExtendedFeature> expectedFeatureList = new FluentHashSet<ExtendedFeature>(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures());
		
		PromoCodeByCodeLoader mockPromoByCodeLoader = createMockPromoCodeLoader(null, SOME_INVALIDE_PROMO_CODE);
		
		ExtendedFeatureListResolver sut = new ExtendedFeatureListResolver(mockPromoByCodeLoader);
		sut.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED).withPromoCode(NO_PROMO_CODE);
		
		
		// exercise
		Set<ExtendedFeature> actualFeatureList = sut.resolve(mockTransaction);
		
		// verify
		assertArrayEquals(expectedFeatureList.toArray(), actualFeatureList.toArray());
		
		verify(mockPromoByCodeLoader);
	}
	
	@Test
	public void should_return_only_get_extended_features_of_package_when_no_promo_code_found() {

		Set<ExtendedFeature> expectedFeatureList = new FluentHashSet<ExtendedFeature>(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures());
		
		PromoCodeByCodeLoader mockLoader = createMockPromoCodeLoader(null, PROMO_CODE_THAT_DOES_NOT_EXIST);
		
		ExtendedFeatureListResolver sut = new ExtendedFeatureListResolver(mockLoader);
		sut.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED).withPromoCode(PROMO_CODE_THAT_DOES_NOT_EXIST);
		
		
		// exercise
		Set<ExtendedFeature> actualFeatureList = sut.resolve(mockTransaction);
		
		// verify
		assertEquals(expectedFeatureList, actualFeatureList);
		
		
	}
	
	
	@Test
	public void should_return_a_list_of_extended_features_matching_the_package_when_the_promo_code_exists_but_has_no_features() {
		// fixture setup
		Set<ExtendedFeature> expectedFeatureList = new FluentHashSet<ExtendedFeature>(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures());
		
		PromoCode loadedPromoCode = new PromoCode();	
		
		PromoCodeByCodeLoader mockLoader = createMockPromoCodeLoader(loadedPromoCode, PROMO_CODE_THAT_EXISTS);
		
		ExtendedFeatureListResolver sut = new ExtendedFeatureListResolver(mockLoader);
		sut.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED).withPromoCode(PROMO_CODE_THAT_EXISTS);
		
		// exercise
		Set<ExtendedFeature> actualFeatureList = sut.resolve(mockTransaction);
		
		// verify
		verify(mockLoader);
		assertArrayEquals(expectedFeatureList.toArray(), actualFeatureList.toArray());
	}
	@Test
	public void should_return_a_list_of_extended_features_containing_everything_in_the_package_plus_the_feature_on_the_promo_code_that_is_not_included_in_the_package() {
	
		// fixture setup
		
		PromoCode loadedPromoCode = new PromoCode();
		loadedPromoCode.getExtendedFeatures().add(FEATURE_NOT_INCLUDED_IN_PACKAGE);	
		
		Set<ExtendedFeature> expectedFeatureList = new FluentHashSet<ExtendedFeature>(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures());
		expectedFeatureList.addAll(loadedPromoCode.getExtendedFeatures());
		
		PromoCodeByCodeLoader mockLoader = createMockPromoCodeLoader(loadedPromoCode, PROMO_CODE_THAT_EXISTS);
				
		ExtendedFeatureListResolver sut = new ExtendedFeatureListResolver(mockLoader);
		sut.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED).withPromoCode(PROMO_CODE_THAT_EXISTS);
		
		// exercise
		Set<ExtendedFeature> actualFeatureList = sut.resolve(mockTransaction);
		
		// verify
		verify(mockLoader);
		assertArrayEquals(expectedFeatureList.toArray(), actualFeatureList.toArray());
		
	}
	
	@Test
	public void should_return_a_list_of_extended_features_matching_the_package_when_the_promo_code_exists_and_has_a_feature_already_included_in_the_package() {
		// fixture setup
		
		PromoCode loadedPromoCode = new PromoCode();
		loadedPromoCode.getExtendedFeatures().addAll(Arrays.asList(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures()));	
		
		Set<ExtendedFeature> expectedFeatureList = new FluentHashSet<ExtendedFeature>(SIGN_UP_PACKAGE_BEING_USED.getExtendedFeatures());
		
		PromoCodeByCodeLoader mockLoader = createMockPromoCodeLoader(loadedPromoCode, PROMO_CODE_THAT_EXISTS);
				
		ExtendedFeatureListResolver sut = new ExtendedFeatureListResolver(mockLoader);
		sut.withSignUpPackageDetails(SIGN_UP_PACKAGE_BEING_USED).withPromoCode(PROMO_CODE_THAT_EXISTS);
		
		// exercise
		Set<ExtendedFeature> actualFeatureList = sut.resolve(mockTransaction);
		
		// verify
		verify(mockLoader);
		assertArrayEquals(expectedFeatureList.toArray(), actualFeatureList.toArray());
	}

	
	private PromoCodeByCodeLoader createMockPromoCodeLoader(PromoCode loadedPromoCode, String promoCode) {
		PromoCodeByCodeLoader mockLoader = createMock(PromoCodeByCodeLoader.class);
		
		mockLoader.setCode(promoCode);
		
		expect(mockLoader.load(mockTransaction)).andReturn(loadedPromoCode);
		
		replay(mockLoader);
		
		return mockLoader;
	}
}
