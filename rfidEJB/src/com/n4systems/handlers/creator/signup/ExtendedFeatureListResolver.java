package com.n4systems.handlers.creator.signup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.promocode.PromoCode;
import com.n4systems.model.promocode.PromoCodeByCodeLoader;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.persistence.Transaction;

public class ExtendedFeatureListResolver {
	private final PromoCodeByCodeLoader promoCodeLoader;
	
	private SignUpPackageDetails signUpPackage;
	private String promoCode;
	

	public ExtendedFeatureListResolver(PromoCodeByCodeLoader promoCodeLoader) {
		super();
		this.promoCodeLoader = promoCodeLoader;
	}
	
	
	public Set<ExtendedFeature> resolve(Transaction mockTransaction) {
		Set<ExtendedFeature> extendedFeaturesToTurnOn = new HashSet<ExtendedFeature>();
	
		extendedFeaturesToTurnOn.addAll(Arrays.asList(signUpPackage.getExtendedFeatures()));
		extendedFeaturesToTurnOn.addAll(getFeaturesFromPromoCode(loadPromoCode(mockTransaction)));
		
		return extendedFeaturesToTurnOn; 
	}


	private List<ExtendedFeature> getFeaturesFromPromoCode(PromoCode code) {
		List<ExtendedFeature> features = new ArrayList<ExtendedFeature>();
		
		
		if (code != null) {
			features.addAll(code.getExtendedFeatures());
		}
		
		return features;
	}


	private PromoCode loadPromoCode(Transaction mockTransaction) {
		return promoCodeLoader.setCode(promoCode).load(mockTransaction);
	}




	public ExtendedFeatureListResolver withSignUpPackageDetails(SignUpPackageDetails signUpPackageDetails) {
		this.signUpPackage = signUpPackageDetails;
		return this;
	}

	public ExtendedFeatureListResolver withPromoCode(String promoCode) {
		this.promoCode = promoCode;
		return this;
	}

}
