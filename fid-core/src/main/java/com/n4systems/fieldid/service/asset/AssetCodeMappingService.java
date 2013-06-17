package com.n4systems.fieldid.service.asset;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import rfid.ejb.entity.AssetCodeMapping;

public class AssetCodeMappingService extends FieldIdPersistenceService {

    private Logger logger = Logger.getLogger(AssetCodeMappingService.class);

    public AssetCodeMapping getAssetCodeByAssetCodeAndTenant(String assetCode) {
        QueryBuilder<AssetCodeMapping> query = createTenantSecurityBuilder(AssetCodeMapping.class);
        query.addSimpleWhere("assetCode", assetCode);

        query.addPostFetchPaths("assetInfo.infoFields");

        AssetCodeMapping assetMapping = null;
        try {
            assetMapping = persistenceService.find(query);
        } catch(InvalidQueryException e) {
            logger.error("Failed loading AssetCodeMapping", e);
        }

        // if we were unable to find an asset code mapping, return the default
        if(assetMapping == null) {
            assetMapping = getDefaultMapping();
        }

        return assetMapping;
    }

    private AssetCodeMapping getDefaultMapping() {
        AssetCodeMapping defaultMapping = new AssetCodeMapping();
        defaultMapping.setAssetInfo(defaultAssetType());
        return defaultMapping;
    }

    private AssetType defaultAssetType() {
        // find the default asset type name for this tenant
        String defaultTypeName = ConfigContext.getCurrentContext().getString(ConfigEntry.DEFAULT_PRODUCT_TYPE_NAME, getCurrentTenant().getId());

        QueryBuilder<AssetType> builder = createTenantSecurityBuilder(AssetType.class);

        builder.addSimpleWhere("name", defaultTypeName);

        AssetType type = null;
        try {
            type = persistenceService.find(builder);
        } catch(InvalidQueryException e) {
            logger.error("Failed finding default AssetType", e);
        }

        return type;
    }
}
