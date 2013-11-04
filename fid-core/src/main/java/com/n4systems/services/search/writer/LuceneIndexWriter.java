package com.n4systems.services.search.writer;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.user.User;
import com.n4systems.services.search.IndexException;
import com.n4systems.services.search.field.IndexField;
import com.n4systems.services.search.parser.QueryTerm;
import com.n4systems.services.search.parser.SearchQuery;
import com.n4systems.services.search.parser.SimpleValue;
import com.n4systems.util.StringUtils;
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
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class LuceneIndexWriter<T extends BaseEntity> extends IndexWriter<T> {

    private static final int DEFAULT_BOOST = 1;

    public LuceneIndexWriter(Class<T> itemClass) {
        super(itemClass);
    }

    protected abstract void unindex(org.apache.lucene.index.IndexWriter writer, T item) throws IOException;

    protected void unindex(org.apache.lucene.index.IndexWriter writer, Long id, String idFieldName) throws IOException {
        SearchQuery searchQuery = new SearchQuery().add(new QueryTerm(idFieldName, QueryTerm.Operator.EQ, new SimpleValue(id + "")));
        Query query = searchParserService.convertToLuceneQuery(searchQuery);
        writer.deleteDocuments(query);
    }

    protected void index(EntityManager em, final Tenant tenant, final List<T> items, boolean update) {
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
            writer = createIndexWriter(dir, writerConfig);

            for (T item : items) {
                if (item instanceof Archivable && ((Archivable)item).isArchived()) {
                    unindex(writer, item);
                } else {
                    Document doc = createDocument(em, item);
                    if (update) {
                        BytesRef bytes = new BytesRef(NumericUtils.BUF_SIZE_LONG);
                        NumericUtils.longToPrefixCoded(item.getId(), 0, bytes);
                        writer.updateDocument(new Term("_id", bytes),  doc);
                    } else {
                        writer.addDocument(doc);
                    }
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

    /* pkg protected for testing purposes */
    org.apache.lucene.index.IndexWriter createIndexWriter(Directory dir, IndexWriterConfig writerConfig) throws IOException {
        return new org.apache.lucene.index.IndexWriter(dir, writerConfig);
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
