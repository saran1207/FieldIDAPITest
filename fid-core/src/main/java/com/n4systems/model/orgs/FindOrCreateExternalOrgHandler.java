package com.n4systems.model.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;


public abstract class FindOrCreateExternalOrgHandler<T extends ExternalOrg, P extends BaseOrg> {


	private boolean orgCreated;
	private ListLoader<T> loader;
	private P parent;
	private Saver<? super T> saver;
	

	public FindOrCreateExternalOrgHandler(ListLoader<T> loader, Saver<? super T> saver) {
		this.loader = loader;
		this.saver = saver;
	}

	abstract protected T createOrg(P parent, String name, String code);
	
	
	public T find(P parent, String name) {
		return find(parent, name, createADefaultExternalOrgCode(name));
	}
	
	public T find(P parent, String name, String code) {
		cleanState(parent);
		
		List<T> orgs = loadOrgList();
		
		return getFuzzyMatcher().find(code, name, orgs);
	}
	
	private void cleanState(P parent) {
		this.parent = parent;
		orgCreated = false;
	}
	
	
	public T findOrCreate(P parent, String name) {
		return findOrCreate(parent, name, createADefaultExternalOrgCode(name));
	}


	public T findOrCreate(P parent, String name, String code) {
		T matchedExternalOrg = find(parent, name, code);
		
		if (matchedExternalOrg == null) {
			matchedExternalOrg = createAndPersistOrg(parent, name, findUniqueExternalOrgCode(code, loadOrgList()));
		}
		
		return matchedExternalOrg;
	}

	private String createADefaultExternalOrgCode(String name) {
		return FuzzyResolver.mungString(name);
	}

	
	
	private String findUniqueExternalOrgCode(String preferedId, List<T> orgs) {
		List<String> idList = new ArrayList<String>();
		for(T org: orgs) {
			idList.add(org.getCode());
		}
		
		String newId = preferedId;
		
		int idx = 1;
		while(idList.contains(newId)) {
			newId = preferedId + idx;
			idx++;
		}
		
		return newId;
	}

	
	protected List<T> loadOrgList() {
		List<T> orgs = loader.load();
		return orgs;
	}


	private T createAndPersistOrg(P parent, String name, String code) { 
		validateNameAndCode(name, code);
		
		T org = createOrg(parent, name, code);
		
		saveOrg(org);
		
		orgCreated = true;
		return org;
	}
	
	private void validateNameAndCode(String name, String code) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(code)) {
			throw new InvalidExternalOrgException(String.format("Name/Code cannot be empty. Name [%s], Code [%s]", name, code));
		}
	}
	
	protected void saveOrg(T division) {
		saver.save(division);
		
	}


	public boolean orgWasCreated() {
		return orgCreated;
	}
	
	protected P getParent() {
		return parent;
	}
	
	protected ExternalOrgFuzzyFinder<T> getFuzzyMatcher() {
		return new ExternalOrgFuzzyFinder<T>();
	}

}