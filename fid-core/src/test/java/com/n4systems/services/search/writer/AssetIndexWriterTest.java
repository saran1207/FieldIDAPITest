package com.n4systems.services.search.writer;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.search.AnalyzerFactory;
import com.n4systems.services.search.IndexException;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.services.search.parser.QueryTerm;
import com.n4systems.services.search.parser.SearchParserService;
import com.n4systems.services.search.parser.SearchQuery;
import com.n4systems.services.search.parser.SimpleValue;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class AssetIndexWriterTest extends FieldIdServiceTest {

    private @TestTarget AssetIndexWriter assetIndexWriter;
    private @TestMock SearchParserService searchParserService;
    private @TestMock AnalyzerFactory analyzerFactory;
    private @TestMock PersistenceService persistenceService;
    private @TestMock SecurityContext securityContext;


    private org.apache.lucene.index.IndexWriter indexWriter;
    private EntityManager entityManager;
    private Document document;


    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new AssetIndexWriter() {
            @Override EntityManager getJpaEntityManager() {
                return entityManager;
            }
            @Override
            protected IndexWriter createIndexWriter(Directory dir, IndexWriterConfig writerConfig) throws IOException {
                return indexWriter;
            }

            @Override
            protected Document createNewDocument() {
                return document;
            }
        };
    }

    @Override
    public void setUp() {
        super.setUp();
        indexWriter = createMock(org.apache.lucene.index.IndexWriter.class);
        entityManager = createMock(EntityManager.class);
        document = new Document();
    }

    @Test
    public void testGetIndexPath() throws Exception {
        Tenant tenant = TenantBuilder.n4();
        String indexPath = assetIndexWriter.getIndexPath(tenant);
        assertEquals("/var/fieldid/private/indexes/n4/assets", indexPath);
    }

    @Test
    public void testGetFields() throws Exception {
        IndexField[] fields = assetIndexWriter.getFields();
        assertArrayEquals(AssetIndexField.values(), fields);
    }

    @Test
    public void testGetField() throws Exception {
        // should resolve all of these...
        for (AssetIndexField f:AssetIndexField.values()) {
            assertEquals(f, assetIndexWriter.getField(f.getField()));
        }

        // test case insensitivity
        assertEquals(AssetIndexField.ID, assetIndexWriter.getField("_ID"));
        assertEquals(AssetIndexField.ID, assetIndexWriter.getField("_iD"));
        assertEquals(AssetIndexField.ID, assetIndexWriter.getField("_Id"));

        assertEquals(AssetIndexField.PURCHASE_ORDER, assetIndexWriter.getField("PO"));
        assertEquals(AssetIndexField.PURCHASE_ORDER, assetIndexWriter.getField("pO"));

        // if it can't find it, return null.
        assertNull(assetIndexWriter.getField("bogusFieldName"));
    }

    @Test
    public void testIndex() throws Exception {
        AssetType type = AssetTypeBuilder.anAssetType().named("type").build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named("status").build();
        Tenant tenant = TenantBuilder.n4();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().withName("org").build();
        Asset asset = AssetBuilder.anAsset().
                ofType(type).
                havingStatus(status).
                inFreeformLocation("location").
                referenceNumber("ref").
                rfidNumber("rfid").
                withOwner(owner).
                forTenant(tenant).build();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        expect(analyzerFactory.createAnalyzer(aryEq(AssetIndexField.values()))).andReturn(analyzer);
        replay(analyzerFactory);

        indexWriter.addDocument(document);
        indexWriter.close();
        replay(indexWriter);

        assetIndexWriter.index(tenant, Lists.newArrayList(asset), false);

        // assert that doc has fields.

        verifyTestMocks();
    }

    @Test
    public void testIndexArchived() throws Exception {
        AssetType type = AssetTypeBuilder.anAssetType().named("type").build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named("status").build();
        Tenant tenant = TenantBuilder.n4();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().withName("org").build();
        Asset asset = AssetBuilder.anAsset().
                ofType(type).
                havingStatus(status).
                inFreeformLocation("location").
                referenceNumber("ref").
                rfidNumber("rfid").
                withOwner(owner).
                forTenant(tenant).build();
        asset.archiveEntity();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        expect(analyzerFactory.createAnalyzer(aryEq(AssetIndexField.values()))).andReturn(analyzer);
        replay(analyzerFactory);

        Query query = new BooleanQuery();
        SearchQuery searchQuery = new SearchQuery().add(new QueryTerm(AssetIndexField.ID.getField(), QueryTerm.Operator.EQ, new SimpleValue(asset.getId()+"")));

        indexWriter.deleteDocuments(query);
        indexWriter.close();
        replay(indexWriter);
        expect(searchParserService.convertToLuceneQuery(SearchQueryMatcher.eq(searchQuery))).andReturn(query);
        replay(searchParserService);

        assetIndexWriter.index(tenant, Lists.newArrayList(asset), false);

        verifyTestMocks();
    }

    @Test(expected = IndexException.class)
    public void testExceptionIndex() throws Exception {
        document = null;        /// this will cause a NPE during method call.  we want to make sure that close() is still called.

        AssetType type = AssetTypeBuilder.anAssetType().named("type").build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named("status").build();
        Tenant tenant = TenantBuilder.n4();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().withName("org").build();
        Asset asset = AssetBuilder.anAsset().
                ofType(type).
                havingStatus(status).
                inFreeformLocation("location").
                referenceNumber("ref").
                rfidNumber("rfid").
                withOwner(owner).
                forTenant(tenant).build();

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
        expect(analyzerFactory.createAnalyzer(aryEq(AssetIndexField.values()))).andReturn(analyzer);
        replay(analyzerFactory);

        assetIndexWriter.index(tenant, Lists.newArrayList(asset), false);

        verifyTestMocks();
    }


    static protected class SearchQueryMatcher implements IArgumentMatcher {

        private String searchQueryAsString;

        public SearchQueryMatcher(SearchQuery searchQuery) {
            Preconditions.checkArgument(searchQuery!=null);
            this.searchQueryAsString = searchQuery.toString();
        }

        public static SearchQuery eq(SearchQuery searchQuery) {
            EasyMock.reportMatcher(new SearchQueryMatcher(searchQuery));
            return null;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append(searchQueryAsString);
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof SearchQuery) ) {
                return false;
            }
            SearchQuery actual = (SearchQuery) argument;
            return searchQueryAsString.equalsIgnoreCase(actual.toString());
        }

    }

}
