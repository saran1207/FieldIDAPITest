package com.n4systems.handlers.creator.signup;

import com.n4systems.model.promocode.PromoCode;
import com.n4systems.model.promocode.PromoCodeByCodeLoader;
import com.n4systems.model.signuppackage.SignUpPackageDetails;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.persistence.Transaction;

public class LimitResolver {

	private final PromoCodeByCodeLoader promoCodeLoader;
	
	private SignUpPackageDetails signUpPackage;
	private String promoCode;

	private TenantLimit tenantLimit;
	
	public LimitResolver(PromoCodeByCodeLoader promoCodeLoader) {
		super();
		this.promoCodeLoader = promoCodeLoader;
	}
	
	
	public TenantLimit resolve(Transaction transaction) {
		tenantLimit = new TenantLimit();
		
		applyLimitsFromSignUpPackage();
		
		applyLimitsFromPromoCode(loadPromoCode(transaction));
		
		return tenantLimit;
	}


	private void applyLimitsFromPromoCode(PromoCode promoCode) {
		if (promoCode != null) {
			promoCode.getLimitAdjuster().adjustLimits(tenantLimit);
		}
	}


	private PromoCode loadPromoCode(Transaction transaction) {
		PromoCode promoCode = promoCodeLoader.setCode(this.promoCode).load(transaction);
		return promoCode;
	}


	private void applyLimitsFromSignUpPackage() {
		signUpPackage.getLimitAdjuster().adjustLimits(tenantLimit);
	}
	
	

	public LimitResolver withSignUpPackageDetails(SignUpPackageDetails signUpPackageDetails) {
		this.signUpPackage = signUpPackageDetails;
		return this;
	}

	public LimitResolver withPromoCode(String promoCode) {
		this.promoCode = promoCode;
		return this;
	}
}
