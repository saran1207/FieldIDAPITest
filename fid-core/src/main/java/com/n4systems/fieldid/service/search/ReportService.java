package com.n4systems.fieldid.service.search;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.model.Event;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.util.persistence.search.ResultTransformer;

import java.util.List;

public class ReportService extends FieldIdService {

    private SearchService searchService;

    public ReportService() {}

    public ReportService(SearchService searchService) {
        this.searchService = searchService;
    }

    public <K> PageHolder<K> eventReport(EventReportCriteriaModel criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize) {
        SearchResult<Event> eventSearchResult = searchService.performEventSearch(criteriaModel, pageNumber, pageSize);

        List<Event> entities = eventSearchResult.getResults();
        List<Event> oldEntities = entities;

        entities = EntitySecurityEnhancer.enhanceList(entities, userSecurityFilter);

        K pageResults = transformer.transform(entities);

        return new PageHolder<K>(pageResults, eventSearchResult.getTotalResultCount());
    }

    public List<Long> idSearch(EventReportCriteriaModel criteriaModel) {
        return searchService.idSearch(criteriaModel);
    }

    public Integer countPages(EventReportCriteriaModel criteriaModel, Long pageSize) {
        return searchService.countPages(criteriaModel, pageSize);
    }

}
