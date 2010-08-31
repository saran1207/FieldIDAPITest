package com.n4systems.fieldid.actions.user;

import java.util.ArrayList;
import java.util.Collection;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.api.Listable;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;

public class RegionListAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private String countryId;
	private Collection<? extends Listable<String>> regionList = new ArrayList<Listable<String>>();
	
	public RegionListAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doList() {
		Country country = CountryList.getInstance().getCountryById(countryId);
		
		if (country != null) {
			regionList = country.getRegions();
		}
		
		return SUCCESS;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public Collection<? extends Listable<String>> getRegionList() {
		return regionList;
	}	
}
