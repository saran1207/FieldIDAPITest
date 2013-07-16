package com.n4systems.services.search.writer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.user.User;
import com.n4systems.services.search.AnalyzerFactory;
import com.n4systems.services.search.IndexException;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class IndexWriter<T extends BaseEntity> extends FieldIdPersistenceService {

    private static final int DEFAULT_BOOST = 1;

    private @Resource PlatformTransactionManager transactionManager;
    protected @Autowired AnalyzerFactory analyzerFactory;

    private static Logger logger = Logger.getLogger(IndexWriter.class);
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

            EntityManager em = ((JpaTransactionManager) transactionManager).getEntityManagerFactory().createEntityManager();

            try {
                items = query.createQuery(em).setFirstResult(page*pageSize).setMaxResults(pageSize).getResultList();
                index(em, tenant, items, true);
                logger.info("Indexed " + ((page * pageSize) + items.size()) + " items of type " + itemClass.getSimpleName() + " for tenant " + tenant.getName());
                page++;
            } finally {
                em.close();
            }

        } while (items.size() == pageSize);

        logger.info("Indexing " + tenant.getName() + " completed");
    }

    public void index(final Tenant tenant, final List<T> items, boolean update) {
        EntityManager em = ((JpaTransactionManager) transactionManager).getEntityManagerFactory().createEntityManager();
        try {
            index(em, tenant, items, update);
        } finally {
            em.close();
        }
    }

    private void index(EntityManager em, final Tenant tenant, final List<T> items, boolean update) {
        long startTime = System.currentTimeMillis();
        Directory dir = null;
        org.apache.lucene.index.IndexWriter writer = null;
        Analyzer analyzer = null;

        try {
            analyzer = analyzerFactory.createAnalyzer(getFields());
            IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
            writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            writerConfig.setRAMBufferSizeMB(256.0);

            dir = FSDirectory.open(new File(getIndexPath(tenant)));
            writer = new org.apache.lucene.index.IndexWriter(dir, writerConfig);

            for (T item : items) {
                Document doc = createDocument(em, item);
                if (update) {
                    BytesRef bytes = new BytesRef(NumericUtils.BUF_SIZE_LONG);
                    NumericUtils.longToPrefixCoded(item.getId(), 0, bytes);
                    writer.updateDocument(new Term("_id", bytes),  doc);
                } else {
                    writer.addDocument(doc);
                }
            }
        } catch (Exception e) {
            throw new IndexException(e);
        } finally {
            try {
                IOUtils.close(writer, dir, analyzer);
            } catch (Exception e) {}
            logger.info("Index task completed in " + ((System.currentTimeMillis() - startTime) / 1000.0) + "s");
        }
    }

    protected void addAllField(IndexField allField, Document doc) {
        ArrayList<String> fields = Lists.newArrayList(Iterables.transform(doc.getFields(), new Function<IndexableField, String>() {
            @Override
            public String apply(IndexableField input) {
                return input.stringValue();
            }
        }));
        addField(doc, allField, Joiner.on(" ").join(fields));
    }

    protected Object parseInfoOptionValue(InfoOptionBean infoOption) {
        String strValue = StringUtils.clean(infoOption.getName());
        if (strValue == null) return null;

        Object value;
        if (infoOption.getInfoField().getFieldType().equals(InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
            try {
                value = new Date(Long.parseLong(strValue));
            } catch (NumberFormatException nfe) {
                logger.warn("Couldn't parse date infoFieldBean '" + strValue + "'  for InfoFieldBean " +  infoOption.getUniqueID() + ". Treating it as a string for now. ",  nfe);
                value = strValue;
            }
        } else {
            // attempt to parse the string into a more specific type (Double otherwise String).
            // recall all numeric custom attributes are considered double because we won't know at query time so just use widest type of query.
            try {
                value = Double.parseDouble(strValue);
            } catch (NumberFormatException e2) {
                // fall back to String
                value = strValue;
            }
        }
        return value;
    }

    protected void addIdField(Document doc, String name, BaseEntity baseEntity) {
        if (baseEntity != null) {
            addField(doc, getField(name), baseEntity.getId());
        }
    }

    protected void addNamedEntity(Document doc, String name, NamedEntity namedEntity) {
        if (namedEntity != null) {
            addField(doc, getField(name), namedEntity.getName());
        }
    }

    protected void addUserField(Document doc, String name, User user) {
        if (user != null) {
            addField(doc, getField(name), user.getDisplayName());
        }
    }

    protected void addCustomField(Document doc, String name, Object value) {
        // for custom fields, all numeric values are forced to double.
        if (value instanceof Number) {
            addField(doc,name,((Number)value).doubleValue());
        } else {
            addField(doc,name,value);
        }
    }

    protected void addField(Document doc, String name, Object value, int boost) {
        if (value != null) {
            Field field = null;
            name = name.toLowerCase().trim();
            if (value instanceof String) {
                doc.add(field = new TextField(name, (String) value, Field.Store.YES));
            } else if (value instanceof Float) {
                doc.add(field = new FloatField(name, (Float) value, Field.Store.YES));
            } else if (value instanceof Double) {
                doc.add(field = new DoubleField(name, (Double) value, Field.Store.YES));
            } else if (value instanceof Long) {
                doc.add(field = new LongField(name, (Long) value, Field.Store.YES));
            } else if (value instanceof Integer) {
                doc.add(field = new IntField(name, (Integer) value, Field.Store.YES));
            } else if (value instanceof Date) {
                doc.add(field = new LongField(name, ((Date) value).getTime(), Field.Store.YES));
            } else {
                throw new RuntimeException("Unhandled Field Type: " + value.getClass());
            }
            // TODO DD : we may want to control which fields omitNorms() instead of just using defaults.??
            if (!field.fieldType().omitNorms()) {
                field.setBoost(boost);
            }
        }
    }

    protected void addField(Document doc, String name, Object value) {
        addField(doc, name, value, DEFAULT_BOOST);
    }

    protected void addField(Document doc, IndexField indexField, Object value) {
        addField(doc, indexField.getField(), value, indexField.getBoost());
    }

    public abstract String getIndexPath(Tenant tenant);
    protected abstract Document createDocument(EntityManager em, T item);
    protected abstract IndexField[] getFields();
    protected abstract IndexField getField(String fieldName);

}
