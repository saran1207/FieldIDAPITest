package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import org.apache.log4j.Logger;

import rfid.ejb.entity.OrderMappingBean;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.OrderMapping;
import com.n4systems.ejb.legacy.impl.OrderMappingManager;
import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.LegacyFindOrCreateCustomerOrgHandler;
import com.n4systems.model.orgs.LegacyFindOrCreateDivisionOrgHandler;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.plugins.PluginException;
import com.n4systems.plugins.integration.CustomerOrderTransfer;
import com.n4systems.plugins.integration.OrderResolver;
import com.n4systems.plugins.integration.OrderTransfer;
import com.n4systems.plugins.integration.ShopOrderTransfer;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
@SuppressWarnings("deprecation")
 
public class OrderManagerImpl implements OrderManager {
	private Logger logger = Logger.getLogger(OrderManagerImpl.class);
	
	 private PersistenceManager persistenceManager;
	 private OrderMapping orderMappingManager;
	
	public OrderManagerImpl(EntityManager em) {
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.orderMappingManager = new OrderMappingManager(em);
	}
	
	
	
	public Order findOrder(OrderType type, String orderNumber, Long tenantId, SecurityFilter filter) {
		QueryBuilder<Order> builder = new QueryBuilder<Order>(Order.class, new OpenSecurityFilter());

		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addWhere(WhereParameter.Comparator.EQ, "orderNumber", "orderNumber", orderNumber, WhereParameter.IGNORE_CASE);
		builder.addSimpleWhere("orderType", type);

		if (filter != null) {
			builder.applyFilter(filter);
		}
		
		Order order = null;
		try {
			order = persistenceManager.find(builder);
		} catch(Exception e) {
			logger.error("Failed finding order", e);
		}
		
		return order;
	}
	
	public LineItem findLineItem(Order order, String lineId) {
		QueryBuilder<LineItem> builder = new QueryBuilder<LineItem>(LineItem.class, new OpenSecurityFilter());
		builder.addSimpleWhere("order", order);
		builder.addSimpleWhere("lineId", lineId);
		
		LineItem lineItems = null;
		try {
			lineItems = persistenceManager.find(builder);
		} catch(Exception e) {
			logger.error("Failed finding LineItem", e);
		}
		
		return lineItems;
	}
	
	public List<LineItem> findLineItems(Order order) {
		QueryBuilder<LineItem> builder = new QueryBuilder<LineItem>(LineItem.class, new OpenSecurityFilter());
		builder.addSimpleWhere("order", order);
		builder.setOrder("idx");
		
		List<LineItem> lineItems = new ArrayList<LineItem>();
		try {
			lineItems = persistenceManager.findAll(builder);
		} catch(Exception e) {
			logger.error("Failed finding LineItems", e);
		}
		
		return lineItems;
	}
	
	public int countLineItems(Order order) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(LineItem.class, new OpenSecurityFilter());
		builder.setCountSelect();
		builder.addSimpleWhere("order", order);
		
		Long lineItems = null;
		try {
			lineItems = persistenceManager.find(builder);
		} catch(Exception e) {
			logger.error("Failed counting LineItems", e);
		}
		
