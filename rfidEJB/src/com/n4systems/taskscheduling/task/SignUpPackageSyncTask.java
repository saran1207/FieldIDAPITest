package com.n4systems.taskscheduling.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.n4systems.handlers.creator.CreateHandlerFacorty;
import com.n4systems.model.signuppackage.SignUpPackageSyncHandler;
import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.ContractPrice;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.taskscheduling.ScheduledTask;

public class SignUpPackageSyncTask extends ScheduledTask {

	public SignUpPackageSyncTask() {
	    super(5 * 60, TimeUnit.SECONDS);
	    
	}

	@Override
	protected void runTask(TaskConfig config) throws Exception {
		List<ContractPrice> contractPrices = retrieveContracts();
		syncContractsWithSignUpPackages(contractPrices);
	}

	private List<ContractPrice> retrieveContracts() throws CommunicationException {
		SubscriptionAgent subscriptionAgent = new CreateHandlerFacorty().getSubscriptionAgent();

		List<ContractPrice> contractPrices = subscriptionAgent.retrieveContractPrices();
		return contractPrices;
	}	
	
	private void syncContractsWithSignUpPackages(List<ContractPrice> contractPrices) {
		SignUpPackageSyncHandler signupPackageSyncHandler = new SignUpPackageSyncHandler(getNonSecureLoaderFactory().createContractPricingByNsRecordIdLoader(), 
				SaverFactory.createContractPricingSaver());
		
		signupPackageSyncHandler.setContractPrices(contractPrices);
		signupPackageSyncHandler.sync();
	}

	private NonSecureLoaderFactory getNonSecureLoaderFactory() {
		return new NonSecureLoaderFactory();
	}

	

}
