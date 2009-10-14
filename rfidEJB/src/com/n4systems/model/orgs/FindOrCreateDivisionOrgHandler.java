package com.n4systems.model.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.persistence.loaders.DuplicateExtenalOrgException;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.FuzzyResolver;

public class FindOrCreateDivisionOrgHandler {
	private ListLoader<DivisionOrg> loader;
	private Saver<? super DivisionOrg> saver;
	private CustomerOrg customer;
	private boolean findOnly = false;
	private boolean divisionCreated;
	
	public FindOrCreateDivisionOrgHandler(ListLoader<DivisionOrg> loader, Saver<? super DivisionOrg> saver) {
		this.loader = loader;
		this.saver = saver;
	}
	
	public DivisionOrg findOrCreate(CustomerOrg customer, String name) throws DuplicateExtenalOrgException {
		return findOrCreate(customer, name, FuzzyResolver.mungString(name));
	}
	
	public DivisionOrg findOrCreate(CustomerOrg customer, String name, String code) throws DuplicateExtenalOrgException {
		this.customer = customer;
		divisionCreated = false;
		
		List<DivisionOrg> divisions = loadDivisionList();
		
		/*
		 * we get the customer in 3 steps.  First we attempt an exact name match on the 
		 * division code.  Failing that, we attempt to resolve against the full division name.
		 * failing that, we'll create one
		 */
		List<DivisionOrg> matchedDivisions = new ArrayList<DivisionOrg>();
		for(DivisionOrg div: divisions) {
			if(div.getCode() != null && div.getCode().equalsIgnoreCase(code)) {
				matchedDivisions.add(div);
			}
		}
		
		// If we find more than one by code, we act like we didn't find it 
		if (matchedDivisions.size() > 1) {
			matchedDivisions = new ArrayList<DivisionOrg>();
		}
		
		//if we didn't find a division, then lets try the full name
		if(matchedDivisions.isEmpty()) {
			String fuzzyName = FuzzyResolver.mungString(name);
			for(DivisionOrg div: divisions) {
				if(FuzzyResolver.mungString(div.getName()).equals(fuzzyName)) {
					matchedDivisions.add(div);
				}
			}
		}
		
		if (matchedDivisions.size() > 1) {
			// if we found more then one customer here, we need to throw an exception
			throw new DuplicateExtenalOrgException(matchedDivisions);
		} else if (matchedDivisions.isEmpty() && !findOnly) {
			// if we still haven't found a customer, we need to create one but first we need to make sure our customer code is unique
			matchedDivisions.add(createDivision(customer, name, findUniqueDivisionCode(code, divisions)));
		}
		
		// return the first customer in the list if it's not empty (by this time there should only be 0 or 1 in the list)
		return (!matchedDivisions.isEmpty()) ? matchedDivisions.get(0) : null;
	}

	private DivisionOrg createDivision(CustomerOrg customer, String name, String code) { 
		DivisionOrg division = new DivisionOrg();
		division.setTenant(customer.getTenant());
		division.setParent(customer);
		division.setCode(code);
		division.setName(name);
		
		persistDivision(division);
		divisionCreated = true;
		
		return division;
	}

	private String findUniqueDivisionCode(String preferedId, List<DivisionOrg> divisions) {
		List<String> idList = new ArrayList<String>();
		for(DivisionOrg division: divisions) {
			idList.add(division.getCode());
		}
		
		String newId = preferedId;
		
		int idx = 1;
		while(idList.contains(newId)) {
			newId = preferedId + idx;
			idx++;
		}
		
		return newId;
	}
	
	// XXX - This is really only required by the LegacyFindOrCreateDivisionOrg and should probably be removed when it is
	protected CustomerOrg getCustomer() {
		return customer;
	}
	
	protected List<DivisionOrg> loadDivisionList() {
		List<DivisionOrg> divisions = loader.load();
		return divisions;
	}
	
	protected void persistDivision(DivisionOrg division) {
		saver.save(division);
	}
	
	public void setFindOnly(boolean findOnly) {
		this.findOnly = findOnly;
	}
	
	public boolean divisionWasCreated() {
		return divisionCreated;
	}
}
