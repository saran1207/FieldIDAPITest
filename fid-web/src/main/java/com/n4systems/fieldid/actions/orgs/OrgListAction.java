package com.n4systems.fieldid.actions.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.CustomerOrgLister;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.InternalOrgLister;
import com.n4systems.fieldid.actions.utils.OrgType;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.Preparable;

public class OrgListAction extends AbstractAction implements Preparable {

	private DummyOwnerHolder ownerHolder = new DummyOwnerHolder();
	private OwnerPicker ownerPicker;
	private String orgTypeFilter = "all";

	public OrgListAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createEntityByIdLoader(BaseOrg.class), ownerHolder);
		
		ownerPicker.setOwnerId(getPrimaryOrg().getId());
	}
	
	public String doShow() {
		if (getOwner() == null) {
			setOwnerId(getSessionUserOwner().getId());
		}
		return SUCCESS;
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
	}
	
	public List<ListingPair> getOrgs() {
		List<Listable<Long>> orgList = new InternalOrgLister(getOrgType(), getLoaderFactory().createFilteredListableLoader(SecondaryOrg.class), getPrimaryOrg()).getInternalOrgs();
		
		if (getOwner().getSecondaryOrg() != null && !orgList.contains(new SimpleListable<Long>(getOwner().getSecondaryOrg()))) {
			orgList.add(new SimpleListable<Long>(getOwner().getSecondaryOrg()));
		}
		
		return ListHelper.longListableToListingPair(orgList);
	}
	
	public List<ListingPair> getCustomers() {
		List<Listable<Long>> customerList = new CustomerOrgLister(getOrgType(), getLoaderFactory().createBaseParentFilterLoader()).getCustomerList(getOwner());
		
		if (getOwner().getCustomerOrg() != null && !customerList.contains(new SimpleListable<Long>(getOwner().getCustomerOrg()))) {
			customerList.add(new SimpleListable<Long>(getOwner().getCustomerOrg()));
		}
		if (selectFirstCustomer(customerList)) {
			setOwnerId(customerList.get(0).getId());
		}
		
		return ListHelper.longListableToListingPair(customerList);
	}

	private boolean selectFirstCustomer(List<Listable<Long>> customerList) {
		return !isCustomerBlankValueRequired() && getOwner().isInternal() && !customerList.isEmpty();
	}

	private boolean isCustomerBlankValueRequired() {
		if (orgTypeFilter.equalsIgnoreCase("customer")) {
			return false;
		}
		if (orgTypeFilter.equalsIgnoreCase("external")) {
			return false;
		}		
		return true;
	}

	public List<ListingPair> getDivisions() {
		List<Listable<Long>> divisionList = new ArrayList<Listable<Long>>();
		if (isDivisionBlankValueRequired()) {
			divisionList.add(getBlankValue());
		}
			
		if (isDivisionListRequired() && getOwner().getCustomerOrg() != null) {
			divisionList.addAll(divisionListForCustomer(getOwner().getCustomerOrg()));
		}
			
		if (getOwner().getDivisionOrg() != null && !divisionList.contains(getOwner().getDivisionOrg())) {
			divisionList.add(getOwner().getDivisionOrg());
		}
		return ListHelper.longListableToListingPair(divisionList);
	}

	private List<Listable<Long>> divisionListForCustomer(CustomerOrg customer) {
		return getLoaderFactory().createBaseParentFilterLoader().setClazz(DivisionOrg.class).setParent(customer).load();
	}

	private boolean isDivisionListRequired() {
		if (orgTypeFilter.equalsIgnoreCase("internal") 
				|| orgTypeFilter.equalsIgnoreCase("customer") 
				|| orgTypeFilter.equalsIgnoreCase("readonly") 
				|| orgTypeFilter.equalsIgnoreCase("primary") 
				|| orgTypeFilter.equalsIgnoreCase("secondary")) {
			return false;
		}
		return true;
	}

	private boolean isDivisionBlankValueRequired() {
		if (getOwner().isInternal()) {
			return false;
		}
		if (getOwner().isExternal() && !orgTypeFilter.equalsIgnoreCase("customer"))
			return true;
		
		return false;
	}

	private SimpleListable<Long> getBlankValue() {
		return new SimpleListable<Long>(-1L, "");
	}

	public String getOrgTypeFilter() {
		return orgTypeFilter;
	}

	public void setOrgTypeFilter(String orgTypeFilter) {
		if (orgTypeFilter == null || orgTypeFilter.trim().length() == 0) {
			this.orgTypeFilter = "all";
		} else {
			this.orgTypeFilter = orgTypeFilter;
		}
	}
	
	private OrgType getOrgType() {
		if(orgTypeFilter.equalsIgnoreCase("readonly"))
			return OrgType.ALL;
		return OrgType.getByName(this.orgTypeFilter);
	}

}
