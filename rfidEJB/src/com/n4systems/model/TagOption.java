package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.n4systems.model.Order.OrderType;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "tagoptions")
public class TagOption extends EntityWithTenant implements Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;

	public enum OptionKey implements Listable<String> {
		SHOPORDER		("Order Number",			"ordernumber",			OrderType.SHOP		), 
		CUSTOMERORDER	("Customer Order",			"customerordernumber",	OrderType.CUSTOMER	);
		
		private String defaultText;
		private String legacyKey;
		private OrderType orderType;
		
		OptionKey(String defaultText, String legacyKey, OrderType orderType) {
			this.defaultText = defaultText;
			this.legacyKey = legacyKey;
			this.orderType = orderType;
		}
		
		public String getDefaultText() {
			return defaultText;
		}
		
		@Deprecated
		public String getLegacyKey() {
			return legacyKey;
		}
		
		public OrderType getOrderType() {
			return orderType;
		}
		
		public String getId() {
			return name();
		}
		
		public String getDisplayName() {
			return getDefaultText();
		}
	}
	
	@Column(name="optionkey", nullable=false)
	@Enumerated(EnumType.STRING)
	private OptionKey optionKey;


	@Column(nullable=false)
	private Long weight = 0L;
	
	private String text;
	private String resolverClassName;
	
	public TagOption () {}
	
	@Deprecated
	public OptionKey getKey() {
		return getOptionKey();
	}

	@Deprecated
	public void setKey(OptionKey key) {
		setOptionKey(key);
	}
	
	
	public OptionKey getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(OptionKey optionKey) {
		this.optionKey = optionKey;
	}

	public String getText() {
		return (text != null) ? text : optionKey.getDefaultText();
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
	
	public String getResolverClassName() {
		return resolverClassName;
	}

	public void setResolverClassName(String resolverClassName) {
		this.resolverClassName = resolverClassName;
	}

	public String getDisplayName() {
		return getText();
	}


	
}
