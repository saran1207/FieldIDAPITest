package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.org.OrgList;
import com.n4systems.fieldid.service.org.OrgQuery;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgEnum;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;

public class AutoCompleteOrgPicker extends AutoComplete<BaseOrg> {
    
    private @SpringBean OrgService orgService;
    
    private HashSet<OrgEnum> categories;

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
    protected String normalizeSearchTerm(String term) {
        return new OrgQuery(term).getSearchTerm();
    }

    private String getCategory(BaseOrg org) {
        OrgEnum category = OrgEnum.fromClass(org.getClass());
        if (!categories.contains(category)) {
            categories.add(category);
            return category.toString();
        }
        return "";
    }

    protected OrgList getChoices() {
        return getChoices(term);
    }

    public OrgList getChoices(String term) {
        return orgService.getAllOrgsLike(term);
     }

    @Override
    protected void clearCategories() {
        categories = new HashSet<OrgEnum>();
    }
}

