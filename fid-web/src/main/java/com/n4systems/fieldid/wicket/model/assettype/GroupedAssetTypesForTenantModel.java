package com.n4systems.fieldid.wicket.model.assettype;

import com.n4systems.fieldid.viewhelpers.NaturalOrderSort;
import com.n4systems.fieldid.wicket.model.AssetTypesForTenantModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupedAssetTypesForTenantModel extends LoadableDetachableModel<List<AssetType>> {

    private IModel<AssetTypeGroup> assetTypeGroupModel;

    public GroupedAssetTypesForTenantModel(IModel<AssetTypeGroup> assetTypeGroupModel) {
        this.assetTypeGroupModel = assetTypeGroupModel;
    }

    @Override
    protected List<AssetType> load() {
        AssetTypesForTenantModel assetTypesForTenantModel = new AssetTypesForTenantModel(assetTypeGroupModel);
        List<AssetType> assetTypes = assetTypesForTenantModel.getObject();
        groupAssetTypes(assetTypes);
        return assetTypes;
    }

    protected void groupAssetTypes(List<AssetType> assetTypes) {
        final Comparator<String> naturalComparator = NaturalOrderSort.getNaturalComparator();
        Collections.sort(assetTypes, new Comparator<AssetType>() {
            @Override
            public int compare(AssetType type1, AssetType type2) {
                Long group1Index = type1.getGroup() == null ? Long.MAX_VALUE : type1.getGroup().getOrderIdx();
                Long group2Index = type2.getGroup() == null ? Long.MAX_VALUE : type2.getGroup().getOrderIdx();
                if (!group1Index.equals(group2Index)) {
                    return group1Index.compareTo(group2Index);
                } else {
                    return naturalComparator.compare(type1.getDisplayName(), type2.getDisplayName());
                }
            }
        });
    }

}
