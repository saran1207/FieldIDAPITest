package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetSearchDataProvider extends FieldIdAPIDataProvider {

    private @SpringBean AssetSearchService assetSearchService;
    private AssetSearchCriteria searchCriteria;

    public AssetSearchDataProvider(AssetSearchCriteria searchCriteria) {
        super(searchCriteria, "identified", SortDirection.DESC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        return assetSearchService.countPages(searchCriteria, 1L);
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return assetSearchService.performSearch(searchCriteria, createResultTransformer(), page, pageSize);
    }

    @Override
    public List<Long> getIdList() {
        return assetSearchService.idSearch(searchCriteria);
    }
}
