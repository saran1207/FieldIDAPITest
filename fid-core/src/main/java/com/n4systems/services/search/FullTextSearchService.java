package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.brainforest.SearchParserService;
import com.n4systems.services.brainforest.SearchQuery;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FullTextSearchService extends FieldIdPersistenceService {
    private static Logger logger = Logger.getLogger(FullTextSearchService.class);

    private @Autowired SearchParserService searchParserService;
    private @Autowired SecurityContext securityContext;
    private @Autowired AssetIndexerService assetIndexerService;


    public int count(final String queryString) {
        return lucene(new SearchQueryLuceneWorker<Integer>(queryString) {
            @Override Integer runQuery(Analyzer analyzer, IndexSearcher searcher) throws IOException {
                TotalHitCountCollector collector = new TotalHitCountCollector();
                searcher.search(getQuery(), getSecurityQueryFilter(), collector);
                return collector.getTotalHits();
            }
            @Override Integer defaultReturnValue() {
                return 0;
            }
        });
    }

    public SearchResults search(final String queryString, final Formatter formatter, final int start, final int count) {
        return lucene(new SearchResultsLuceneWorker(queryString, formatter) {
            @Override
            SearchResults runQuery(IndexSearcher searcher, Query query, SearchResults searchResults) throws IOException {
                TopDocs topDocs = searcher.search(query, getSecurityQueryFilter(), start + count);
                return searchResults.add(searcher, Arrays.copyOfRange(topDocs.scoreDocs, start, start+count));
            }
        });
    }

    public SearchResults search(final String queryString, final Formatter formatter) {
        return lucene(new SearchResultsLuceneWorker(queryString, formatter) {
            @Override SearchResults runQuery(IndexSearcher searcher, Query query, SearchResults searchResults) throws IOException {
                TopDocs topDocs = searcher.search(query, getSecurityQueryFilter(), Integer.MAX_VALUE);
                return searchResults.add(searcher, topDocs.scoreDocs);
            }
        });
    }

    public SearchResults search(String queryString) {
        return search(queryString, null);
    }

    public List<Document> findAll(String tenantName) {
        return lucene(new LuceneWorker<List<Document>>() {

            @Override
            List<Document> runQuery(Analyzer analyzer, IndexSearcher searcher) throws IOException {
                List<Document> docs = Lists.newArrayList();
                TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), Integer.MAX_VALUE);
                for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
                    docs.add(searcher.doc(scoreDoc.doc));
			    }
                return docs;
            }

            @Override List<Document> defaultReturnValue() {
                return Lists.newArrayList();
            }
        });
    }

    private <T> T lucene(LuceneWorker<T> worker) {
        final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        Directory dir = null;
        IndexReader reader = null;

        try {
            dir = FSDirectory.open(new File(assetIndexerService.getIndexPath(getCurrentTenant())));
            reader = DirectoryReader.open(dir);
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            return worker.runQuery(analyzer, indexSearcher);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            closeQuietly(reader, dir, analyzer);
        }

        return worker.defaultReturnValue();
    }

    private SearchResults createSearchResults(final Query query, final Analyzer analyzer, final SearchQuery searchQuery, final Formatter formatter) {
        return new SearchResults() {
            @Override protected Analyzer getAnalyzer() {
                return analyzer;
            }
            @Override protected FieldIdHighlighter getHighlighter() {
                if (formatter==null) {
                    return null;
                }
                QueryScorer scorer = new QueryScorer(query);
                scorer.setExpandMultiTermQuery(true);
                return new FieldIdHighlighter(formatter, scorer, searchQuery);
            }
        };
    }

	private void logDocument(Document doc) {
		StringBuilder sb = new StringBuilder("Document: ");
		for (IndexableField field: doc.getFields()) {
			sb.append(field.name()).append(':').append(field.stringValue()).append(", ");
		}
		logger.info(sb.toString());
	}

    private Filter getSecurityQueryFilter() {
        // TODO DD : create cacheable filters based on org/tenant.
        // use spring's @Cahceable annotation for getOwnerFilter(owner);
        // make sure that method returns a CacheableWrapperFilter!
        // need to put a @CacheEvict on service call that updates Orgs!
        BooleanQuery securityQuery = new BooleanQuery();

        BaseOrg owner = this.securityContext.getUserSecurityFilter().getOwner();
        if (owner.isPrimary()) {
            return null;    // can see everything...no filtering required.
        } else if (owner.isSecondary()) {
            Long id = owner.getId();  // secondary can see primary, and this secondary
            securityQuery.add(new BooleanClause(NumericRangeQuery.newLongRange("_secondaryOrgId", id, id, true, true), BooleanClause.Occur.SHOULD));
            // TODO DD : might be better to just assume secondary org will be -1 if doesn't exist.  will have to change indexer to do this.
            securityQuery.add(NumericRangeQuery.newLongRange(AssetIndexField.SECONDARY_ID.getField(), 0L, null, true, true), BooleanClause.Occur.MUST_NOT);  // tricky way of saying "should be null"
        } else if (owner.isCustomer() || owner.isDivision()) {
            addOrgFilter(securityQuery, owner.getSecondaryOrg(), AssetIndexField.SECONDARY_ID.getField());
            addOrgFilter(securityQuery, owner.getCustomerOrg(), AssetIndexField.CUSTOMER_ID.getField());
            addOrgFilter(securityQuery, owner.getDivisionOrg(), AssetIndexField.DIVISION_ID.getField());
        } else {
            throw new IllegalStateException("don't know how to handle security for this owner " + owner.getDisplayName());
        }
        return new QueryWrapperFilter(securityQuery);
    }

    private void addOrgFilter(BooleanQuery query, BaseOrg org, String field) {
        if (org!=null) {
            query.add(NumericRangeQuery.newLongRange(field, org.getId(), org.getId(), true, true), BooleanClause.Occur.MUST);
        }
    }

    private void closeQuietly(Closeable ... closeables) {
        try { IOUtils.close(closeables); } catch (Exception e) {}
    }


    // --------------------------------------------------------------


    abstract class LuceneWorker<T> {
        abstract T runQuery(Analyzer analyzer, IndexSearcher searcher) throws IOException;
        abstract T defaultReturnValue();
    }

    abstract class SearchQueryLuceneWorker<T> extends LuceneWorker<T> {
        protected  SearchQuery searchQuery;
        protected String queryString;

        protected SearchQueryLuceneWorker(String queryString) {
            this.queryString = queryString;
        }

        Query getQuery() {
            searchQuery = searchParserService.createSearchQuery(queryString);
            return searchParserService.convertToLuceneQuery(searchQuery);
        }
    }

    abstract class SearchResultsLuceneWorker extends SearchQueryLuceneWorker<SearchResults> {
        private final Formatter formatter;

        protected SearchResultsLuceneWorker(String queryString, Formatter formatter) {
            super(queryString);
            this.formatter = formatter;
        }

        SearchResults runQuery(Analyzer analyzer, IndexSearcher searcher) throws IOException {
            Query query = getQuery();
            SearchResults searchResults = createSearchResults(query, analyzer, searchQuery, formatter);
            return runQuery(searcher, query, searchResults);
        }

        SearchResults defaultReturnValue() {
            return new SearchResults();
        }

        abstract SearchResults runQuery(IndexSearcher searcher, Query query, SearchResults searchResults) throws IOException;
    }

}
