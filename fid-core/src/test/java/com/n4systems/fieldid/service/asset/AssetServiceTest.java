package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.reporting.WorkSummaryRecord;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import com.n4systems.util.persistence.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

public class AssetServiceTest extends FieldIdServiceTest {

    private @TestTarget AssetService assetService;
    private @TestMock SecurityContext securityContext;
    private @TestMock PersistenceService persistenceService;

    private UserSecurityFilter securityFilter;
    private User user;

    private List<Asset> assetList;

    @Override
    @Before
    public void setUp() {
        super.setUp();

        user = UserBuilder.aFullUser().build();
        securityFilter = new UserSecurityFilter(user);
        assetList = new ArrayList<Asset>();

        expect(securityContext.getUserSecurityFilter()).andReturn(securityFilter);
        replay(securityContext);

        long listCount = assetList.size();

        expect(persistenceService.findAll((QueryBuilder<Asset>) anyObject(QueryBuilder.class))).andReturn(assetList);
        expect(persistenceService.count((QueryBuilder<Asset>) anyObject(QueryBuilder.class))).andReturn(listCount);
        expect(persistenceService.findAllPaginated((QueryBuilder<Asset>) anyObject(QueryBuilder.class), anyInt(), anyInt())).andReturn(assetList);
        replay(persistenceService);
    }

    @Test
    public void test_findExactAssetByIdentifiersForNewSmartSearch() {
        String term = "search";

        List<Asset> res = assetService.findExactAssetByIdentifiersForNewSmartSearch(term);
        assertEquals(res, assetList);
    }

    @Test
    public void test_findExactAssetSizeByIdentifiersForNewSmartSearch() {
        String term = "search";

        int result = assetService.findExactAssetSizeByIdentifiersForNewSmartSearch(term);
        assertEquals(result, assetList.size());
    }

    @Test
    public void test_findExactAssetByIdentifierSmartSearchAndAssetType() {
        String term = "term";
        Long assetTypeId = 1L;
        Long excludeAssetId = 0L;
        int first = 0;
        int pageSize = 1;

        List<Asset> res = assetService.findExactAssetByIdentifierSmartSearchAndAssetType(term, assetTypeId, excludeAssetId, first, pageSize);
        assertEquals(res, assetList);
    }

    @Test
    public void test_findExactAssetSizeByIdentifierSmartSearchAndAssetType() {
        String term = "term";
        Long assetTypeId = 1L;
        Long excludeAssetId = 0L;

        int result = assetService.findExactAssetSizeByIdentifierSmartSearchAndAssetType(term, assetTypeId, excludeAssetId);
        assertEquals(result, assetList.size());
    }
}