		return (lineItems != null) ? lineItems.intValue() : 0;
	}
	
	public int countProductsTagged(LineItem lineItem) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Asset.class, new OpenSecurityFilter());
		builder.setCountSelect();
		builder.addSimpleWhere("shopOrder", lineItem);
		
		Long lineItems = null;
		try {
			lineItems = persistenceManager.find(builder);
		} catch(Exception e) {
			logger.error("Failed counting LineItems", e);
		}
		
		return (lineItems != null) ? lineItems.intValue() : 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> processOrders(Map<String, Object> unmappedOrders) throws OrderProcessingException {

		// if we're logging in trace, debug print the order message
		if (logger.isTraceEnabled()) {
			debugOrders(unmappedOrders);
		}
		
		String tenantName = (String)unmappedOrders.get(ORGANIZATION_ID);
		String sourceId = (String)unmappedOrders.get(EXTERNAL_SOURCE_ID);
		
		// First lets ensure we know this organization
		Tenant tenant = persistenceManager.findByName(Tenant.class, tenantName);
		
		if (tenant == null) {
			logger.error("Unable to locate Tenant for organizationID [" + tenantName + "] externalSourceID [" + sourceId + "]");
			throw new OrderProcessingException("Unknown organizationID " + tenantName);
		}
		
		logger.info("Started Order processing for Tenant [" + tenant.getName() + "] externalSourceID [" + sourceId + "]");
		
		// load the mappings for this soruce and tenant
		Map<String, OrderKey> keyMappings = orderMappingManager.getKeyMappings(tenant, sourceId);
		
		// now process each order
		Collection<Map<String, Object>> rawOrders = (Collection<Map<String, Object>>)unmappedOrders.get(ORDERS);
		logger.info("Found [" + rawOrders.size() + "] Orders for Tenant [" + tenant.getName() + "] externalSourceID [" + sourceId + "]");
		
		List<Order> orders = new ArrayList<Order>();
		for (Map<String, Object> rawOrder: rawOrders) {
			try {
				orders.add(processOrder(tenant, sourceId, rawOrder, keyMappings));
			} catch(OrderProcessingException e) {
				logger.warn("Failed processing Order for Tenant [" + tenant.getName() + "] externalSourceID [" + sourceId + "].  Continuing to next Order ...", e);
			}
		}
		
		logger.info("Completed order processing for tenant [" + tenant.getName() + "] organizationID [" + tenantName + "] externalSourceID [" + sourceId + "].  Processed [" + orders.size() + "] orders");
		
		return orders;
	}
	
	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData) throws OrderProcessingException {
		return processOrder(tenant, sourceId, rawOrderData, null);
	}
	
	@SuppressWarnings("unchecked")
	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData, Map<String, OrderKey> keyMappings) throws OrderProcessingException {
		
		// lookup the key mappings if they're not set
		if(keyMappings == null) {
			keyMappings = orderMappingManager.getKeyMappings(tenant, sourceId);
		}
		
		// map the data into a better format
		Map<OrderKey, String> orderData = mapData(keyMappings, rawOrderData);
		
		// make sure we have our prerequisite values
		OrderType type = OrderType.resolveByLegacyName((String)rawOrderData.get(ORDER_TYPE));
		if(type == null) {
			throw new OrderProcessingException("Unable to resolve order type from '" + rawOrderData.get(ORDER_TYPE) + "'");
		}
		
		String orderNumber = orderData.get(OrderKey.ORDER_NUMBER);
		if(orderNumber == null || orderNumber.length() == 0) {
			throw new OrderProcessingException("Order Number is required");
		}
		
		// we'll try first to resolve this order in the system 
		Order order = findOrder(type, orderNumber, tenant.getId(), null);
		
		// if we didn't find one, then it's a new order
		if(order == null) {
			order = new Order(type, orderNumber);
			order.setTenant(tenant);
		}

		logger.info("Processing Order: Tenant [" + tenant.getName() + "], Type [" + order.getOrderType() + "], OrderNumber [" + order.getOrderNumber() + "]");
		
		/*
		 *  at this point, regardless of if it's a new order or not, we'll set in all the data.
		 *  This means that even on update, all non-required fields will get rewritten.
		 */
		
		CustomerOrg customer;
		DivisionOrg division;
			
		customer = processCustomer(orderData.get(OrderKey.ORDER_CUSTOMER_NAME), orderData.get(OrderKey.ORDER_CUSTOMER_ID), tenant);
		division = processDivision(orderData.get(OrderKey.ORDER_DIVISION_NAME), customer); 
		
		
		
		order.setOwner((division != null) ? division : customer);

		// now we'll fill in the rest of the order data
		String value;
		for(OrderKey key: orderData.keySet()) {
			value = orderData.get(key);
			switch(key) {
				case ORDER_NUMBER:
				case ORDER_CUSTOMER_ID:
				case ORDER_CUSTOMER_NAME:
				case ORDER_DIVISION_NAME:
					// handled above
					continue;
				case ORDER_DATE:
					order.setOrderDate(DateHelper.string2Date("yyyyMMdd", value));
					break;
				case ORDER_DESCRIPTION:
					order.setDescription(value);
					break;
				case ORDER_PO_NUMBER:
					order.setPoNumber(value);
					break;
				default:
					logger.warn("Unknown Key in order '" + key.name() + "' with value '" + value + "'");
					break;
			}
		}
		
		// time to save our order
		if(order.isNew()) {
			logger.info("Saving Order: OrderNumber [" + order.getOrderNumber() + "]");
			
			persistenceManager.save(order);
		} else {
			logger.info("Updating Order: OrderNumber [" + order.getOrderNumber() + "]");
			
			order = persistenceManager.update(order);
		}
		
		
		// Only SHOP orders have line items, we set the lineCount to -1 to reflect this.
		int lineCount = -1;
		if(order.getOrderType().equals(OrderType.SHOP)) {
			// now we can process our line items
			Collection<Map<String,Object>> rawLineItems = (Collection<Map<String,Object>>)rawOrderData.get(LINE_ITEMS);
			
			logger.info("Found [" + rawLineItems.size() + "] LineItems on Order [" + order.getOrderNumber() + "]");
			
			lineCount = 0;
			for(Map<String,Object> rawLineItemdata: rawLineItems) {
				try {
					processLineItem(order, sourceId, rawLineItemdata, keyMappings);
					lineCount++;
				} catch(OrderProcessingException e) {
					logger.warn("Failed during LineItem processing on Order [" + order.getOrderNumber() + "].  Continuing to next LineItem ...", e);
				}
			}
		}
		
		logger.info("Completed processing of Order: Tenant [" + tenant.getName() + "], Type [" + order.getOrderType() + "], OrderNumber [" + order.getOrderNumber() + "].  Processed [" + lineCount + "] lines.");
		
		return order;
	}
	
	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData) throws OrderProcessingException {
		return processLineItem(order, sourceId, rawLineItemData, null);
	}
	
	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData, Map<String, OrderKey> keyMappings) throws OrderProcessingException {
		// our order must exist
		if(order == null || order.getId() == null) {
			throw new OrderProcessingException("Cannot process line item for non-existant order");
		}
		
		// lookup the key mappings if they're not set
		if(keyMappings == null) {
			keyMappings = orderMappingManager.getKeyMappings(order.getTenant(), sourceId);
		}
		
		Map<OrderKey, String> lineItemData = mapData(keyMappings, rawLineItemData);
		
		// we'll try first to resolve this line item by it's line id.  If lineId is null, then it's assumed to be new.
		String lineId = lineItemData.get(OrderKey.LINE_ID);
		
		LineItem lineItem = null;
		if(lineId != null) {
			lineItem = findLineItem(order, lineId);
		}
		
		// create the line item if it was not resolved before
		if(lineItem == null) {
			lineItem = new LineItem(order);
		}
		
		String productCode = lineItemData.get(OrderKey.LINE_PRODUCT_CODE);
		
		if (productCode != null) {
			logger.info("Processing Line Item with Asset Code [" + productCode + "] for Order [" + order.getOrderNumber() + "]");
			
			lineItem.setAssetCode(productCode);
		} else {
			logger.warn("Line Item for Order [" + order.getOrderNumber() + "] had empty Asset Code.  Using default ...");
			lineItem.setAssetCode(ConfigContext.getCurrentContext().getString(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME, order.getTenant().getId()));
		}
		
		String value;
		for(OrderKey key: lineItemData.keySet()) {
			value = lineItemData.get(key);
			switch(key) {
				case LINE_PRODUCT_CODE:
					// handled above
					continue;
				case LINE_ID:
					lineItem.setLineId(value);
					break;
				case LINE_QUANTITY:
					try {
						//some integration systems send quantity as a float
						lineItem.setQuantity(Double.valueOf(value).intValue());
					} catch(NumberFormatException e) {
						logger.warn("Line Item had unparsable quantity '" + value + "'");
					}
					break;
				case LINE_DESCRIPTION:
					lineItem.setDescription(value);
					break;
				default:
					logger.warn("Unknown Key in line item '" + key.name() + "' with value '" + value + "'");
					break;
			}
		}
		
		// if no index was set for this line item, then we'll set one now
		if(!lineItem.isIndexSet()) {
			lineItem.setIndex(countLineItems(order));
		}
		
		// now we can save it
		if(lineItem.isNew()) {
			logger.info("Creating new line item [" + lineItem.getAssetCode() + "] Order [" + order.getOrderNumber() + "]");
			
			persistenceManager.save(lineItem);
		} else {
			logger.info("Updating line item [" + lineItem.getAssetCode() + "] Order [" + order.getOrderNumber() + "]");
			
			lineItem = persistenceManager.update(lineItem);
		}
		
		return lineItem;
	}
	
	/**
	 * Attempts to resolve or create a customer.  If customerName and customerId are null, then null 
	 * will be returned.  If customerName is null then customerId will be used for both values.  If
	 * customerId is null then customerName will be used for both values.
	 * @param customerName	name of the customer
	 * @param customerCode	short name of the customer
	 * @param tenant		a Tenant
	 * @return				A customer or null
	 * @throws DuplicateExtenalOrgException 
	 */
	private CustomerOrg processCustomer(String customerName, String customerCode, Tenant tenant) {
		// if neither are set, do nothing
		if(customerName == null && customerCode == null) {
			return null;
		}
		
		// both customerName and customerId must be set for findOrCreateCustomer
		if(customerName == null) {
			customerName = customerCode;
		} else if(customerCode == null) {
			customerCode = customerName;
		}
		
		PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(tenant.getId());
		
		LegacyFindOrCreateCustomerOrgHandler findOrCreateCust = getFindOrCreateCustomerHandler();
		
		CustomerOrg customer = findOrCreateCust.findOrCreate(primaryOrg, customerName, customerCode);
		return customer;
	}
	
	/**
	 * Attempts to resolve or create a division.  Will return null if either divisionName or customer is null.
	 * @param divisionName	Name of a division
	 * @param customer		An existing customer in the system
	 * @return				A Division or null
	 * @throws DuplicateExtenalOrgException 
	 */
	private DivisionOrg processDivision(String divisionName, CustomerOrg customer) {
		// we'll need both of these to do anything
		if(divisionName == null || customer == null) {
			return null;
		}
		
		LegacyFindOrCreateDivisionOrgHandler findOrCreateDiv = getFindOrCreateDivisionHandler();
		
		DivisionOrg division = findOrCreateDiv.findOrCreate(customer, divisionName);
		return division;
	}
	
	/**
	 * Maps raw order data into a format that is easier to work with.  The rawData keys are resolved
	 * against the keyMap keys and the data values are converts to Strings and trimmed.  Note that rawData values for
	 * keys that do not exist in the keyMap will be discarded.<br/>This means any meta-data values such as
	 * the {@link OrderMapping#LINE_ITEMS} or {@link OrderMapping#ORDER_TYPE} <b>WILL NOT</b> be present
	 * in the resulting Map. <br/>Null or zero-length values will also be discarded.
	 * @param keyMap	A Map of {@link OrderMappingBean#getSourceOrderKey()} to {@link OrderKey} 
	 * @param rawData	A Map of {@link OrderMappingBean#getSourceOrderKey()} to their values
	 * @return	A Map of {@link OrderKey} to rawData values
	 */
	private Map<OrderKey, String> mapData(Map<String, OrderKey> keyMap, Map<String, Object> rawData) {
		Map<OrderKey, String> dataMap = new HashMap<OrderKey, String>();
		
		OrderKey key;
		String value;
		for(Map.Entry<String, Object> entry: rawData.entrySet()) {
			key = keyMap.get(entry.getKey());
			
			// skip this entry if there is no mapping
			if(key == null) {
				continue;
			}
			
			value = (String)entry.getValue();
			if(value != null && value.length() > 0) {
				dataMap.put(key, value.trim());
			}
		}
		
		return dataMap;
	}
	
	public LineItem createNonIntegrationShopOrder(String orderNumber, Long tenantId) {
		final String NON_INTEGRATION_DESCRIPTION = "";
		
		if(orderNumber == null || orderNumber.length() == 0) {
			return null;
		}
		
		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);
		
		// for non integration customers, orders, lineitems and products are all 1 to 1
		Order order = new Order(OrderType.SHOP, orderNumber);
		order.setTenant(tenant);
		order.setDescription(NON_INTEGRATION_DESCRIPTION);
		persistenceManager.save(order);
		
		LineItem lineItem = new LineItem(order);
		lineItem.setTenant(tenant);
		lineItem.setIndex(0);
		lineItem.setDescription(NON_INTEGRATION_DESCRIPTION);
		persistenceManager.save(lineItem);
		
		return lineItem;
	}

	public Order processOrderFromPlugin(OrderResolver resolver, String orderNumber, OrderType type, Long tenantId) throws OrderProcessingException {
		Order order = null;
		
		if(type.equals(OrderType.SHOP)) {
			order = processShopOrderFromPlugin(resolver, orderNumber, tenantId);
		} else {
			order = processCustomerOrderFromPlugin(resolver, orderNumber, tenantId);
		}
		
		return order;
	}
	
	/**
	 * 
	 * @param resolver
	 * @param orderNumber
	 * @param tenantId
	 * @return
	 * @throws OrderProcessingException
	 */
	private Order processShopOrderFromPlugin(OrderResolver resolver, String orderNumber, Long tenantId) throws OrderProcessingException {
		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);
		
		ShopOrderTransfer orderTransfer;
		try {
			orderTransfer = resolver.findShopOrder(orderNumber, tenant.getName());
		} catch (PluginException e) {
			throw new OrderProcessingException("Plugin failed finding ShopOrder", e);
		}
		
		if(orderTransfer == null) {
			return null;
		}
		
		Order shopOrder = processAbstractPluginOrder(orderTransfer, OrderType.SHOP, tenant);
		
		// LineItems need a ProductCode
		if(orderTransfer.getProductCode() == null || orderTransfer.getProductCode().trim().length() == 0) {
			throw new OrderProcessingException("Plugin returned blank ProductCode");
		}
		
		LineItem lineItem = findLineItem(shopOrder, orderTransfer.getLineItemId());
		
		// create the lineItem if we couldn't find one
		if (lineItem == null) {
			lineItem = new LineItem(shopOrder);
			
			// if this line item is new, then we'll need to set an index for it
			lineItem.setIndex(countLineItems(shopOrder));
		}
		
		lineItem.setAssetCode(orderTransfer.getProductCode());
		lineItem.setLineId(orderTransfer.getLineItemId());
		lineItem.setQuantity(orderTransfer.getOrderQuantity());
		lineItem.setDescription(orderTransfer.getLineItemDescription());
		
		// save or update the line item
		if(lineItem.isNew()) {
			persistenceManager.save(lineItem);
		} else {
			lineItem = persistenceManager.update(lineItem);
		}
		
		return shopOrder;
	}
	
	/**
	 * 
	 * @param resolver
	 * @param orderNumber
	 * @param tenantId
	 * @return
	 * @throws OrderProcessingException
	 */
	private Order processCustomerOrderFromPlugin(OrderResolver resolver, String orderNumber, Long tenantId) throws OrderProcessingException {
		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);
		
		CustomerOrderTransfer orderTransfer;
		try {
			orderTransfer = resolver.findCustomerOrder(orderNumber, tenant.getName());
		} catch (PluginException e) {
			throw new OrderProcessingException("Plugin failed finding ShopOrder", e);
		}
		
		if(orderTransfer == null) {
			return null;
		}
		
		return processAbstractPluginOrder(orderTransfer, OrderType.CUSTOMER, tenant);
	}
	
	private Order processAbstractPluginOrder(OrderTransfer orderTransfer, OrderType type, Tenant tenant) throws OrderProcessingException {
		// must have an order number
		if(orderTransfer.getOrderNumber() == null || orderTransfer.getOrderNumber().trim().length() == 0) {
			throw new OrderProcessingException("Plugin returned Order with blank OrderNumber");
		}
		
		CustomerOrg customer = null;
		DivisionOrg division = null;
		
		if(orderTransfer.getCustomerName() != null || orderTransfer.getCustomerId() != null) {
			customer = processCustomer(orderTransfer.getCustomerName(), orderTransfer.getCustomerId(), tenant);
			
			if(orderTransfer.getDivisionName() != null) {
				division = processDivision(orderTransfer.getDivisionName(), customer);
			}
		}
		
		// try and look up the order first
		Order order = findOrder(type, orderTransfer.getOrderNumber(), tenant.getId(), null);
		
		if(order == null) {
			// if we didn't find one, we'll need to create it now
			order = new Order(type);
			order.setTenant(tenant);
		}
		
		// set or update the order's data
		order.setOrderNumber(orderTransfer.getOrderNumber());
		order.setOwner((division != null) ? division : customer);
		order.setPoNumber(orderTransfer.getPoNumber());
		order.setOrderDate(orderTransfer.getOrderDate());
		order.setDescription(orderTransfer.getOrderDescription());
		
		// save or update the order
		if(order.isNew()) {
			persistenceManager.save(order);
		} else {
			order = persistenceManager.update(order);
		}
		
		return order;
	}
	
    @SuppressWarnings("unchecked")
    private void debugOrders(Map<String, Object> unmappedOrders) {
		StringBuilder strbuild = new StringBuilder();
		strbuild.append("\n********************* Order Debug Start *************************");
		
		strbuild.append("\nOrder Container:");
		for (Map.Entry<String, Object> masterEntry: unmappedOrders.entrySet()) {
			if (masterEntry.getKey().equals(ORDERS)) {
				// we'll get these after
				continue;
			}
			
			strbuild.append("\n" + masterEntry.getKey() + " = " + masterEntry.getValue().toString());
		}
		

		strbuild.append("\n\nOrders:");
		
		Collection<Map<String, Object>> orders = (Collection<Map<String, Object>>)unmappedOrders.get(ORDERS);
		for (Map<String, Object> order: orders) {
			strbuild.append("\n\t-------------------------- Order --------------------------------");
			
			for (Map.Entry<String, Object> orderEntry: order.entrySet()) {
				if (orderEntry.getKey().equals(LINE_ITEMS)) {
					// we'll get these after
					continue;
				}
				strbuild.append("\n\t" + orderEntry.getKey() + " = " + orderEntry.getValue().toString());
			}
			
			strbuild.append("\n\n\tLineItems:");
			
			Collection<Map<String,Object>> lineItems = (Collection<Map<String,Object>>)order.get(LINE_ITEMS);
			for (Map<String, Object> lineItem: lineItems) {
				strbuild.append("\n\t\t- - - - - - - - - - - Line Item - - - - - - - - - - - - - - - - -");
				
				for (Map.Entry<String, Object> lineItemEntry: lineItem.entrySet()) {
					strbuild.append("\n\t\t" + lineItemEntry.getKey() + " = " + lineItemEntry.getValue().toString());
				}
				
				strbuild.append("\n\t\t- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			}
			
			strbuild.append("\n\t-----------------------------------------------------------------");
		}
		strbuild.append("\n********************* Order Debug Stop **************************\n");
		
		logger.trace(strbuild.toString());
	}
    
    protected LegacyFindOrCreateCustomerOrgHandler getFindOrCreateCustomerHandler() {
		return new LegacyFindOrCreateCustomerOrgHandler(persistenceManager);
	}
    
    protected LegacyFindOrCreateDivisionOrgHandler getFindOrCreateDivisionHandler() {
		return new LegacyFindOrCreateDivisionOrgHandler(persistenceManager);
	}
}
