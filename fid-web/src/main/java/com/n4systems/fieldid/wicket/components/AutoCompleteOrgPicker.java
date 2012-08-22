package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.location.OrgLocationModel;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.collections.OrgList;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;

public class AutoCompleteOrgPicker extends AutoComplete<EntityWithTenant> {
    
    private @SpringBean OrgService orgService;
    
    private HashSet<String> categories = new HashSet<String>();
    private boolean includeLocations = false;


    @Deprecated //transitioning over to dual model verion.
    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> model) {
        this(id, model,null);
    }

    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> orgModel, final IModel<PredefinedLocation> locationModel) {
        super(id, new OrgLocationModel(orgModel,locationModel));
        includeLocations = locationModel!=null;
        withAutoUpdate(true);
    }

    protected AutoCompleteResult createAutocompleteJson(EntityWithTenant entity, String term) {
        boolean thisOneSelected = entity.equals(getModelObject());
        final String idValue = entity.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        return new AutoCompleteResult(entity.getId()+"", getDisplayValue(entity), getCategory(entity), term, getDisplayValue(entity));
   }

    @Override
    protected String getWatermarkText() {
        return "type organization name";
    }

    @Override
    protected String normalizeSearchTerm(String term) {
        return new OrgQueryParser(term).getSearchTerm();
    }

    private String getCategory(EntityWithTenant org) {
        String category = org.getClass().getSimpleName();
        if (!categories.contains(category)) {
            categories.add(category);
            return category;
        }
        return "";
    }

    @Override
    protected OrgList getChoices() {
        return getChoices(term);
    }

    @Override
    public OrgList getChoices(String term) {
        return orgService.search(term, includeLocations, threshold);
    }

    @Override
    protected List<EntityWithTenant> getChoicesForEmptyTerm() {
        return orgService.search(includeLocations, threshold);
    }

    @Override
    protected void startRequest(Request request) {
        categories.clear();
    }

    @Override
    protected String getDisplayValue(EntityWithTenant entity) {
        if (entity instanceof BaseOrg) {
            BaseOrg org = (BaseOrg) entity;
            return org.getName();
        } else if (entity instanceof PredefinedLocation) {
            PredefinedLocation location = (PredefinedLocation) entity;
            return location.getName();
        }
        return entity.getId()+"";
    }

}

