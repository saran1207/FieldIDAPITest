package com.n4systems.subscription;


public class UpgradeResponse {

	private final UpgradeCost cost;
	private final Long contractId;
	
	public UpgradeResponse(UpgradeCost cost, Long contractId) {
		super();
		this.cost = cost;
		this.contractId = contractId;
	}

	public UpgradeCost getCost() {
		return cost;
	}

	public Long getContractId() {
		return contractId;
	}

	
}
