package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.n4systems.model.Asset;
import org.apache.struts2.components.UIBean;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.util.ValueStack;

public class SafetyNetworkSmartSearchComponent extends UIBean {
	public static final String TEMPLATE = "safetyNetworkSmartSearch";
	
	private Boolean refreshRegistration = false;
	
	
	public SafetyNetworkSmartSearchComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected AbstractAction getAction() {
		// find the first instance of AbsatractAction 
		// (probably the first element but there's no guarantee)
		AbstractAction action = null;
		for (Object stackItem: getStack().getRoot()) {
			if (stackItem instanceof AbstractAction) {
				action = (AbstractAction)stackItem;
				break;
			}
		}
		return action;
	}
	
	protected LoaderFactory getLoaderFactory() {
		return getAction().getLoaderFactory();
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

		Long linkedProductId  = null;
		try {
			linkedProductId = Long.valueOf((String)getParameters().get("nameValue"));
		} catch(NumberFormatException e) {}
		
		boolean editMode = false;
		if (linkedProductId != null) {
			Asset linkedAsset = loadLinkedProduct(linkedProductId);
			
			if (linkedAsset != null) {
				editMode = true;
				addParameter("linkedProduct_Id", linkedAsset.getId());
				addParameter("linkedProduct_SerialNumber", linkedAsset.getSerialNumber());
				addParameter("linkedProduct_RfidNumber", linkedAsset.getRfidNumber());
				addParameter("linkedProduct_OwnerName", linkedAsset.getOwner().getDisplayName());
				addParameter("linkedProduct_TypeName", linkedAsset.getType().getDisplayName());
				addParameter("linkedProduct_ReferenceNumber", linkedAsset.getCustomerRefNumber());
			}
		}
		
		addParameter("linkedProduct_editMode", editMode);
		addParameter("vendorList", getVendorList());
		
		addParameter("refreshRegistration", refreshRegistration);
		
	}

	public List<ListingPair> getVendorList() {
		VendorOrgConnectionsListLoader loader = getLoaderFactory().createVendorOrgConnectionsListLoader();
		
		List<ListingPair> vendors = new ArrayList<ListingPair>();
		for (OrgConnection vendorConnection: loader.load()) {
			vendors.add(new ListingPair(vendorConnection.getVendor()));
		}
		
		return vendors;
	}
	
	public Asset loadLinkedProduct(Long linkedProductId) {
		return getLoaderFactory().createSafetyNetworkProductLoader().setProductId(linkedProductId).load();
	}

	public Boolean getRefreshRegistration() {
		return refreshRegistration;
	}

	public void setRefreshRegistration(Boolean refreshRegistration) {
		this.refreshRegistration = refreshRegistration;
	}
}
