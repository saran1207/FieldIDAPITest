package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class AssetSearchForAssetMergeDataProvider extends FieldIDDataProvider<Asset> {


    @SpringBean
    private AssetService assetService;

    private int pageSize;
    private Long excludeId;
    private String searchValue;
    private Long assetTypeId;


    public AssetSearchForAssetMergeDataProvider(int pageSize) {
        this.pageSize = pageSize;
        excludeId = null;
        searchValue = null;
        assetTypeId = null;
    }

    public void setParams(Long excludeId, String searchValue, AssetType assetType) {
        this.excludeId = excludeId;
        this.searchValue = searchValue;
        assetTypeId = assetType.getId();
    }

    /**
     * Gets total number of items in the collection represented by the DataProvider
     *
     * @return total item count
     */
    @Override
    public int size() {
        return assetService.findExactAssetSizeByIdentifierSmartSearchAndAssetType(
                searchValue, assetTypeId, excludeId);
    }

    /**
     * Gets an iterator for the subset of total data
     *
     * @param first
     *            first row of data
     * @param count
     *            minimum number of elements to retrieve
     *
     * @return iterator capable of iterating over {first, first+count} items
     */
    public Iterator<Asset> iterator(int first, int count) {

        List<Asset> assets = assetService.findExactAssetByIdentifierSmartSearchAndAssetType(
                searchValue, assetTypeId, excludeId, first, pageSize);

        return assets.iterator();
    }

    @Override
    public void detach() {

    }

    @Override
    public IModel<Asset> model(final Asset object) {
        return new AbstractReadOnlyModel<Asset>() {
            @Override
            public Asset getObject() {
                return object;
            }
        };
    }

}

