package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.api.Listable;

public enum OrderKey implements Listable<String> {
//                    lineMapping, required,     displayname	
	ORDER_NUMBER		( false,	true,	"Order Number"				),
	ORDER_DATE			( false,	false,	"Order Date"				),
	ORDER_CUSTOMER_ID	( false,	false,	"Order Customer ID"			),
	ORDER_CUSTOMER_NAME	( false,	false,	"Order Customer Name"		),
	ORDER_DIVISION_NAME	( false,	false,	"Order Division Name"		),
	ORDER_DESCRIPTION	( false,	false,	"Order Description"			),
	ORDER_PO_NUMBER		( false,	false,	"Order PO Number"			),
	LINE_PRODUCT_CODE	( true,		true,	"Line Item Asset Code"	),
	LINE_INDEX			( true,		false,	"Line Item Order Index"		),
	LINE_QUANTITY		( true,		false,	"Line Item Quantity"		),
	LINE_ID				( true,		false,	"Line Item ID"				),
	LINE_DESCRIPTION	( true,		false,	"Line Item Description"		);
	
	private boolean lineMapping;
	private boolean required;
	private String displayName;
	
	OrderKey(boolean lineMapping, boolean required, String displayName) {
		this.lineMapping = lineMapping;
		this.required = required;
		this.displayName = displayName;
	}
	
	public boolean isLineMapping() {
		return lineMapping;
	}
	
	public boolean isRequired() {
		return required;
	}	
	
	public List<OrderKey> getOrderMappings() {
		return getOrderMappings(false);
	}
	
	public List<OrderKey> getOrderMappings(boolean requiredOnly) {
		List<OrderKey> mappings = new ArrayList<OrderKey>();
		for(OrderKey key: OrderKey.values()) {
			if(!key.isLineMapping()) {
				if(!requiredOnly || (requiredOnly && key.isRequired())) {
					mappings.add(key);
				}
			}
		}
		return mappings;
	}
	
	public List<OrderKey> getLineMappings() {
		return getLineMappings(false);
	}
	
	public List<OrderKey> getLineMappings(boolean requiredOnly) {
		List<OrderKey> mappings = new ArrayList<OrderKey>();
		for(OrderKey key: OrderKey.values()) {
			if(key.isLineMapping()) {
				if(!requiredOnly || (requiredOnly && key.isRequired())) {
					mappings.add(key);
				}
			}
		}
		return mappings;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return name();
	}

}
