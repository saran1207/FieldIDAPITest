package com.n4systems.fieldid.service.schedule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class AssetTypeScheduleService extends FieldIdPersistenceService {

    public List<AssetTypeSchedule> getAssetTypeSchedules(Long eventTypeId, Long assetTypeId) {
		QueryBuilder<AssetTypeSchedule> builder = createTenantSecurityBuilder(AssetTypeSchedule.class);

		if (eventTypeId != null) {
			builder.addSimpleWhere("eventType.id", eventTypeId);
		}

		if (assetTypeId != null) {
			builder.addSimpleWhere("assetType.id", assetTypeId);
		}

		return persistenceService.findAll(builder);
    }

}
