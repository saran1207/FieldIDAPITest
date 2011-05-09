package com.n4systems.fieldid.viewhelpers.navigation;

import java.util.Map;

public class NavOption {
	public static final String TYPE_ADD = "add";
	public static final String TYPE_ADDLEFT = "addLeft";
	public static final String TYPE_ENTITY = "entity";
	public static final String TYPE_LIST = "list";
	public static final String TYPE_ENTITYRIGHT = "entityRight";
	
	private String label;
	private String name;
	private String action;
	private String permissionRequired;
	private String extendedFeatureRequired;
	private String type;
	private String conditionalView;
	private Integer order;
	private boolean useEntityTitle;
	private Map<String,String> urlParams;
    private boolean wicket;
		
	public NavOption(String label, String name, String action, int order, String permissionRequired, String extendedFeatureRequired, String type, Map<String, String> urlParams, String conditionalView, boolean useEntityTitle, boolean wicket) {
		this.label = label;
		this.name = name;
		this.order = order;
		this.action = action;
		this.permissionRequired = permissionRequired;
		this.extendedFeatureRequired = extendedFeatureRequired;
		this.type = type;
		this.conditionalView = conditionalView;
		this.urlParams = urlParams;
		this.useEntityTitle = useEntityTitle;
        this.wicket = wicket;
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

	public boolean isUseEntityTitle() {
		return useEntityTitle;
	}
	
	public boolean isEntity() {
		return type.equals(TYPE_ENTITY) || type.equals(TYPE_ENTITYRIGHT);
	}
	
	public boolean isRightJustified() {
		return type.equals(TYPE_ADD) || type.equals(TYPE_ENTITYRIGHT);
	}

    public boolean isWicket() {
        return wicket;
    }

    public void setWicket(boolean wicket) {
        this.wicket = wicket;
    }
}
