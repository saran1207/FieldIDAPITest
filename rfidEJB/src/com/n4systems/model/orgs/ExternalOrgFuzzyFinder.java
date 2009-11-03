package com.n4systems.model.orgs;

import java.util.List;

import com.n4systems.util.FuzzyResolver;


/**
 * The fuzzy matcher will find an external org in the list 
 * 1 .if there is only one that has the same code as the given code.
 * 
 * 2. if more than one external org matches the code or none matches the code. It will then look for the first external org that has
 * a fuzzy name match.  
 * 
 * This means that the order of the list will determine which external org is found if more than one name matches.  
 * 
 * 
 * Fuzzy name matching is implemented by FuzzyResolver.mungString
 * @see FuzzyResolver
 * @author aaitken
 *
 */
public class ExternalOrgFuzzyFinder<T extends ExternalOrg> {

	public T find(String code, String name, List<T> orgs) {
		T matchedOrg = null;
		
		matchedOrg = matchOrgUsingCode(code, orgs);
		
		if (matchedOrg == null) {
			matchedOrg = matchOrgWithFuzzyName(name, orgs);
		}
		
		return matchedOrg;
	}

	private T matchOrgWithFuzzyName(String name, List<T> orgs) {
		String fuzzyName = FuzzyResolver.mungString(name);

		for(T org: orgs) {
			if(FuzzyResolver.mungString(org.getName()).equals(fuzzyName)) {
				return org;
			}
		}
		return null;
	}

	private T matchOrgUsingCode(String code, List<T> orgs) {
		T matchedOrg = null;
		
		for(T org: orgs) {
			if(org.getCode() != null && org.getCode().equalsIgnoreCase(code)) {
				if (matchedOrg != null) {
					return null;
				}
				matchedOrg = org;
			}
		}
		
		return matchedOrg;
	}
}
