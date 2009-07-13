package com.n4systems.fieldid.viewhelpers.navigation;

import java.util.Map;

public class NavOption {

	private String label;
	private String name;
	private String action;
	private String permissionRequired;
	private String extendedFeatureRequired;
	private String type;
	private String conditionalView;
	private Integer order;
	private Map<String,String> urlParams;
	
	
	public NavOption(String label, String name, String action, int order, String permissionRequired, String extendedFeatureRequired, String type, Map<String, String> urlParams, String conditionalView) {
		super();
		this.label = label;
		this.name = name;
		this.order = order;
		this.action = action;
		this.permissionRequired = permissionRequired;
		this.extendedFeatureRequired = extendedFeatureRequired;
		this.type = type;
		this.conditionalView = conditionalView;
		this.urlParams = urlParams;
	}

	
	
	
	public String getLabel() {
		return label;
	}

	

	public String getName() {
		return name;
	}


	public String getAction() {
		return action;
	}


	public String getPermissionRequired() {
		return permissionRequired;
	}


	public String getExtendedFeatureRequired() {
		return extendedFeatureRequired;
	}


	public String getType() {
		return type;
	}
	
	public Map<String,String> getUrlParams() {
		
		return urlParams;
	}

	public Integer getOrder() {
		return order;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NavOption) {
			return name.equals(((NavOption) obj).getName());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}


	public String getConditionalView() {
		return (conditionalView != null) ? conditionalView : "true";
	}
}
