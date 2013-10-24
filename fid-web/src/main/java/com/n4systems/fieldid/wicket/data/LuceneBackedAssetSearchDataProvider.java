package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.search.AssetSearchTableAdapterService;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class LuceneBackedAssetSearchDataProvider extends FieldIdAPIDataProvider {

//    private @SpringBean AssetFullTextSearchService fullTextSearchService;
    private @SpringBean AssetSearchTableAdapterService assetSearchTableAdapterService;
    private @SpringBean PersistenceService persistenceService;
    private SearchCriteria searchCriteria;

    public LuceneBackedAssetSearchDataProvider(SearchCriteria searchCriteria) {
        super(searchCriteria, "ignored", SortDirection.ASC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        return assetSearchTableAdapterService.getResultCount(searchCriteria);
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
        return assetSearchTableAdapterService.runSearch(searchCriteria, page, pageSize);
    }

    @Override
    public List<Long> getIdList() {
        return assetSearchTableAdapterService.getIdList(searchCriteria);
    }

}
