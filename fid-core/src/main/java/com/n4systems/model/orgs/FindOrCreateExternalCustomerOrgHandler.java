package com.n4systems.model.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;


public abstract class FindOrCreateExternalCustomerOrgHandler {


    private boolean orgCreated;
    private ListLoader<CustomerOrg> loader;
    private PrimaryOrg parent;
    private Saver<? super CustomerOrg> saver;


    public FindOrCreateExternalCustomerOrgHandler(ListLoader<CustomerOrg> loader, Saver<? super CustomerOrg> saver) {
        this.loader = loader;
        this.saver = saver;
    }

    abstract protected CustomerOrg createOrg(PrimaryOrg parent, String name, String code);


    public CustomerOrg find(PrimaryOrg parent, String name) {
        return find(parent, name, createADefaultExternalOrgCode(name));
    }

    public CustomerOrg find(PrimaryOrg parent, String name, String code) {
        cleanState(parent);

        List<CustomerOrg> orgs = loadOrgList();

        return getFuzzyMatcher().find(code, name, orgs);
    }

    private void cleanState(PrimaryOrg parent) {
        this.parent = parent;
        orgCreated = false;
    }


    public CustomerOrg findOrCreate(PrimaryOrg parent, String name) {
        return findOrCreate(parent, name, createADefaultExternalOrgCode(name));
    }


    public CustomerOrg findOrCreate(PrimaryOrg parent, String name, String code) {
        CustomerOrg matchedExternalOrg = find(parent, name, code);

        if (matchedExternalOrg == null) {
            matchedExternalOrg = createAndPersistOrg(parent, name, findUniqueExternalOrgCode(code, loadOrgList()));
        }

        return matchedExternalOrg;
    }

    private String createADefaultExternalOrgCode(String name) {
        return FuzzyResolver.mungString(name);
    }



    private String findUniqueExternalOrgCode(String preferedId, List<CustomerOrg> orgs) {
        List<String> idList = new ArrayList<String>();
        for(CustomerOrg org: orgs) {
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


    protected List<CustomerOrg> loadOrgList() {
        List<CustomerOrg> orgs = loader.load();
        return orgs;
    }


    private CustomerOrg createAndPersistOrg(PrimaryOrg parent, String name, String code) {
        validateNameAndCode(name, code);

        CustomerOrg org = createOrg(parent, name, code);

        saveOrg(org);

        orgCreated = true;
        return org;
    }

    private void validateNameAndCode(String name, String code) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(code)) {
            throw new InvalidExternalOrgException(String.format("Name/Code cannot be empty. Name [%s], Code [%s]", name, code));
        }
    }

    protected void saveOrg(CustomerOrg division) {
        saver.save(division);

    }


    public boolean orgWasCreated() {
        return orgCreated;
    }

    protected PrimaryOrg getParent() {
        return parent;
    }

    protected ExternalOrgFuzzyFinder<CustomerOrg> getFuzzyMatcher() {
        return new ExternalOrgFuzzyFinder<CustomerOrg>();
    }

}