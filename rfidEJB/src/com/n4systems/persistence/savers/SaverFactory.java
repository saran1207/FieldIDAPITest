package com.n4systems.persistence.savers;

import com.n4systems.model.signuppackage.ContractPricingSaver;

public class SaverFactory {

	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */
	
	public static ContractPricingSaver createContractPricingSaver() {
		return new ContractPricingSaver();
	}
}
