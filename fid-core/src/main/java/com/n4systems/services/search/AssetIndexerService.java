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
import com.n4systems.services.search.writer.AssetIndexWriter;
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
				logger.warn("ProcessIndexQueue: Failed for " + item.getType() + ":" + item.getId(), e);
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
				reindexByAssetTypeGroup(persistenceService.findNonSecure(AssetTypeGroup.class, item.getId()));
				break;
			case ASSETSTATUS:
				reindexByAssetStatus(persistenceService.findNonSecure(AssetStatus.class, item.getId()));
				break;
			case TENANT:
				reindexByTenant(persistenceService.findNonSecure(Tenant.class, item.getId()));
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

}
