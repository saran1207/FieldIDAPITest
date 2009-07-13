package com.n4systems.plugins.integration;

import com.n4systems.plugins.PluginException;

public interface OrderResolver {
	public boolean testConnection() throws PluginException;
	public ShopOrderTransfer findShopOrder(String orderNumber, String organizationName) throws PluginException;
	public CustomerOrderTransfer findCustomerOrder(String orderNumber, String organizationName) throws PluginException;
}
