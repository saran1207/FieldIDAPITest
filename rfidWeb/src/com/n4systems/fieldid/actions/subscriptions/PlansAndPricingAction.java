package com.n4systems.fieldid.actions.subscriptions;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.subscriptions.plansandpricing.PlansAndPricingUrlBuilder;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.uri.UrlBuilder;

public class PlansAndPricingAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final ConfigContext configContext;
	private String refCode;
	
	protected PlansAndPricingAction(PersistenceManager persistenceManager, ConfigContext configContext) {
		super(persistenceManager);	
		this.configContext = ConfigContext.getCurrentContext();
	}
	
	public PlansAndPricingAction(PersistenceManager persistenceManager) {
		this(persistenceManager, ConfigContext.getCurrentContext());
	}
	
	public String doShowPlansAndPricing() {
		String actionResult = isExternalPlansAndPricingEnabled() ? "external" : "internal";
		
		return actionResult;
	}

	private boolean isExternalPlansAndPricingEnabled() {
		return configContext.getBoolean(ConfigEntry.EXTERNAL_PLANS_AND_PRICING_ENABLED);
	}
	
	private String getExternalPlansAndPricingUrl() {
		return configContext.getString(ConfigEntry.PLANS_AND_PRICING_URL);
	}

	public String getExternalUrl() {
		UrlBuilder urlBuilder = new PlansAndPricingUrlBuilder(getExternalPlansAndPricingUrl(), getTenant(), refCode);
		
		String externalUrl = urlBuilder.build();
		return externalUrl;
	}

	public String getRefCode() {
		return refCode;
	}
	
	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
}
