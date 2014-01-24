package com.n4systems.fieldid.service.search.columns;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.SearchResult;
import com.n4systems.model.Asset;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.MappedResults;
import com.n4systems.services.search.SearchResults;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AssetTextOrFilterSearchService extends TextOrFilterSearchService<AssetSearchCriteria, Asset, AssetSearchRecord> {

    private @Autowired AssetFullTextSearchService fullTextSearchService;
    private @Autowired AssetSearchService searchService;
    private @Autowired LastEventDateService lastEventDateService;
    private @Autowired AssetService assetService;

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
    protected SearchResult<Asset> findSelectedEntities(AssetSearchCriteria criteriaModel, int pageNumber, int pageSize) {
        int beginIndex = pageNumber * pageSize;
        List<Long> selectedIdList = criteriaModel.getSelection().getSelectedIds();
        List<Long> currentPageOfSelectedIds = selectedIdList.subList(beginIndex, Math.min(selectedIdList.size(), beginIndex + pageSize));

        List<Asset> entities = new ArrayList<Asset>(pageSize);
        Asset asset = null;
        for (Long id : currentPageOfSelectedIds) {
            asset = assetService.findAssetWithInfoOptions(id);
            fillInVirtualColumns(asset, criteriaModel);
            entities.add(asset);
        }

        SearchResult<Asset> searchResult = new SearchResult<Asset>();
        searchResult.setResults(entities);
        searchResult.setTotalResultCount(entities.size());
        return searchResult;
    }

    @Override
    protected MappedResults<AssetSearchRecord> filterSearch(AssetSearchCriteria criteriaModel) {
        return searchService.performMapSearch(criteriaModel);
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
