package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.brainforest.SearchParserService;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FullTextSearchService extends FieldIdPersistenceService {
    private static Logger logger = Logger.getLogger(FullTextSearchService.class);

    public static final int DOC_COUNT = 1000;

    private @Autowired SearchParserService searchParserService;
    private @Autowired SecurityContext securityContext;
    private @Autowired AssetIndexerService assetIndexerService;


	private List<Document> search(IndexReader reader, Analyzer analyzer, Query query) throws IOException, ParseException {
        // TODO DD: do i need analyzer parameter???
		List<Document> docs = new ArrayList<Document>();

		//Query query = searchParserService.createSearchQuery(queryString);     //new QueryParser(Version.LUCENE_43, "identifier", analyzer).parse(queryString);

		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs topDocs = searcher.search(query, getSecurityQueryFilter(), DOC_COUNT);

        // TODO DD : if query doesn't specify attributes, then use indexSearcher to explain query results (i.e. which fields matched).
        //   also should return the query along with the docs, so UI can normalize it and also use it to find specified results.

		for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
			docs.add(searcher.doc(scoreDoc.doc));
		}
		return docs;
	}

    public SearchResults search(final String queryString, Formatter formatter) {

        boolean isFormatted = false;

        if (null != formatter) {
            isFormatted = true;
        }

        Analyzer analyzer = null;
        Directory dir = null;
        IndexReader reader = null;
        SearchResults results = null;

        try {
            analyzer = new StandardAnalyzer(Version.LUCENE_43);
            dir = FSDirectory.open(new File(assetIndexerService.getIndexPath(getCurrentTenant())));
            reader = DirectoryReader.open(dir);

            Query query = searchParserService.createSearchQuery(queryString);     //new QueryParser(Version.LUCENE_43, "identifier", analyzer).parse(queryString);

            List<Document> docs = search(reader, analyzer, query);


            QueryScorer queryScorer = new QueryScorer(query);
            Highlighter highlighter = null;

            /* TODO - dont bother highlighting if no Formatter*/
            if (isFormatted) {
                highlighter = new Highlighter(formatter, queryScorer);
                highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, Integer.MAX_VALUE));
                highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);

            }

            // TODO DD :put search query in results so we can normalize display.
            results = new SearchResults();
            logger.info(queryString + ": " + docs.size());
            for (Document doc: docs) {
                if (isFormatted) {
                    results.add(doc, highlighter, analyzer);
                } else {
                    results.add(doc);
                }

                logDocument(doc);
            }
            return results;

        } catch (Exception e) {
            logger.error(e);
        } finally {
            closeQuietly(reader, dir, analyzer);
        }

        return results;

    }


    public SearchResults search(String queryString) {
        return search(queryString, null);
    }



	public List<Document> findAll(String tenantName) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
		builder.addSimpleWhere("name", tenantName);
		Tenant tenant = persistenceService.find(builder);

		Analyzer analyzer = null;
		Directory dir = null;
		IndexReader reader = null;
		try {
			analyzer = new StandardAnalyzer(Version.LUCENE_43);
			dir = FSDirectory.open(new File(assetIndexerService.getIndexPath(tenant)));
			reader = DirectoryReader.open(dir);

			List<Document> docs = new ArrayList<Document>();

			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs topDocs = searcher.search(new MatchAllDocsQuery(), Integer.MAX_VALUE);
			for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
				docs.add(searcher.doc(scoreDoc.doc));
			}

			logger.info(tenant.getName() +  " Documents: ");
			for (Document doc: docs) {
				logDocument(doc);
			}

            return docs;

		} catch (Exception e) {
			logger.error(e);
		} finally {
			closeQuietly(reader, dir, analyzer);
		}
        return null;
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



}
