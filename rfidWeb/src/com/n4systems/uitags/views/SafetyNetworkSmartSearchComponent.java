package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.util.ValueStack;

public class SafetyNetworkSmartSearchComponent extends UIBean {
	public static final String TEMPLATE = "safetyNetworkSmartSearch";
	
	public SafetyNetworkSmartSearchComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected AbstractAction getAction() {
		// find the first instance of AbsatractAction 
		// (probably the first element but there's not guarantee)
		AbstractAction action = null;
		for (Object stackItem: getStack().getRoot()) {
			if (stackItem instanceof AbstractAction) {
				action = (AbstractAction)stackItem;
				break;
			}
		}
		return action;
	}
	
	protected VendorOrgConnectionsListLoader getVendorListLoader() {
		return getAction().getLoaderFactory().createVendorOrgConnectionsListLoader();
	}
	
	protected Long getTenantId() {
		return getAction().getTenantId();
	}
	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}
	
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		addParameter("vendorList", getVendorList());
	}

	public List<ListingPair> getVendorList() {
		VendorOrgConnectionsListLoader loader = getVendorListLoader();
		
		List<ListingPair> vendors = new ArrayList<ListingPair>();
		for (OrgConnection vendorConnection: loader.load()) {
			vendors.add(new ListingPair(vendorConnection.getVendor()));
		}
		
		return vendors;
	}
}
