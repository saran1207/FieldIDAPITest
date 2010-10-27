package com.n4systems.fieldid.actions.integration;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.TagOption;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.plugins.PluginFactory;
import com.n4systems.plugins.integration.OrderResolver;
import com.n4systems.security.Permissions;

@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class OrderUtilAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(OrderUtilAction.class);
	
	private Option optionManager;
	private OrderManager orderManager;
	
	private Long asset;
	private String orderNumber;
	private List<LineItem> lineItems;
	
	public OrderUtilAction(OrderManager orderManager, Option optionManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.orderManager = orderManager;
		this.optionManager = optionManager;
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber( String orderNumber ) {
		this.orderNumber = orderNumber;
	}

	public List<LineItem> getLineItems() {
		if( lineItems == null && orderNumber != null ) {
			TagOption tagOption = optionManager.findTagOption(OptionKey.SHOPORDER, getSecurityFilter());
			
			Order order = null;
			if(tagOption == null || tagOption.getResolverClassName() == null) {
				// We didn't find a tag option or the option has no resolver class, lookup the order in the standard way.
				order = orderManager.findOrder(OrderType.SHOP, orderNumber, getTenantId(), getSecurityFilter());
				
			} else {
				// use the order plugin system
				OrderResolver resolver = PluginFactory.createResolver(tagOption.getResolverClassName());
				
				try {
					order = orderManager.processOrderFromPlugin(resolver, orderNumber, tagOption.getOptionKey().getOrderType(), getTenantId());
				} catch(OrderProcessingException e) {
					logger.error("Failed loading order [" + orderNumber + "], type [" + tagOption.getOptionKey().getOrderType().name() + "] from plugin system", e);
					addFlashErrorText("error.failedfindingorder");
				}
			}
			
			// if we found an order, now find the line items
			if(order != null) {
				lineItems = orderManager.findLineItems(order);
			} else {
				lineItems = new ArrayList<LineItem>();
			}
		}
		
		return lineItems;
	}

	public Long getAsset() {
		return asset;
	}

	public void setAsset( Long asset) {
		this.asset = asset;
	}
	
	public int getIdentifiedProductCount(LineItem lineItem) { 
		return orderManager.countAssetsTagged(lineItem);
	}
}
