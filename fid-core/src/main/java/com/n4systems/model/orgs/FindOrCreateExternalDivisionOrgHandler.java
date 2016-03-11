package com.n4systems.model.orgs;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class FindOrCreateExternalDivisionOrgHandler {


    private boolean orgCreated;
    private ListLoader<DivisionOrg> loader;
    private CustomerOrg parent;
    private Saver<? super DivisionOrg> saver;


    public FindOrCreateExternalDivisionOrgHandler(ListLoader<DivisionOrg> loader, Saver<? super DivisionOrg> saver) {
        this.loader = loader;
        this.saver = saver;
    }

    abstract protected DivisionOrg createOrg(CustomerOrg parent, String name, String code);


    public DivisionOrg find(CustomerOrg parent, String name) {
        return find(parent, name, createADefaultExternalOrgCode(name));
    }

    public DivisionOrg find(CustomerOrg parent, String name, String code) {
        cleanState(parent);

        List<DivisionOrg> orgs = loadOrgList();

        return getFuzzyMatcher().find(code, name, orgs);
    }

    private void cleanState(CustomerOrg parent) {
        this.parent = parent;
        orgCreated = false;
    }


    public DivisionOrg findOrCreate(CustomerOrg parent, String name) {
        return findOrCreate(parent, name, createADefaultExternalOrgCode(name));
    }


    public DivisionOrg findOrCreate(CustomerOrg parent, String name, String code) {
        DivisionOrg matchedExternalOrg = find(parent, name, code);

        if (matchedExternalOrg == null) {
            matchedExternalOrg = createAndPersistOrg(parent, name, findUniqueExternalOrgCode(code, loadOrgList()));
        }

        return matchedExternalOrg;
    }

    private String createADefaultExternalOrgCode(String name) {
        return FuzzyResolver.mungString(name);
    }



    private String findUniqueExternalOrgCode(String preferedId, List<DivisionOrg> orgs) {
        List<String> idList = new ArrayList<String>();
        for(DivisionOrg org: orgs) {
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


    protected List<DivisionOrg> loadOrgList() {
        List<DivisionOrg> orgs = loader.load();
        return orgs;
    }


    private DivisionOrg createAndPersistOrg(CustomerOrg parent, String name, String code) {
        validateNameAndCode(name, code);

        DivisionOrg org = createOrg(parent, name, code);

        saveOrg(org);

        orgCreated = true;
        return org;
    }

    private void validateNameAndCode(String name, String code) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(code)) {
            throw new InvalidExternalOrgException(String.format("Name/Code cannot be empty. Name [%s], Code [%s]", name, code));
        }
    }

    protected void saveOrg(DivisionOrg division) {
        saver.save(division);

    }


    public boolean orgWasCreated() {
        return orgCreated;
    }

    protected CustomerOrg getParent() {
        return parent;
    }

    protected ExternalOrgFuzzyFinder<DivisionOrg> getFuzzyMatcher() {
        return new ExternalOrgFuzzyFinder<DivisionOrg>();
    }

}
