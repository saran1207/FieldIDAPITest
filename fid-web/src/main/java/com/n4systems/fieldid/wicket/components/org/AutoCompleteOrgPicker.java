package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoComplete;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoCompleteResult;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgLocationEnum;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.collections.OrgList;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;


/**
 * note that the Org hierarchy is now linked, as far as the user is concerned, to the PredefinedLocations hierarchy.
 *  in other words, the user only sees one global hierarchy even though it represents different entities.
 *  in order to accomplish this, the compoent cannot be genericized to BaseOrg, but rather it must use EntityWithTenant
 *  as that's the only thing they have in common.   (it might be possible to create an interface for both of them??).
 */
public class AutoCompleteOrgPicker extends AutoComplete<EntityWithTenant> {
    
    private @SpringBean OrgService orgService;
    
    private HashSet<OrgLocationEnum> categories = new HashSet<OrgLocationEnum>();
    private boolean includeLocations = false;


    @Deprecated //transitioning over to dual model version.
    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> model) {
        this(id, model,null);
    }

    public AutoCompleteOrgPicker(String id, final IModel<BaseOrg> orgModel, final IModel<PredefinedLocation> locationModel) {
        super(id, new OrgLocationModel(orgModel,locationModel));
        includeLocations = locationModel!=null;
        withAutoUpdate(false);
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

    private String getCategory(EntityWithTenant entity) {
        OrgLocationEnum category = OrgLocationEnum.fromClass(entity.getClass());
        if (!categories.contains(category)) {
            categories.add(category);
            return new FIDLabelModel(category.getLabel()).getObject().toUpperCase();
        }
        return "";
    }

    public AutoCompleteOrgPicker withLocation() {
        this.includeLocations = true;
        return this;
    }

    public AutoCompleteOrgPicker withoutLocation() {
        this.includeLocations = false;
        return this;
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

