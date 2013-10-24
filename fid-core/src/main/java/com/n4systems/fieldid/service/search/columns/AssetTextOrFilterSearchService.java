package com.n4systems.fieldid.service.search.columns;

import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.SearchResult;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.SearchResults;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AssetTextOrFilterSearchService extends TextOrFilterSearchService<AssetSearchCriteria, Asset> {

    private @Autowired AssetFullTextSearchService fullTextSearchService;
    private @Autowired AssetSearchService searchService;
    private @Autowired LastEventDateService lastEventDateService;

    public AssetTextOrFilterSearchService() {
        super(Asset.class);
    }

    @Override
    protected List<Long> textIdSearch(AssetSearchCriteria criteria) {
        SearchResults search = fullTextSearchService.search(criteria.getQuery(), new Formatter() {
            @Override
            public String highlightTerm(String s, TokenGroup tokenGroup) {
                return s;
            }
        });
        List<Long> idList = new ArrayList<Long>();
        for (com.n4systems.services.search.SearchResult searchResult : search.getResults()) {
            Long id = Long.valueOf(searchResult.get(AssetIndexField.ID.getField()));
            idList.add(id);
        }
        return idList;
    }

    @Override
    protected List<Long> filterIdSearch(AssetSearchCriteria criteria) {
        return searchService.idSearch(criteria);
    }

    @Override
    protected Integer textCountPages(AssetSearchCriteria criteria, Long pageSize) {
        return (int)Math.ceil(getResultCount(criteria) / pageSize.doubleValue());
    }

    @Override
    protected Integer filterCountPages(AssetSearchCriteria criteria, Long pageSize) {
        return searchService.countPages(criteria, pageSize);
    }

    @Override
    protected SearchResult<Asset> textSearch(AssetSearchCriteria criteria, Integer pageNumber, Integer pageSize) {
        SearchResults search = fullTextSearchService.search(criteria.getQuery(), new Formatter() {
            @Override
            public String highlightTerm(String s, TokenGroup tokenGroup) {
                return s;
            }
        }, pageNumber * pageSize, pageSize);

        List<Asset> assetsList = new ArrayList<Asset>(pageSize);
        for (com.n4systems.services.search.SearchResult searchResult : search.getResults()) {
            Long id = Long.valueOf(searchResult.get(AssetIndexField.ID.getField()));
            Asset asset = persistenceService.find(Asset.class, id);
            fillInVirtualColumns(asset, criteria);
            assetsList.add(asset);
        }

        SearchResult<Asset> result = new SearchResult<Asset>();
        result.setResults(assetsList);
        result.setTotalResultCount(getResultCount(criteria));
        return result;
    }

    @Override
    protected SearchResult<Asset> filterSearch(AssetSearchCriteria criteriaModel, Integer pageNumber, Integer pageSize) {
        return searchService.performRegularSearch(criteriaModel, pageNumber, pageSize);
    }

    private int getResultCount(SearchCriteria searchCriteria) {
        SearchResults count = fullTextSearchService.count(searchCriteria.getQuery());
        return count.getCount();
    }

    private void fillInVirtualColumns(Asset asset, AssetSearchCriteria criteria) {
        if (criteria.sortingByOrIncludingLastEventDate()) {
            asset.setLastEventDate(lastEventDateService.findLastEventDate(asset));
        }
    }

}
