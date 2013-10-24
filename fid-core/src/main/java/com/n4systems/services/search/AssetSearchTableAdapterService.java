package com.n4systems.services.search;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.TableView;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AssetSearchTableAdapterService extends FieldIdPersistenceService {

    private @Autowired AssetFullTextSearchService fullTextSearchService;

    public int getResultCount(SearchCriteria searchCriteria) {
        SearchResults count = fullTextSearchService.count(searchCriteria.getQuery());
        return count.getCount();
    }

    public PageHolder<TableView> runSearch(SearchCriteria searchCriteria, int page, int pageSize) {
        SearchResults search = fullTextSearchService.search(searchCriteria.getQuery(), new Formatter() {
            @Override
            public String highlightTerm(String s, TokenGroup tokenGroup) {
                return s;
            }
        }, page*pageSize, pageSize);

        List<Asset> assetsList = new ArrayList<Asset>(pageSize);
        for (SearchResult searchResult : search.getResults()) {
            Long id = Long.valueOf(searchResult.get(AssetIndexField.ID.getField()));
            assetsList.add(persistenceService.find(Asset.class, id));

        }
        ResultTransformer<TableView> resultTransformer = new ResultTransformerFactory().createResultTransformer(searchCriteria);

        TableView transform = resultTransformer.transform(assetsList);
        return new PageHolder<TableView>(transform, getResultCount(searchCriteria));
    }

    public List<Long> getIdList(SearchCriteria criteria) {
        SearchResults search = fullTextSearchService.search(criteria.getQuery(), new Formatter() {
            @Override
            public String highlightTerm(String s, TokenGroup tokenGroup) {
                return s;
            }
        });
        List<Long> idList = new ArrayList<Long>();
        for (SearchResult searchResult : search.getResults()) {
            Long id = Long.valueOf(searchResult.get(AssetIndexField.ID.getField()));
            idList.add(id);
        }
        return idList;
    }

}
