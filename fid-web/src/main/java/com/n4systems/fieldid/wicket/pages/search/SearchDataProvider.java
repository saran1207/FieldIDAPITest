package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.services.search.FullTextSearchService;
import com.n4systems.services.search.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SearchDataProvider extends FieldIDDataProvider<SearchResult> {

    private @SpringBean FullTextSearchService fullTextSearchService;
    private IModel<String> searchText;
    private final SimpleHTMLFormatter formatter;

    public SearchDataProvider(IModel<String> searchText, SimpleHTMLFormatter formatter) {
        this.searchText = searchText;
        this.formatter = formatter;
    }

    @Override
    public Iterator<? extends SearchResult> iterator(int first, int count) {
        if (StringUtils.isBlank(searchText.getObject())) {
            return Collections.<SearchResult>emptyList().iterator();
        }
        List<SearchResult> results = fullTextSearchService.search(searchText.getObject(), formatter).getResults();
        return results.iterator();
    }

    @Override
    public int size() {
        if (StringUtils.isBlank(searchText.getObject())) {
            return 0;
        }
        return fullTextSearchService.count(searchText.getObject());
    }

    @Override
    public IModel<SearchResult> model(SearchResult object) {
        return new Model<SearchResult>(object);
    }

    @Override
    public void detach() {
        searchText.detach();
    }
}
