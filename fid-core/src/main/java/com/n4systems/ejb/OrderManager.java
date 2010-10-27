package com.n4systems.ejb;

import java.util.List;
import java.util.Map;

import rfid.ejb.entity.OrderMappingBean;

import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.plugins.integration.OrderResolver;

public interface OrderManager {
	
	// META-DATA mappings in the raw order data
	public final static String ORGANIZATION_ID = "organizationID";
	public final static String EXTERNAL_SOURCE_ID ="externalSourceID";
	public final static String ORDER_TYPE = "orderType";
	public final static String ORDERS = "orders";
	public final static String LINE_ITEMS = "lineItems";
	
	/**
	 * Takes a Map containing general information as well as a collection of "orders" (more Maps) 
	 * which also contains another collection of "lineItems" (again, more Maps) and maps them, based on 
	 * the clients mapping configuration, to a collection of OrderMasterBeans.
	 * This also supports updating.  That is, if the line item already exists, it will load that bean and pass it with
	 * the updated information.
	 * @see #processOrder(Tenant, String, Map, Map)
	 * @param unmappedOrders	Map of order data
	 * @return					List of created orders
	 * @throws OrderProcessingException	On any unrecoverable problem.
	 */
	public List<Order> processOrders(Map<String, Object> unmappedOrders) throws OrderProcessingException;

	/**
	 * Processes a single order and its line items from raw data.  Attempts to resolve orders by type and orderNumber before processing.
	 * Resolved orders will be updated, new orders will be saved.  Force loads key mappings.
	 * @see #processOrder(Tenant, String, Map, Map)
	 * @param tenant		A tenant
	 * @param sourceId		The integration source address {@link OrderMappingBean#getExternalSourceID()}
	 * @param rawOrderData	Raw order data
	 * @return				The new or updated order
	 * @throws OrderProcessingException		On any unrecoverable problem
	 */
	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData) throws OrderProcessingException;
	
	/**
	 * Processes a single order and its line items from raw data.  Attempts to resolve orders by type and orderNumber before processing.
	 * Resolved orders will be updated, new orders will be saved.
	 * @see #processLineItem(Tenant, String, Order, Map, Map)
	 * @param tenant		A tenant
	 * @param sourceId		The integration source address {@link OrderMappingBean#getExternalSourceID()}
	 * @param rawOrderData	Raw order data
	 * @param keyMappings	OrderMappings for this Tenant or Source address.  Loads from database if null.
	 * @return				The new or updated order
	 * @throws OrderProcessingException	On any unrecoverable problem
	 */
	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData, Map<String, OrderKey> keyMappings) throws OrderProcessingException;
	
	/**
	 * Processes a single line item.  Attempts to resolve line items by lineId and order before processing.
	 * Resolved line items will be updated, new line items will be saved.  Note that the Order MUST exist
	 * in the database before starting.  Force loads key mappings.
	 * @param order				An existing order
	 * @param sourceId			The integration source address {@link OrderMappingBean#getExternalSourceID()}
	 * @param rawLineItemData	Raw line item data
	 * @return					The new or updated line item
	 * @throws OrderProcessingException	On any unrecoverable problem
	 */
	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData) throws OrderProcessingException;
	
	/**
	 * Processes a single line item.  Attempts to resolve line items by lineId and order before processing.
	 * Resolved line items will be updated, new line items will be saved.  Note that the Order MUST exist
	 * in the database before starting.
	 * @param order				An existing order
	 * @param sourceId			The integration source address {@link OrderMappingBean#getExternalSourceID()}
	 * @param rawLineItemData	Raw line item data
	 * @param keyMappings		OrderMappings for this Tenant or Source address.  Loads from database if null.
	 * @return					The new or updated line item
	 * @throws OrderProcessingException	On any unrecoverable problem
	 */
	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData, Map<String, OrderKey> keyMappings) throws OrderProcessingException;
	
	/**
	 * Finds an Order by OrderType and Order Number for a Tenant.
	 * @param type			Type of order
	 * @param orderNumber	The Order Number
	 * @param tenantId		Id of a Tenant
	 * @param filter		Optional SecurityFilter
	 * @return				An Order or null
	 */
	public Order findOrder(OrderType type, String orderNumber, Long tenantId, SecurityFilter filter);
	
	/**
	 * Attempts to resolve a LineItem on an Order using it's lineId
	 * @param order		An Order
	 * @param lineId	The lineId of the LineItem
	 * @return			A LineItem or null
	 */
	public LineItem findLineItem(Order order, String lineId);
	
	/**
	 * Finds LineItems associated with an Order
	 * @param order	An Order
	 * @return		A List of LineItems
	 */
	public List<LineItem> findLineItems(Order order);
	
	/**
	 * Counts the number of LineItems on an Order
	 * @param order	An Order
	 * @return		The number of line items attached
	 */
	public int countLineItems(Order order);
	
	/**
	 * Counts the number of Assets tagged on a LineItem
	 * @param lineItem	A LineItem
	 * @return			The number of Assets tagged against this lineItem
	 */
	public int countAssetsTagged(LineItem lineItem);
	
	public LineItem createNonIntegrationShopOrder(String orderNumber, Long tenantId);
	
	public Order processOrderFromPlugin(OrderResolver resolver, String orderNumber, OrderType type, Long tenantId) throws OrderProcessingException;
	
}
