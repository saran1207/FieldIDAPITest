package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.AssetType;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class AssetTypesWithAutoAttributeCriteria extends FieldIDSpringModel<List<AssetType>> {

    private IModel<List<AssetType>> assetTypesModel;

    public AssetTypesWithAutoAttributeCriteria(IModel<List<AssetType>> assetTypesModel) {
        this.assetTypesModel = assetTypesModel;
    }

    @Override
    protected List<AssetType> load() {
        List<AssetType> assetTypesWithCriteria = new ArrayList<AssetType>();
        for (AssetType assetType : assetTypesModel.getObject()) {
            if (assetType.getAutoAttributeCriteria() != null) {
                assetTypesWithCriteria.add(assetType);
            }
        }
        return assetTypesWithCriteria;
    }

    @Override
    public void detach() {
        super.detach();

        assetTypesModel.detach();
    }
}
