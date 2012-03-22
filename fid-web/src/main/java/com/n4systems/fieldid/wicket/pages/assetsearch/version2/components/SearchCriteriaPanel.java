package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.AssetType;
import com.n4systems.model.search.AssetSearchCriteria;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.List;

public class SearchCriteriaPanel extends AbstractCriteriaPanel<AssetSearchCriteria> {
	
	public SearchCriteriaPanel(String id, final Model<AssetSearchCriteria> model) {
        super(id, model);
	}

    @Override
    protected Panel createFiltersPanel(String filters, Model<AssetSearchCriteria> model) {
        return new SearchFilterPanel("filters",model) {
            @Override
            protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
                getSearchColumnsPanel().updateAssetTypeOrGroup(target, selectedAssetType, availableAssetTypes);
                // note : we know that this event can only occur when Filters panel is displayed. that's where the assetType widget lives.
                getSearchColumnsPanel().add(new AttributeModifier("style", "display:none;"));
            }
        };
    }

    @Override
    protected AbstractColumnsPanel createColumnsPanel(String id, Model<AssetSearchCriteria> model) {
        return new SearchColumnsPanel(id,model);
    }

    public SearchColumnsPanel getSearchColumnsPanel() {
        return (SearchColumnsPanel) columns;
    }

}
