package com.n4systems.handlers.creator;

import com.n4systems.handlers.creator.signup.AccountPlaceHolderCreateHandler;
import com.n4systems.handlers.creator.signup.AccountPlaceHolderCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemSetupDataCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemStructureCreateHandler;
import com.n4systems.handlers.creator.signup.BaseSystemStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.BaseSystemTenantStructureCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.PrimaryOrgCreateHandler;
import com.n4systems.handlers.creator.signup.PrimaryOrgCreateHandlerImpl;
import com.n4systems.handlers.creator.signup.SignUpHandler;
import com.n4systems.handlers.creator.signup.SignUpHandlerImpl;
import com.n4systems.handlers.creator.signup.SubscriptionApprovalHandler;
import com.n4systems.handlers.creator.signup.SubscriptionApprovalHandlerImpl;
import com.n4systems.model.inspectiontypegroup.InspectionTypeGroupSaver;
import com.n4systems.model.producttype.ProductTypeSaver;
import com.n4systems.model.serialnumbercounter.SerialNumberCounterSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.tagoption.TagOptionSaver;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.SubscriptionAgentFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class CreateHandlerFacorty {

	
	
	
	public SignUpHandler getSignUpHandler() {
		return new SignUpHandlerImpl(getAccountPlaceHolderCreateHandler(), getBaseSystemStructureCreateHandler(), getSubscriptionAgent(), getSubscriptionApprovalHandler());
	}

	public SubscriptionApprovalHandler getSubscriptionApprovalHandler() {
		return new SubscriptionApprovalHandlerImpl(new OrganizationSaver(), new UserSaver());
	}

	public BaseSystemStructureCreateHandler getBaseSystemStructureCreateHandler() {
		return new BaseSystemStructureCreateHandlerImpl(new BaseSystemTenantStructureCreateHandlerImpl(new SetupDataLastModDatesSaver(),
				new SerialNumberCounterSaver()), new BaseSystemSetupDataCreateHandlerImpl(new TagOptionSaver(), new ProductTypeSaver(), new InspectionTypeGroupSaver(), new StateSetSaver()));
	}
	
	public AccountPlaceHolderCreateHandler getAccountPlaceHolderCreateHandler() {
		return new AccountPlaceHolderCreateHandlerImpl(new TenantSaver(), getPrimaryOrgCreateHandler(), new UserSaver());
	}
	
	public PrimaryOrgCreateHandler getPrimaryOrgCreateHandler() {
		return new PrimaryOrgCreateHandlerImpl(new OrganizationSaver());
	}

	public SubscriptionAgent getSubscriptionAgent() {
		return SubscriptionAgentFactory.createSubscriptionFactory(ConfigContext.getCurrentContext().getString(ConfigEntry.SUBSCRIPTION_AGENT));
	}
}
