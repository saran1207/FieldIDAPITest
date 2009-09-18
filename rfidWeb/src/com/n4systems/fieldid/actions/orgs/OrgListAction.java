package com.n4systems.fieldid.actions.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.Preparable;

public class OrgListAction extends AbstractAction implements Preparable {

	private DummyOwnerHolder ownerHolder = new DummyOwnerHolder();
	private OwnerPicker ownerPicker;
	private String orgType;

	public OrgListAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), ownerHolder);
		
	}
	
	
	public String doShow() {
		if (getOwner() == null) {
			setOwnerId(getPrimaryOrg().getId());
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
		List<Listable<Long>> orgList = new ArrayList<Listable<Long>>();
		orgList.add(new SimpleListable<Long>(getPrimaryOrg().getId(), getPrimaryOrg().getDisplayName()));
		orgList.addAll(getLoaderFactory().createFilteredListableLoader(SecondaryOrg.class).load());
		
		return ListingPair.convertToListingPairs(orgList);
	}
	
	public List<ListingPair> getCustomers() {
		List<Listable<Long>> customerList = new ArrayList<Listable<Long>>();
		customerList.add(getBlankValue());
		
		customerList.addAll(new BaseOrgParentFilterListLoader(getSecurityFilter()).setClazz(CustomerOrg.class).setParent(getOwner().getInternalOrg()).load());
		return ListingPair.convertToListingPairs(customerList);
	}
	
	
	
	public List<ListingPair> getDivisions() {
		List<Listable<Long>> divisionList = new ArrayList<Listable<Long>>();
		divisionList.add(getBlankValue());
		if (getOwner().getCustomerOrg() != null) {
			divisionList.addAll(new BaseOrgParentFilterListLoader(getSecurityFilter()).setClazz(DivisionOrg.class).setParent(getOwner().getCustomerOrg()).load());
		}
		return ListingPair.convertToListingPairs(divisionList);
	}


	private SimpleListable<Long> getBlankValue() {
		return new SimpleListable<Long>(-1L, "");
	}


	public String getOrgType() {
		return orgType;
	}


	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

}
