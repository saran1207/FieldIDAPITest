package com.n4systems.taskscheduling.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.n4systems.model.signuppackage.SignUpPackageSyncHandler;
import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.subscription.SubscriptionAgentFactory;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class SignUpPackageSyncTask extends ScheduledTask {

	public SignUpPackageSyncTask() {
	    super(5 * 60, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask(TaskConfig config) throws Exception {
		SubscriptionAgent subscriptionAgent = SubscriptionAgentFactory.createSubscriptionFactory(ConfigContext.getCurrentContext().getString(ConfigEntry.SUBSCRIPTION_AGENT));

		List<ContractPrice> contractPrices = subscriptionAgent.retrieveContractPrices();
		
		SignUpPackageSyncHandler signupPackageSyncHandler = new SignUpPackageSyncHandler(NonSecureLoaderFactory.createContractPricingByNsRecordIdLoader(), 
				SaverFactory.createContractPricingSaver());
		
		signupPackageSyncHandler.setContractPrices(contractPrices);
		signupPackageSyncHandler.sync();
	}	

}
