package com.n4systems.fieldid.wicket.components;

import com.n4systems.util.collections.OrgList;
import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgEnum;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;

public class AutoCompleteOrgPicker extends AutoComplete<BaseOrg> {
    
    private @SpringBean OrgService orgService;
    
    private HashSet<OrgEnum> categories = new HashSet<OrgEnum>();

    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> model) {
        super(id, model);
    }

    protected AutoCompleteResult createAutocompleteJson(BaseOrg org, String term) {
        boolean thisOneSelected = org.equals(getModelObject());
        final String idValue = org.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        return new AutoCompleteResult(org.getId()+"", org.getName(), getCategory(org), term, org.getHierarchicalDisplayName());
   }

    @Override
    protected String getWatermarkText() {
        return "type organization name";
    }

    @Override
    protected String normalizeSearchTerm(String term) {
        return new OrgQueryParser(term).getSearchTerm();
    }

    private String getCategory(BaseOrg org) {
        OrgEnum category = OrgEnum.fromClass(org.getClass());
        if (!categories.contains(category)) {
            categories.add(category);
            return category.toString();
        }
        return "";
    }

    @Override
    protected OrgList getChoices() {
        return getChoices(term);
    }

    @Override
    public OrgList getChoices(String term) {
        return orgService.search(term, threshold);
    }

    @Override
    protected List<BaseOrg> getChoicesForEmptyTerm() {
        return orgService.search(threshold);
    }

    @Override
    protected void startRequest(Request request) {
        categories.clear();
    }

    @Override
    protected String getDisplayValue(BaseOrg org) {
        return org.getName();
    }

}

