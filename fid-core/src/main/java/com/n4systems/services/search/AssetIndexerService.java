package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationSearchTerm;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownWithPrimaryFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.ConfigService;
import com.n4systems.services.search.writer.AssetIndexWriter;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Transactional
public class AssetIndexerService extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AssetIndexerService.class);

    @Autowired private AssetIndexWriter assetIndexWriter;

    private @Autowired ConfigService configService;
    private @Autowired AsyncService asyncService;
    private @Resource PlatformTransactionManager transactionManager;

    private static final int PAGE_SIZE_FOR_TENANT_INSERTING = 512;

    @Scheduled(fixedDelay = 2000)
	public void processIndexQueue() {
        if (!configService.getBoolean(ConfigEntry.ASSET_INDEX_ENABLED)) {
            logger.info(getClass().getSimpleName() +": Disabled");
            return;
        }
        long startTime = System.currentTimeMillis();
		logger.info(getClass().getSimpleName() +": Running");

        QueryBuilder<IndexQueueItem> query = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class);
        query.setLimit(configService.getInteger(ConfigEntry.ASSET_INDEX_SIZE));

        List<IndexQueueItem> items = persistenceService.findAll(query);
		logger.info(getClass().getSimpleName() + " queue length = " + items.size());
		for (IndexQueueItem item : items) {
			try {
				processIndexQueueItem(item);
				persistenceService.deleteAny(item);
			} catch (Exception e) {
				logger.warn(getClass().getSimpleName() +": Failed for " + item.getType() + ":" + item.getItemId(), e);
			}
		}
		logger.info(getClass().getSimpleName() +": Completed " + (System.currentTimeMillis() - startTime) + "ms");
	}

	private void processIndexQueueItem(IndexQueueItem item) {
		switch (item.getType()) {
			case ASSET_INSERT:
				indexAsset(persistenceService.findNonSecure(Asset.class, item.getItemId()));
				break;
			case ASSET_UPDATE:
				reindexAsset(persistenceService.findNonSecure(Asset.class, item.getItemId()));
				break;
			case USER:
				reindexAssetsByUser(persistenceService.findNonSecure(User.class, item.getItemId()));
				break;
			case ORG:
				reindexAssetsByOrg(persistenceService.findNonSecure(BaseOrg.class, item.getItemId()));
				break;
			case ORDER:
				reindexAssetsByOrder(persistenceService.findNonSecure(Order.class, item.getItemId()));
				break;
			case PREDEFINEDLOCATION:
				reindexAssetsByPredefinedLocation(persistenceService.findNonSecure(PredefinedLocation.class, item.getItemId()));
				break;
			case ASSETTYPE:
				reindexAssetsByAssetType(persistenceService.findNonSecure(AssetType.class, item.getItemId()));
				break;
			case ASSETTYPEGROUP:
				reindexByAssetTypeGroup(persistenceService.findNonSecure(AssetTypeGroup.class, item.getItemId()));
				break;
			case ASSETSTATUS:
				reindexByAssetStatus(persistenceService.findNonSecure(AssetStatus.class, item.getItemId()));
				break;
			case TENANT:
				reindexByTenant(persistenceService.findNonSecure(Tenant.class, item.getItemId()));
				break;
			default:
				throw new IllegalArgumentException("Unhandled type: " + item.getType());
		}
	}

    private TenantOnlySecurityFilter createTenantFilter(HasTenant hasTenantEntity) {
		return new TenantOnlySecurityFilter(hasTenantEntity.getTenant());
	}

	private void indexAsset(Asset asset) {
        assetIndexWriter.index(asset.getTenant(), Arrays.asList(asset), false);
	}

	private void reindexAsset(Asset asset) {
        assetIndexWriter.index(asset.getTenant(), Arrays.asList(asset), true);
	}

	private void reindexAssetsByUser(User user) {
        assetIndexWriter.reindexItems(user.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(user))
                .addWhere(WhereClauseFactory.group("user_params",
                        WhereClauseFactory.create("createdBy", user, WhereClause.ChainOp.OR),
                        WhereClauseFactory.create("modifiedBy", user, WhereClause.ChainOp.OR),
                        WhereClauseFactory.create("identifiedBy", user, WhereClause.ChainOp.OR),
                        WhereClauseFactory.create("assignedUser", user, WhereClause.ChainOp.OR)
                )));
	}

	private void reindexAssetsByOrg(BaseOrg org) {
        assetIndexWriter.reindexItems(org.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(org))
                .applyFilter(new OwnerAndDownWithPrimaryFilter(org)));
	}

	private void reindexAssetsByOrder(Order order) {
        assetIndexWriter.reindexItems(order.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(order))
                .addWhere(WhereClauseFactory.create("shopOrder.order", order)));
	}

	private void reindexAssetsByPredefinedLocation(PredefinedLocation predefinedLocation) {
        assetIndexWriter.reindexItems(predefinedLocation.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(predefinedLocation))
                .addJoin(new JoinClause(JoinClause.JoinType.LEFT, "advancedLocation.predefinedLocation.searchIds", "preLocSearchId", true))
                .addWhere(new PredefinedLocationSearchTerm("preLocSearchId", predefinedLocation.getId())));
	}

	private void reindexAssetsByAssetType(AssetType assetType) {
        assetIndexWriter.reindexItems(assetType.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetType))
                .addWhere(WhereClauseFactory.create("type", assetType)));
	}

	private void reindexByAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        assetIndexWriter.reindexItems(assetTypeGroup.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetTypeGroup))
                .addWhere(WhereClauseFactory.create("type.group", assetTypeGroup)));
	}

	private void reindexByAssetStatus(AssetStatus assetStatus) {
        assetIndexWriter.reindexItems(assetStatus.getTenant(), new QueryBuilder<Asset>(Asset.class, createTenantFilter(assetStatus))
                .addWhere(WhereClauseFactory.create("assetStatus", assetStatus)));
	}

	private void reindexByTenant(Tenant tenant) {
        assetIndexWriter.reindexItems(tenant, new QueryBuilder<Asset>(Asset.class, new TenantOnlySecurityFilter(tenant)));
	}

    public void indexTenant(String tenantName) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
		builder.addSimpleWhere("name", tenantName);
		Tenant tenant = persistenceService.find(builder);

        asynchronouslyIndexAssets(tenant);
	}

    public long asynchronouslyIndexAssets(final Tenant tenant) {
        TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(tenant).setShowArchived(true);
        final QueryBuilder<Asset> assetQueryBuilder = new QueryBuilder<Asset>(Asset.class, filter);

        final Long count = persistenceService.count(assetQueryBuilder);

        AsyncService.AsyncTask<Void> task = asyncService.createTaskNoUserContext(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                indexTenantsPerAssets(tenant, assetQueryBuilder, count);
                return null;
            }
        });

        asyncService.run(task);

        return count;
    }

    private void indexTenantsPerAssets(Tenant tenant, QueryBuilder<Asset> assetQueryBuilder, Long count) {
        // I believe we need to include archived here, but maybe not?
        // the triggers for sure will dump records with archived assets into the queue table

        int numPages = (int) Math.ceil(count / PAGE_SIZE_FOR_TENANT_INSERTING);

        for (int i = 0; i < numPages; i++) {
            EntityManager em = ((JpaTransactionManager) transactionManager).getEntityManagerFactory().createEntityManager();
            try {
                em.getTransaction().begin();
                Query query = assetQueryBuilder.createQuery(em);
                query.setFirstResult(i*PAGE_SIZE_FOR_TENANT_INSERTING);
                query.setMaxResults(PAGE_SIZE_FOR_TENANT_INSERTING);
                List<Asset> assets = query.getResultList();

                for (Asset asset : assets) {
                    if (!creationIndexItemAlreadyExists(em, asset.getId())) {
                        IndexQueueItem item = new IndexQueueItem();
                        item.setTenant(tenant);
                        item.setType(IndexQueueItem.IndexQueueItemType.ASSET_INSERT);
                        item.setItemId(asset.getId());
                        em.persist(item);
                    }
                }
            } finally {
                em.getTransaction().commit();
                em.close();
            }
        }
    }

    private boolean creationIndexItemAlreadyExists(EntityManager em, Long assetId) {
        QueryBuilder<IndexQueueItem> existsBuilder = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class, new OpenSecurityFilter());

        existsBuilder.addSimpleWhere("type", IndexQueueItem.IndexQueueItemType.ASSET_INSERT);
        existsBuilder.addSimpleWhere("id", assetId);
        return existsBuilder.entityExists(em);
    }

    /* pkg protected for testing purposes */
    IndexQueueItem createNewIndexQueueItem() {
        return new IndexQueueItem();
    }

    // Needs to be done in a separate transaction from reindexTenant
    // since we can't have multiple users kicking off indexes at the same time.
    public boolean checkAndMarkStarted() {
        Tenant tenant = getCurrentTenant();
        boolean previouslyStarted = tenant.isAssetIndexerStarted();
        tenant.setAssetIndexerStarted(true);
        persistenceService.update(tenant);
        return previouslyStarted;
    }

    public void reindexTenant() {
        reindexTenant(getCurrentTenant());
    }

    public void reindexTenant(Tenant tenant) {
        deleteExistingIndexIfExists(tenant);

        asynchronouslyIndexAssets(tenant);
    }

    private void deleteExistingIndexIfExists(Tenant tenant) {
        File file = new File(assetIndexWriter.getIndexPath(tenant));

        if (file.exists()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch(IOException e) {
                logger.error("Could not delete existing index for tenant: " + tenant.getDisplayName());
                throw new RuntimeException("Error deleting existing index for " + tenant.getDisplayName(), e);
            }
        }
    }


    public Long countRemainingIndexItemsForTenant() {
        QueryBuilder<IndexQueueItem> query = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant", getCurrentTenant());
        return persistenceService.count(query);
    }

}
