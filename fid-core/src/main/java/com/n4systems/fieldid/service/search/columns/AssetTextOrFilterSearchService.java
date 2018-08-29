package com.n4systems.fieldid.service.search.columns;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.fieldid.service.search.SearchResult;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.localization.LocalizationService;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.MappedResults;
import com.n4systems.services.search.SearchResults;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.util.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class AssetTextOrFilterSearchService extends TextOrFilterSearchService<AssetSearchCriteria, Asset, AssetSearchRecord> {

    private @Autowired AssetFullTextSearchService fullTextSearchService;
    private @Autowired AssetSearchService searchService;
    private @Autowired LocalizationService localizationService;
    private @Autowired AssetService assetService;

    @Override
    protected List<Long> textIdSearch(AssetSearchCriteria criteria) {
        SearchResults search = fullTextSearchService.search(criteria.getQuery(), (s, tokenGroup) -> s);
        List<Long> idList = new ArrayList<>();
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
        SearchResults search = fullTextSearchService.search(criteria.getQuery(), (s, tokenGroup) -> s, pageNumber * pageSize, pageSize);

        List<Long> assetIDs = search.getResults().stream().map(result -> Long.valueOf(result.get(AssetIndexField.ID.getField()))).collect(Collectors.toList());
        List<Asset> assets = persistenceService.findAllById(Asset.class, assetIDs);

        SearchResult<Asset> result = new SearchResult<>();
        result.setResults(assets);
        result.setTotalResultCount(getResultCount(criteria));
        return result;
    }

    @Override
    protected SearchResult<Asset> findSelectedEntities(AssetSearchCriteria criteriaModel, int pageNumber, int pageSize) {
        int beginIndex = pageNumber * pageSize;
        List<Long> selectedIdList = criteriaModel.getSelection().getSelectedIds();
        List<Long> currentPageOfSelectedIds = selectedIdList.subList(beginIndex, Math.min(selectedIdList.size(), beginIndex + pageSize));

        List<Asset> entities = persistenceService.findAllById(Asset.class, currentPageOfSelectedIds);


        SearchResult<Asset> searchResult = new SearchResult<>();
        searchResult.setResults(convertResults(entities));
        searchResult.setTotalResultCount(entities.size());
        return searchResult;
    }

    private List<Asset> convertResults(List<Asset> assets) {
        //We need the original attribute name and not the translated name for custom search columns
        if (localizationService.hasTranslations(getCurrentUser().getLanguage())) {
            return assetService.getUntranslatedCustomSearchColumnNames(assets);
        }
        else {
            return assets;
        }
    }

    @Override
    protected MappedResults<AssetSearchRecord> filterMapSearch(AssetSearchCriteria criteriaModel) {
        return searchService.performMapSearch(criteriaModel);
    }

    @Override
    protected SearchResult<Asset> filterSearch(AssetSearchCriteria criteriaModel, Integer pageNumber, Integer pageSize) {
        if (criteriaModel.sortingByNetworkLastEventDate())
            return searchService.performRegularSearch(criteriaModel,
                    new MultipleSelectClause().addSubQuery(createNetworkLastEventDateSubQuery("obj")).addSimpleSelect("obj"),
                    pageNumber, pageSize, new GetEntityFromResultTransformer(Asset.class));
        else
            return searchService.performRegularSearch(criteriaModel, pageNumber, pageSize);
    }

    private int getResultCount(SearchCriteria searchCriteria) {
        SearchResults count = fullTextSearchService.count(searchCriteria.getQuery());
        return count.getCount();
    }

    private QueryBuilder<Date> createNetworkLastEventDateSubQuery(String mainQueryAlias) {
        QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");

        qBuilder.setMaxSelect("completedDate");
        qBuilder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        qBuilder.addSimpleWhere("workflowState", WorkflowState.COMPLETED);
        qBuilder.addWhere(new CorrelatedSubQueryWhereClause(
                CorrelatedSubQueryWhereClause.Comparator.EQ, mainQueryAlias, "asset.networkId", "networkId", WhereClause.ChainOp.AND));
        qBuilder.setQueryAlias("networkLastEventDate");
        return qBuilder;
    }

}
