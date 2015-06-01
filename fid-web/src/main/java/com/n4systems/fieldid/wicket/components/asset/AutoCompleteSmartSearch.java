package com.n4systems.fieldid.wicket.components.asset;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoComplete;
import com.n4systems.fieldid.wicket.components.autocomplete.AutoCompleteResult;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rrana on 2015-05-26.
 */
public class AutoCompleteSmartSearch extends AutoComplete<Asset> {

    private @SpringBean
    AssetService assetService;

    private HashSet<AssetType> categories = new HashSet<AssetType>();

    public AutoCompleteSmartSearch(String id, final IModel<Asset> model) {
        super(id, model);
    }

    @Override
    public List<Asset> getChoices(String term) {
        return assetService.findAssetByIdentifiersForNewSmartSearch(term);
    }

    @Override
    protected List<Asset> getChoicesForEmptyTerm() {
        //We will not display any results if there is no search term
        return new ArrayList<Asset>();
    }

    @Override
    protected String getWatermarkText() {
        return "Search";
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

        String description = getDescriptionString(asset);

        //return new AutoCompleteResult(asset.getId()+"", "ID:" + asset.getDisplayName() + ", RFID:" + asset.getRfidNumber() + ", REFERENCE:" + asset.getCustomerRefNumber(), getCategory(asset), term, tooltip);
        return new AutoCompleteResult(asset.getId()+"", description, getCategory(asset), term, tooltip);
    }

    protected String getDescriptionString(Asset asset) {

        String Rfid = "";
        String Reference = "";

        if(asset.getRfidNumber() != null) {
            Rfid = asset.getRfidNumber();
        }

        if(asset.getCustomerRefNumber() != null) {
            Reference = asset.getCustomerRefNumber();
        }

        String description = "ID:" + asset.getDisplayName() + ", RFID:" + Rfid + ", REFERENCE:" + Reference;
        return description;
    }

    @Override
    protected String normalizeSearchTerm(String term) {
        int index = term.indexOf(":");
        if (index!=-1 && index+1<term.length()) {
            return term.substring(index+1);
        }
        return term;
    }

    @Override
    protected Asset findEntity(long entityId, String input) {
        return persistenceService.find(Asset.class,entityId);
    }

    protected String getDisplayValue(Asset asset) {
        return asset.getIdentifier();
    }

}

