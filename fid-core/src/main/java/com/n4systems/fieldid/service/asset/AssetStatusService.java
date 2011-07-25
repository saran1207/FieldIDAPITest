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

}
