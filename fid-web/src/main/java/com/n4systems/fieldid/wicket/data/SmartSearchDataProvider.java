package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rrana on 2015-06-22.
 */
public class SmartSearchDataProvider extends FieldIDDataProvider<Asset>{

    @SpringBean
    private AssetService assetService;

    private String searchTerm;

    public SmartSearchDataProvider(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    @Override
    public Iterator<? extends Asset> iterator(int first, int count) {

        List<? extends Asset> assetList = null;

        assetList = assetService.findExactAssetByIdentifiersForNewSmartSearch(searchTerm);
        return assetList.iterator();

    }

    @Override
    public int size() {
        int size = 0;

        size = assetService.findExactAssetSizeByIdentifiersForNewSmartSearch(searchTerm);
        return size;

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
