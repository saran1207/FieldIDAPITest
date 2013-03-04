package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoComplete;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoCompleteResult;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgLocationEnum;
import com.n4systems.util.collections.OrgList;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;


public class AutoCompleteOrgPicker extends AutoComplete<BaseOrg> {
    
    private @SpringBean OrgService orgService;
    
    private HashSet<OrgLocationEnum> categories = new HashSet<OrgLocationEnum>();

    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> model) {
        super(id, BaseOrg.class, model);
        withAutoUpdate(false);
    }

    protected AutoCompleteResult createAutocompleteJson(BaseOrg entity, String term) {
        boolean thisOneSelected = entity.equals(getModelObject());
        final String idValue = entity.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        return new AutoCompleteResult(entity.getId()+"", getDisplayValue(entity), getCategory(entity), term, getTooltip(entity));
    }

    private String getTooltip(BaseOrg entity) {
        return entity!=null ? entity.getHierarchicalDisplayName() : "";
    }

    @Override
    protected String getWatermarkText() {
        return "type organization name";
    }

    @Override
    protected String normalizeSearchTerm(String term) {
        return new OrgQueryParser(term).getSearchTerm();
    }

    private String getCategory(BaseOrg entity) {
        OrgLocationEnum category = OrgLocationEnum.fromClass(entity.getClass());
        if (!categories.contains(category)) {
            categories.add(category);
            return new FIDLabelModel(category.getLabel()).getObject().toUpperCase();
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
        return orgService.search("",threshold);
    }

    @Override
    protected void startRequest(Request request) {
        categories.clear();
    }

    @Override
    protected String getDisplayValue(BaseOrg entity) {
        if (entity instanceof BaseOrg) {
            BaseOrg org = (BaseOrg) entity;
            return org.getName();
        }
        return entity.getId()+"";
    }

}

