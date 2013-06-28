package com.n4systems.services.search;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownWithPrimaryFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional
public class AssetIndexerService extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AssetIndexerService.class);


    private static final String INDEX_FORMAT = "/var/fieldid/private/indexes/%s/assets";
    private static final int DEFAULT_BOOST = 1;

    private @Autowired AnalyzerFactory analyzerFactory;
    private @Resource PlatformTransactionManager transactionManager;

    @Scheduled(fixedDelay = 5000)
	public void processIndexQueue() {
        long startTime = System.currentTimeMillis();
		logger.info("ProcessIndexQueue: Running");

		List<IndexQueueItem> items = persistenceService.findAllNonSecure(IndexQueueItem.class);
		logger.info("Queue length = " + items.size());
		for (IndexQueueItem item : items) {
			try {
				processIndexQueueItem(item);
				persistenceService.deleteAny(item);
			} catch (Exception e) {
				logger.warn("ProcessIndexQueue: Failed for " + item.getType() + ":" + item.getId());
			}
		}
		logger.info("ProcessIndexQueue: Completed " + (System.currentTimeMillis() - startTime) + "ms");
	}

	private void processIndexQueueItem(IndexQueueItem item) {
		switch (item.getType()) {
			case ASSET_INSERT:
				indexAsset(persistenceService.findNonSecure(Asset.class, item.getId()));
				break;
			case ASSET_UPDATE:
				reindexAsset(persistenceService.findNonSecure(Asset.class, item.getId()));
				break;
			case USER:
				reindexAssetsByUser(persistenceService.findNonSecure(User.class, item.getId()));
				break;
			case ORG:
				reindexAssetsByOrg(persistenceService.findNonSecure(BaseOrg.class, item.getId()));
				break;
			case ORDER:
				reindexAssetsByOrder(persistenceService.findNonSecure(Order.class, item.getId()));
				break;
			case PREDEFINEDLOCATION:
				reindexAssetsByPredefinedLocation(persistenceService.findNonSecure(PredefinedLocation.class, item.getId()));
				break;
			case ASSETTYPE:
				reindexAssetsByAssetType(persistenceService.findNonSecure(AssetType.class, item.getId()));
				break;
			case ASSETTYPEGROUP:
				reindexAssetsByAssetTypeGroup(persistenceService.findNonSecure(AssetTypeGroup.class, item.getId()));
				break;
			case ASSETSTATUS:
				reindexAssetsByAssetStatus(persistenceService.findNonSecure(AssetStatus.class, item.getId()));
				break;
			case TENANT:
				reindexAssetsByTenant(persistenceService.findNonSecure(Tenant.class, item.getId()));
				break;
			default:
				throw new IllegalArgumentException("Unhandled type: " + item.getType());
		}
	}

	private TenantOnlySecurityFilter createTenantFilter(HasTenant hasTenantEntity) {
		return new TenantOnlySecurityFilter(hasTenantEntity.getTenant());
	}

	private void indexAsset(Asset asset) {
		index(asset.getTenant(), Arrays.asList(asset), false);
	}

	private void reindexAsset(Asset asset) {
		index(asset.getTenant(), Arrays.asList(asset), true);
	}

	private void reindexAssetsByUser(User user) {
		reindexAssets(user.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(user))
				.addWhere(WhereClauseFactory.group("user_params",
						WhereClauseFactory.create("createdBy", user, WhereClause.ChainOp.OR),
						WhereClauseFactory.create("modifiedBy", user, WhereClause.ChainOp.OR),
						WhereClauseFactory.create("identifiedBy", user, WhereClause.ChainOp.OR),
						WhereClauseFactory.create("assignedUser", user, WhereClause.ChainOp.OR)
				)));
	}

	private void reindexAssetsByOrg(BaseOrg org) {
		reindexAssets(org.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(org))
				.applyFilter(new OwnerAndDownWithPrimaryFilter(org)));
	}

	private void reindexAssetsByOrder(Order order) {
		reindexAssets(order.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(order))
				.addWhere(WhereClauseFactory.create("shopOrder.order", order)));
	}

	private void reindexAssetsByPredefinedLocation(PredefinedLocation predefinedLocation) {
		reindexAssets(predefinedLocation.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(predefinedLocation))
				.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "advancedLocation.predefinedLocation.searchIds", "preLocSearchId", true))
				.addWhere(new PredefinedLocationSearchTerm("preLocSearchId", predefinedLocation.getId())));
	}

	private void reindexAssetsByAssetType(AssetType assetType) {
		reindexAssets(assetType.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetType))
				.addWhere(WhereClauseFactory.create("type", assetType)));
	}

	private void reindexAssetsByAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
		reindexAssets(assetTypeGroup.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetTypeGroup))
				.addWhere(WhereClauseFactory.create("type.group", assetTypeGroup)));
	}

	private void reindexAssetsByAssetStatus(AssetStatus assetStatus) {
		reindexAssets(assetStatus.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetStatus))
				.addWhere(WhereClauseFactory.create("assetStatus", assetStatus)));
	}

	private void reindexAssetsByTenant(Tenant tenant) {
		reindexAssets(tenant, new QueryBuilder<Asset>(Asset.class, new TenantOnlySecurityFilter(tenant)));
	}

	public void indexTenant(String tenantName) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
		builder.addSimpleWhere("name", tenantName);
		Tenant tenant = persistenceService.find(builder);

        if(!queueItemExists(tenant.getId(), IndexQueueItem.IndexQueueItemType.TENANT)) {
            IndexQueueItem item = new IndexQueueItem();
            item.setType(IndexQueueItem.IndexQueueItemType.TENANT);
            item.setId(tenant.getId());

            persistenceService.saveAny(item);
        }
	}

    private boolean queueItemExists(Long id, IndexQueueItem.IndexQueueItemType type){
        QueryBuilder<IndexQueueItem> builder = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class, new OpenSecurityFilter());
        builder.addSimpleWhere("item.id", id);
        builder.addSimpleWhere("item.type", IndexQueueItem.IndexQueueItemType.TENANT);

        return persistenceService.exists(builder);
    }

	private void reindexAssets(final Tenant tenant, final QueryBuilder<Asset> query) {
		Long count = persistenceService.count(query);

		logger.info("Indexing " + count + " assets for tenant " + tenant.getName());
		int page = 0, pageSize = 512;
		List<Asset> assets;
		do {

            EntityManager em = ((JpaTransactionManager) transactionManager).getEntityManagerFactory().createEntityManager();

            try {
                assets = query.createQuery(em).setFirstResult(page*pageSize).setMaxResults(pageSize).getResultList();
                index(tenant, assets, true);
                logger.info("Indexed " + ((page * pageSize) + assets.size()) + " assets for tenant " + tenant.getName());
                page++;
            } finally {
                em.close();
            }

		} while (assets.size() == pageSize);

		logger.info("Indexing " + tenant.getName() + " completed");
	}

	public void index(final Tenant tenant, final List<Asset> assets, boolean update) {
		long startTime = System.currentTimeMillis();
		Directory dir = null;
		IndexWriter writer = null;
        Analyzer analyzer = null;

        try {
            analyzer = analyzerFactory.createAnalyzer(AssetIndexField.values());
			IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			writerConfig.setRAMBufferSizeMB(256.0);

			dir = FSDirectory.open(new File(getIndexPath(tenant)));
			writer = new IndexWriter(dir, writerConfig);

			for (Asset asset : assets) {
				Document doc = createDocument(asset);
				if (update) {
					BytesRef bytes = new BytesRef(NumericUtils.BUF_SIZE_LONG);
					NumericUtils.longToPrefixCoded(asset.getId(), 0, bytes);
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

    public Document createDocument(Asset asset) {
        Document doc = new Document();

        BaseOrg owner = asset.getOwner();

        addIdField(doc, AssetIndexField.ID.getField(), asset);

        addIdField(doc, AssetIndexField.SECONDARY_ID.getField(), owner.getSecondaryOrg());
        addIdField(doc, AssetIndexField.CUSTOMER_ID.getField(), owner.getCustomerOrg());
        addIdField(doc, AssetIndexField.DIVISION_ID.getField(), owner.getDivisionOrg());

        addField(doc, AssetIndexField.CREATED, asset.getCreated());
        addField(doc, AssetIndexField.MODIFIED, asset.getModified());
        addField(doc, AssetIndexField.IDENTIFIER, asset.getIdentifier());
        addField(doc, AssetIndexField.RFID, asset.getRfidNumber());
        addField(doc, AssetIndexField.REFERENCE_NUMBER, asset.getCustomerRefNumber());
        addField(doc, AssetIndexField.PURCHASE_ORDER, asset.getPurchaseOrder());
        addField(doc, AssetIndexField.COMMENTS, asset.getComments());
        addField(doc, AssetIndexField.IDENTIFIED, asset.getIdentified());
        addField(doc, AssetIndexField.ORDER, asset.getOrderNumber());
        addField(doc, AssetIndexField.LAST_EVENT_DATE, asset.getLastEventDate());
        addField(doc, AssetIndexField.LOCATION, asset.getAdvancedLocation().getFullName());

        addUserField(doc, AssetIndexField.CREATED_BY.getField(), asset.getCreatedBy());
        addUserField(doc, AssetIndexField.MODIFIED_BY.getField(), asset.getModifiedBy());
        addUserField(doc, AssetIndexField.IDENTIFIED_BY.getField(), asset.getIdentifiedBy());
        addUserField(doc, AssetIndexField.ASSIGNED.getField(), asset.getAssignedUser());

        addNamedEntity(doc, AssetIndexField.OWNER.getField(), owner);
        addNamedEntity(doc, AssetIndexField.INTERNAL_ORG.getField(), owner.getInternalOrg());
        addNamedEntity(doc, AssetIndexField.CUSTOMER.getField(), owner.getCustomerOrg());
        addNamedEntity(doc, AssetIndexField.DIVISION.getField(), owner.getDivisionOrg());
        addNamedEntity(doc, AssetIndexField.TYPE.getField(), asset.getType());
        addNamedEntity(doc, AssetIndexField.TYPE_GROUP.getField(), asset.getType().getGroup());
        addNamedEntity(doc, AssetIndexField.STATUS.getField(), asset.getAssetStatus());

        for (InfoOptionBean infoOption : asset.getInfoOptions()) {
            if (!infoOption.getInfoField().isRetired()) {
                addField(doc, infoOption.getInfoField().getName(), parseInfoOptionValue(infoOption));
            }
        }

        // CAVEAT : always do this LAST!! after all other fields have been added 'cause it depends on existence of doc's fields.
        addAllField(doc);

        return doc;
    }

    private void addAllField(Document doc) {
        ArrayList<String> fields = Lists.newArrayList(Iterables.transform(doc.getFields(), new Function<IndexableField, String>() {
            @Override
            public String apply(IndexableField input) {
                return input.stringValue();
            }
        }));
        addField(doc, AssetIndexField.ALL, Joiner.on(" ").join(fields));
    }

    private Object parseInfoOptionValue(InfoOptionBean infoOption) {
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
			// attempt to parse the string into a more specific type (Long then Double otherwise String)
			try {
				value = Long.parseLong(strValue);
			} catch (NumberFormatException e1) {
				try {
					value = Double.parseDouble(strValue);
				} catch (NumberFormatException e2) {
					// fall back to String
					value = strValue;
				}
			}
		}
		return value;
	}

	private void addIdField(Document doc, String name, BaseEntity baseEntity) {
		if (baseEntity != null) {
			addField(doc, AssetIndexField.fromString(name), baseEntity.getId());
		}
	}

	private void addNamedEntity(Document doc, String name, NamedEntity namedEntity) {
		if (namedEntity != null) {
			addField(doc, AssetIndexField.fromString(name), namedEntity.getName());
		}
	}

	private void addUserField(Document doc, String name, User user) {
		if (user != null) {
			addField(doc, AssetIndexField.fromString(name), user.getDisplayName());
		}
	}

    private void addField(Document doc, String name, Object value) {
        addField(doc, name, value, DEFAULT_BOOST);
    }

    private void addField(Document doc, String name, Object value, int boost) {
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
                // TODO DD : may just want to index all numeric fields as Double.  (except internal ID's).   for example length>1 still searches for > 1.0
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

    private void addField(Document doc, AssetIndexField assetIndexField, Object value) {
        addField(doc, assetIndexField.getField(), value, assetIndexField.getBoost());
	}

    public String getIndexPath(Tenant tenant) {
        return String.format(INDEX_FORMAT,tenant.getName());
    }
}
