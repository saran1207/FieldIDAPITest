package com.n4systems.fieldid.wicket.data;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.util.ResultTransformerFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.SearchResults;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.views.TableView;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class LuceneBackedAssetSearchDataProvider extends FieldIdAPIDataProvider {

    private @SpringBean AssetFullTextSearchService fullTextSearchService;
    private @SpringBean PersistenceService persistenceService;
    private SearchCriteria searchCriteria;

    public LuceneBackedAssetSearchDataProvider(SearchCriteria searchCriteria) {
        super(searchCriteria, "ignored", SortDirection.ASC);
        this.searchCriteria = searchCriteria;
    }

    @Override
    protected int getResultCount() {
        SearchResults count = fullTextSearchService.count(searchCriteria.getQuery());
        return count.getCount();
    }

    @Override
    protected PageHolder<TableView> runSearch(int page, int pageSize) {
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
        return new PageHolder<TableView>(transform, getResultCount());
    }

    @Override
    public List<Long> getIdList() {
        SearchResults search = fullTextSearchService.search(searchCriteria.getQuery(), new Formatter() {
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
