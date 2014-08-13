package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * This is the data provider for the Asset Status List panel.
 *
 * Created by Jordan Heath on 11/08/14.
 */
public class AssetStatusDataProvider extends FieldIDDataProvider<AssetStatus> {

    @SpringBean
    private AssetStatusService assetStatusService;

    private List<AssetStatus> results;

    private Long size;

    private final Archivable.EntityState state;

    public AssetStatusDataProvider(String order,
                                   SortOrder sortOrder,
                                   Archivable.EntityState state) {

        setSort(order, sortOrder);
        this.state = state;
    }

    @Override
    public Iterator<AssetStatus> iterator(int first,
                                          int count) {

        List<AssetStatus> assetStatusList =
                assetStatusService.getPagedAssetStatusByState(state,
                                                              getSort().getProperty(),
                                                              getSort().isAscending(),
                                                              first,
                                                              count);

        return assetStatusList.iterator();
    }

    @Override
    public int size() {
        return assetStatusService.getAssetStatusCountByState(state).intValue();
    }

    @Override
    public IModel<AssetStatus> model(final AssetStatus assetStatus) {
        return new AbstractReadOnlyModel<AssetStatus>() {
            @Override
            public AssetStatus getObject() {
                return assetStatus;
            }
        };
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }

}
