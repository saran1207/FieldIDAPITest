package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;

public class AutoCompleteSearch extends AutoComplete<Asset> {
    
    private @SpringBean AssetService assetService;
    
    private HashSet<AssetType> categories;
    
    public AutoCompleteSearch(String id, final IModel<Asset> model) {
        super(id, model);
    }

    protected List<Asset> getChoices() {
        return getChoices(term);
    }

    public List<Asset> getChoices(String term) {
        return assetService.getAssetsLike(term);
    }

    @Override
    protected String getWatermarkText() {
        return "";
    }

    @Override
    protected void startRequest(Request request) {
        categories = new HashSet<AssetType>();
    }

    protected String getCategory(Asset asset) {
        AssetType category = asset.getType();
        if (!categories.contains(category)) {
            categories.add(category);
            return category.getDisplayName();
        }
        return "";
    }

    protected AutoCompleteResult createAutocompleteJson(Asset asset, String term) {
        term = normalizeSearchTerm(term);
        boolean thisOneSelected = asset.equals(getModelObject());
        final String idValue = asset.getId() + "";
        if (thisOneSelected) {
            getAutocompleteHidden().setModelObject(idValue);
        }
        String tooltip = asset.getType().getDisplayName();
        List<String> ids = Lists.newArrayList();
        if (StringUtils.isNotBlank(asset.getCustomerRefNumber())) {
            ids.add(asset.getCustomerRefNumber());
        }
        if (StringUtils.isNotBlank(asset.getRfidNumber())) {
            ids.add(asset.getRfidNumber());
        }
        if (ids.size()>0) {
            tooltip += " " + ids.toString();
        }
        return new AutoCompleteResult(asset.getId()+"", asset.getDisplayName(), getCategory(asset), term, tooltip);
    }

    @Override
    protected String normalizeSearchTerm(String term) {
        int index = term.indexOf(":");
        if (index!=-1 && index+1<term.length()) {
            return term.substring(index+1);
        }
        return term;
    }

    protected String getDisplayValue(Asset asset) {
        return asset.getIdentifier();
    }


}

