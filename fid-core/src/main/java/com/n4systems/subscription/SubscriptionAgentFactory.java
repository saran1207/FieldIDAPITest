package com.n4systems.subscription;

public class SubscriptionAgentFactory {

	
	public static SubscriptionAgent createSubscriptionFactory(String subscriptionAgentName) {
		try {
			return (SubscriptionAgent) Class.forName(subscriptionAgentName).newInstance();
		} catch (Exception e) {
			throw new InvalidConfigurationException("SubscriptionAgent [" + subscriptionAgentName + "] could not be found.", e);
		}
	}
}
