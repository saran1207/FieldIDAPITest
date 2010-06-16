package com.n4systems.fieldid.actions.subscriptions.plansandpricing;


import com.n4systems.model.Tenant;
import com.n4systems.util.StringUtils;
import com.n4systems.util.uri.BaseUrlBuilder;

public class PlansAndPricingUrlBuilder extends BaseUrlBuilder {
	
	public PlansAndPricingUrlBuilder(String plansAndPricingUrl, Tenant refTenant, String refCode) {
		super(plansAndPricingUrl);
		
		initializeParameters(refTenant, refCode);
	}

	private void initializeParameters(Tenant refTenant, String refCode) {
		addParameter("companyId", refTenant.getName());
		
		if (StringUtils.isNotEmpty(refCode)) {
			addParameter("refCode", refCode);
		}
	}

	@Override
	protected String path() {
		return "";
	}
}
