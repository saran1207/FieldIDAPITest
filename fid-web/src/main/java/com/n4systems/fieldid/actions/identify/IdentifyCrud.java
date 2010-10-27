package com.n4systems.fieldid.actions.identify;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.TagOption;
import com.n4systems.model.Order.OrderType;
import com.n4systems.plugins.PluginFactory;
import com.n4systems.plugins.integration.OrderResolver;
import com.n4systems.security.Permissions;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Integration)
@UserPermissionFilter(userRequiresOneOf={Permissions.Tag})
public class IdentifyCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(IdentifyCrud.class);
	
	private OrderManager orderManager;
	private Option optionManager;
	
	private TagOption tagOption;
	private String orderNumber;
	private Order order;
	private List<LineItem> lineItems = new ArrayList<LineItem>();
	private List<TagOption> tagOptions;
	
	public IdentifyCrud(OrderManager orderManager, Option optionManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.orderManager = orderManager;
		this.optionManager = optionManager;
	}
	
	@Override
	protected void initMemberFields() {
		tagOptions = optionManager.findTagOptions(getSecurityFilter());
		
		// if we only have one tag option, then let's just set it directly
		if(tagOptions.size() == 1) {
			tagOption = tagOptions.get(0);
		}
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}

	public String doSearch() {
		return SUCCESS;
	}

	public String doShowOrder() {
		OrderType orderType = tagOption.getOptionKey().getOrderType();
		
		if(orderType == null || orderNumber == null) {
			return INPUT;
		}
		
		if(tagOption.getResolverClassName() == null) {
			// no resolver class is set, lookup in the usual way
			order = orderManager.findOrder(orderType, orderNumber, getTenantId(), getSecurityFilter());
		} else {
			// this tagOption has a resolver class set, we'll need to initialize a plugin
			OrderResolver resolver = PluginFactory.createResolver(tagOption.getResolverClassName());
			
			try {
				order = orderManager.processOrderFromPlugin(resolver, orderNumber, orderType, getTenantId());
			} catch(OrderProcessingException e) {
				logger.error("Failed loading order [" + orderNumber + "], type [" + orderType.name() + "] from plugin system", e);
				addFlashErrorText("error.failedfindingorder");
				return ERROR;
			}
			
			// if the order is still null, try looking up in the database
			if(order == null) {
				order = orderManager.findOrder(orderType, orderNumber, getTenantId(), getSecurityFilter());
			}
		}
		
		if(order != null) {
			lineItems = orderManager.findLineItems(order);
		}
		
		return SUCCESS;
	}

	public void setTagOptionId(Long tagOptionId) {
		setTagOption(optionManager.findTagOption(tagOptionId, getSecurityFilter()));
	}
	
	public Long getTagOptionId() {
		return tagOption != null ? tagOption.getId() : null;
	}
	
	public TagOption getTagOption() {
		return tagOption;
	}
	
	public void setTagOption(TagOption tagOption) {
		this.tagOption = tagOption;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = (orderNumber != null && orderNumber.trim().length() > 0) ? orderNumber.trim() : null;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public List<TagOption> getTagOptions() {
		return tagOptions;
	}
	
	public int getIdentifiedAssetCount(LineItem lineItem) {
		return orderManager.countAssetsTagged(lineItem);
	}
	
}
