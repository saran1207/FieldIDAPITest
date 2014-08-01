package com.n4systems.fieldid.service.asset;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;

@Transactional
public class AssetStatusService extends FieldIdPersistenceService {

    public List<AssetStatus> getActiveStatuses() {
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

		builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    /**
     * This method returns the count of ACTIVE AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ACTIVE Asset Statuses.
     */
    public Long getActiveStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);

        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        return persistenceService.count(query);
    }

    /**
     * This method returns the count of ARCHIVED AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ARCHIVED Asset Statuses.
     */
    public Long getArchivedStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);

        query.addSimpleWhere("state", Archivable.EntityState.ARCHIVED);

        return persistenceService.count(query);
    }
    
    public AssetStatus getStatusByName(String name) { 
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        builder.addSimpleWhere("name", name);

        return persistenceService.find(builder);
    }    

}
