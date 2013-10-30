package com.n4systems.services.search;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.ConfigService;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.search.writer.AssetIndexWriter;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.testutils.QueryBuilderMatcher;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.easymock.EasyMock.*;

public class AssetIndexerServiceTest extends FieldIdServiceTest {
    
    private @TestTarget AssetIndexerService assetIndexerService;

    private @TestMock PersistenceService persistenceService;
    private @TestMock ConfigService configService;
    private @TestMock AssetIndexWriter assetIndexWriter;
    private @TestMock SecurityContext securityContext;

    private Asset asset;
    private QueryBuilder<IndexQueueItem> query;
    private IndexQueueItem indexQueueItem;
    private boolean tenantQueueItemExists = false;

    @Override
    protected Object createSut(Field sutField) throws Exception {
        return new AssetIndexerService() {
            @Override IndexQueueItem createNewIndexQueueItem() {
                return indexQueueItem;
            }
        };

    }

    @Override
    public void setUp() {
        super.setUp();
        indexQueueItem = new IndexQueueItem();
    }

    @Test
    public void testProcessIndexQueue_asset_insert() throws Exception {
        expectingProcessIndexQueueItem(IndexQueueItem.IndexQueueItemType.ASSET_INSERT);

        List<Asset> assets = Lists.newArrayList(asset);
        assetIndexWriter.index(asset.getTenant(), assets, false);
        replay(assetIndexWriter);

        assetIndexerService.processIndexQueue();

        verifyTestMocks();
    }

    @Test
    public void testProcessIndexQueue_asset_update() throws Exception {
        expectingProcessIndexQueueItem(IndexQueueItem.IndexQueueItemType.ASSET_UPDATE);

        List<Asset> assets = Lists.newArrayList(asset);
        assetIndexWriter.index(asset.getTenant(), assets, true);
        replay(assetIndexWriter);

        assetIndexerService.processIndexQueue();

        verifyTestMocks();
    }

    private void expectingProcessIndexQueueItem(IndexQueueItem.IndexQueueItemType itemType) {
        AssetType type = AssetTypeBuilder.anAssetType().named("type").build();
        AssetStatus status = AssetStatusBuilder.anAssetStatus().named("status").build();
        Tenant tenant = TenantBuilder.n4();
        BaseOrg owner = OrgBuilder.aPrimaryOrg().withName("org").build();
        asset = AssetBuilder.anAsset().
                ofType(type).
                havingStatus(status).
                inFreeformLocation("location").
                referenceNumber("ref").
                rfidNumber("rfid").
                withOwner(owner).
                forTenant(tenant).build();


        Long id = 123L;
        IndexQueueItem item = new IndexQueueItem();
        item.setType(itemType);
        item.setItemId(id);

        query = new QueryBuilder<IndexQueueItem>(IndexQueueItem.class, new OpenSecurityFilter());

        expect(configService.getBoolean(ConfigEntry.ASSET_INDEX_ENABLED)).andReturn(true);
        expect(configService.getInteger(ConfigEntry.ASSET_INDEX_SIZE)).andReturn(50);

        List<IndexQueueItem> items = Lists.newArrayList(item);
        expect(persistenceService.findAll(anyObject(QueryBuilder.class))).andReturn(items);
        expect(persistenceService.findNonSecure(Asset.class,id)).andReturn(asset);
        persistenceService.deleteAny(item);
        replay(persistenceService, configService);
    }

}
