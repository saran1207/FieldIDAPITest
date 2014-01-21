package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.columns.AssetTextOrFilterSearchService;
import com.n4systems.fieldid.wicket.model.LocalizeAround;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.concurrent.Callable;

public class AssetSearchDataProvider extends FieldIdAPIDataProvider<Asset> {

    private @SpringBean AssetTextOrFilterSearchService assetTextOrFilterSearchService;
    private AssetSearchCriteria searchCriteria;

    public AssetSearchDataProvider(AssetSearchCriteria searchCriteria) {
        super(searchCriteria, "identified", SortDirection.DESC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        return assetTextOrFilterSearchService.countPages(searchCriteria, 1L);
    }

    @Override
    protected PageHolder<TableView> runSearch(final int page, final int pageSize) {
        return new LocalizeAround<PageHolder<TableView>>(new Callable<PageHolder<TableView>>() {
            @Override
            public PageHolder<TableView> call() throws Exception {
                return assetTextOrFilterSearchService.performSearch(searchCriteria, createResultTransformer(), page, pageSize);
            }
        }).call();
    }

    @Override
    public List<Long> getIdList() {
        return assetTextOrFilterSearchService.idSearch(searchCriteria);
    }
}
