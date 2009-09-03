package com.n4systems.subscription.netsuite.model;

public class GetSubscriptionDetailsResponse extends BaseResponse {

	private NetsuiteSubscriptionDetails subscription;

	public NetsuiteSubscriptionDetails getSubscription() {
		return subscription;
	}

	public void setSubscription(NetsuiteSubscriptionDetails subscription) {
		this.subscription = subscription;
	}

	
}
