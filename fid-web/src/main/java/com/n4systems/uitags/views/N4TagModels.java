package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class N4TagModels {
	
    private ValueStack stack;
    private HttpServletRequest req;
    private HttpServletResponse res;
    
    private PercentBarModel percentBar;
    private IncludeScriptModel includeScript;
    private IncludeStyleModel includeStyle;
    private OrgPickerModel orgPicker;
    private SafetyNetworkSmartSearchModel safetyNetworkSmartSearch;
    private HierarchicalListModel dynamicLocation;
	private LocationModel location;
    
	public N4TagModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }
    
    public PercentBarModel getPercentbar() {
    	 if (percentBar == null) {
             percentBar = new PercentBarModel(stack, req, res);
         }

         return percentBar;
    }
    
    public IncludeScriptModel getIncludeScript() {
    	if (includeScript == null) {
            includeScript = new IncludeScriptModel(stack, req, res);
        }

        return includeScript;
    }
    
    public IncludeStyleModel getIncludeStyle() {
    	if (includeStyle == null) {
    		includeStyle = new IncludeStyleModel(stack, req, res);
    	}
    	return includeStyle;
    }
    
    public OrgPickerModel getOrgPicker() {
    	if (orgPicker == null) {
    		orgPicker = new OrgPickerModel(stack, req, res);
    	}
    	return orgPicker;
    }

    public SafetyNetworkSmartSearchModel getSafetyNetworkSmartSearch() {
    	if (safetyNetworkSmartSearch == null) {
    		safetyNetworkSmartSearch = new SafetyNetworkSmartSearchModel(stack, req, res);
    	}
    	return safetyNetworkSmartSearch;
    }
    
    public HierarchicalListModel getHierarchicalList() {
		if (dynamicLocation==null){
			dynamicLocation = new HierarchicalListModel(stack, req, res);
		}
    	return dynamicLocation;
	}
    
    
    public LocationModel getLocation() {
    	if (location == null) {
    		location = new LocationModel(stack, req, res);
    		
    	}
    	return location;
    }
}
