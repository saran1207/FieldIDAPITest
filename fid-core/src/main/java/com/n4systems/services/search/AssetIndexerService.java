package com.n4systems.services.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Transactional
public class AssetIndexerService extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AssetIndexerService.class);

    @Autowired private AssetIndexWriter assetIndexWriter;

    private @Autowired ConfigService configService;
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

        indexTenantsPerAssets(tenant);
	}

    private void indexTenantsPerAssets(Tenant tenant) {
        // I believe we need to include archived here, but maybe not?
        // the triggers for sure will dump records with archived assets into the queue table
        TenantOnlySecurityFilter filter = new TenantOnlySecurityFilter(tenant).setShowArchived(true);
        QueryBuilder<Asset> assetQueryBuilder = new QueryBuilder<Asset>(Asset.class, filter);

        Long count = persistenceService.count(assetQueryBuilder);
        int numPages = (int) Math.ceil(count / PAGE_SIZE_FOR_TENANT_INSERTING);

        for (int i = 0; i < numPages; i++) {
            List<Asset> assets = persistenceService.findAll(assetQueryBuilder, i, PAGE_SIZE_FOR_TENANT_INSERTING);

            for (Asset asset : assets) {
                if (!creationIndexItemAlreadyExists(asset.getId())) {
                    IndexQueueItem item = new IndexQueueItem();
                    item.setTenant(tenant);
                    item.setType(IndexQueueItem.IndexQueueItemType.ASSET_INSERT);
                    item.setItemId(asset.getId());
                    persistenceService.save(item);
                }
            }

            persistenceService.clearSession();
        }

    }

    private boolean creationIndexItemAlreadyExists(Long assetId) {
        QueryBuilder<IndexQueueItem> existsBuilder = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class, new OpenSecurityFilter());

        existsBuilder.addSimpleWhere("type", IndexQueueItem.IndexQueueItemType.ASSET_INSERT);
        existsBuilder.addSimpleWhere("id", assetId);
        return persistenceService.exists(existsBuilder);
    }

    /* pkg protected for testing purposes */
    IndexQueueItem createNewIndexQueueItem() {
        return new IndexQueueItem();
    }

}
