package com.n4systems.services.search.writer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.user.User;
import com.n4systems.services.search.AnalyzerFactory;
import com.n4systems.services.search.IndexException;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.services.search.parser.QueryTerm;
import com.n4systems.services.search.parser.SearchParserService;
import com.n4systems.services.search.parser.SearchQuery;
import com.n4systems.services.search.parser.SimpleValue;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class IndexWriter<T extends BaseEntity> extends FieldIdPersistenceService {

    private static final int DEFAULT_BOOST = 1;

    private @Resource PlatformTransactionManager transactionManager;  // is there a reason why this is @Resource, not @Autowired?
    protected @Autowired AnalyzerFactory analyzerFactory;
    protected @Autowired SearchParserService searchParserService;

    protected Logger logger = Logger.getLogger(getClass());
    private Class<T> itemClass;

    public IndexWriter(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

    public void reindexItems(final Tenant tenant, final QueryBuilder<T> query) {
        Long count = persistenceService.count(query);

        logger.info("Indexing " + count + " items of type " + itemClass.getSimpleName() + " for tenant " + tenant.getName());
        int page = 0, pageSize = 512;
        List<T> items;
        do {

            EntityManager em = getJpaEntityManager();
            begin(em);
            long startTime = System.currentTimeMillis();

            try {
                items = query.createQuery(em).setFirstResult(page*pageSize).setMaxResults(pageSize).getResultList();
                index(em, tenant, items, true);
                logger.info(getClass().getSimpleName() + " indexed " + ((page * pageSize) + items.size()) + " items of type " + itemClass.getSimpleName() + " for tenant " + tenant.getName() + " (the last " + items.size() + " in " + ((System.currentTimeMillis() - startTime)/1000) + " sec)");
                page++;
            } finally {
                cleanup(em);
            }

        } while (items.size() == pageSize);

        logger.info("Indexing " + tenant.getName() + " completed");
    }

    protected void begin(EntityManager em) { }

    public void index(final Tenant tenant, final List<T> items, boolean update) {
        EntityManager em = getJpaEntityManager();

        try {
            index(em, tenant, items, update);
        } finally {
            em.close();
        }
    }


    protected Document createNewDocument() {
        return new Document();
    }


    /* pkg protected for testing purposes */
    EntityManager getJpaEntityManager() {
        return ((JpaTransactionManager) transactionManager).getEntityManagerFactory().createEntityManager();
    }

    protected void cleanup(EntityManager em) {
        em.close();
    }

    protected abstract void index(EntityManager em, final Tenant tenant, final List<T> items, boolean update);

}
