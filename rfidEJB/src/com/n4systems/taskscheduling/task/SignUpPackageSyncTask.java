package com.n4systems.taskscheduling.task;

import java.util.concurrent.TimeUnit;

import com.n4systems.model.signuppackage.SignUpPackageSyncHandler;
import com.n4systems.model.taskconfig.TaskConfig;
import com.n4systems.netsuite.client.ProductDetailsClient;
import com.n4systems.netsuite.model.GetItemDetailsResponse;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;
import com.n4systems.persistence.savers.SaverFactory;
import com.n4systems.taskscheduling.ScheduledTask;

public class SignUpPackageSyncTask extends ScheduledTask {

	public SignUpPackageSyncTask() {
	    super(5 * 60, TimeUnit.SECONDS);
	}

	@Override
	protected void runTask(TaskConfig config) throws Exception {
		ProductDetailsClient productDetailsClient = new ProductDetailsClient();		
		GetItemDetailsResponse response = productDetailsClient.execute();

		SignUpPackageSyncHandler signupPackageSyncHandler = new SignUpPackageSyncHandler(NonSecureLoaderFactory.createContractPricingByNsRecordIdLoader(), 
				SaverFactory.createContractPricingSaver());
		
		signupPackageSyncHandler.setProductInformations(response.getItemlist());
		signupPackageSyncHandler.sync();
	}	
	

}
