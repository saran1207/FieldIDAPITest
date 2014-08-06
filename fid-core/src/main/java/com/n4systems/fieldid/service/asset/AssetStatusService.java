package com.n4systems.fieldid.service.asset;

import java.util.Date;
import java.util.List;

import com.n4systems.model.assetstatus.AssetStatusSaver;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;

@Transactional
public class AssetStatusService extends FieldIdPersistenceService {

    public List<AssetStatus> getActiveStatuses() {
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

		builder.addOrder("name")
               .addSimpleWhere("state",
                       Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    public List<AssetStatus> getArchivedStatuses() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class, true);

        query.addOrder("name")
             .addSimpleWhere("state",
                             Archivable.EntityState.ARCHIVED);

        return persistenceService.findAll(query);
    }

    /**
     * This method returns the count of ACTIVE AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ACTIVE Asset Statuses.
     */
    public Long getActiveStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);

        query.addSimpleWhere("state",
                             Archivable.EntityState.ACTIVE);

        return persistenceService.count(query);
    }

    /**
     * This method returns the count of ARCHIVED AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ARCHIVED Asset Statuses.
     */
    public Long getArchivedStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class, true);

        query.addSimpleWhere("state",
                             Archivable.EntityState.ARCHIVED);

        return persistenceService.count(query);
    }

    public AssetStatus update(AssetStatus assetStatus, User modifiedBy) {
        assetStatus.setModifiedBy(modifiedBy);
        assetStatus.setModified(new Date(System.currentTimeMillis()));

        return persistenceService.update(assetStatus);
    }

    public AssetStatus saveAssetStatus(AssetStatus assetStatus, User user) {
        assetStatus.setCreated(new Date(System.currentTimeMillis()));
        assetStatus.setCreatedBy(user);
        assetStatus.setModified(new Date(System.currentTimeMillis()));
        assetStatus.setModifiedBy(user);

        Long id = persistenceService.save(assetStatus);

        return persistenceService.find(AssetStatus.class, id);
    }
    
    public AssetStatus getStatusByName(String name) { 
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        builder.addSimpleWhere("name", name);

        return persistenceService.find(builder);
    }

    public AssetStatus getStatusById(Long id) {
        return persistenceService.find(AssetStatus.class, id);
    }

    public void archiveStatus(AssetStatus assetStatus) {
        assetStatus.archiveEntity();
        persistenceService.update(assetStatus);
    }

    public void unarchiveStatus(AssetStatus assetStatus) {
        assetStatus.activateEntity();
        persistenceService.update(assetStatus);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);
        query.addWhere(WhereClauseFactory.create("name", name));
        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", id));
        }
        return persistenceService.exists(query);
    }
}
