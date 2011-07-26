package com.n4systems.fieldid.service.search.columns;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.fieldid.service.search.columns.dynamic.AssetManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.service.search.columns.dynamic.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.service.search.columns.dynamic.InfoFieldDynamicGroupGenerator;
import com.n4systems.model.AssetType;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.EventType;
import com.n4systems.model.search.ColumnMappingGroupView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicColumnsService extends FieldIdService {

    private @Autowired PersistenceManager persistenceManager;
    private @Autowired AssetManager assetManager;

    public List<ColumnMappingGroupView> getDynamicEventColumns(EventType selectedEventType, List<EventType> currentlyAvailableEventTypes) {
        EventAttributeDynamicGroupGenerator dynamicGenerator = new EventAttributeDynamicGroupGenerator(persistenceManager);
        Long selectedEventTypeId = selectedEventType == null ? null : selectedEventType.getId();
        return dynamicGenerator.getDynamicGroups(selectedEventTypeId, idSet(currentlyAvailableEventTypes), securityContext.getUserSecurityFilter().getTenantId(), "event_search", securityContext.getUserSecurityFilter());
    }

    public List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType selectedAssetType, List<AssetType> currentlyAvailableAssetTypes) {
        InfoFieldDynamicGroupGenerator dynamicGenerator = new InfoFieldDynamicGroupGenerator(new AssetManagerBackedCommonAssetAttributeFinder(assetManager), "event_search", "asset");
        Long selectedAssetTypeId = selectedAssetType == null ? null : selectedAssetType.getId();
        return dynamicGenerator.getDynamicGroups(selectedAssetTypeId, idList(currentlyAvailableAssetTypes));
    }

    private static <T extends BaseEntity> List<Long> idList(List<T> items) {
        List<Long> ids = new ArrayList<Long>(items.size());
        for (T item : items) {
            ids.add(item.getId());
        }
        return ids;
    }

    private static <T extends BaseEntity> Set<Long> idSet(List<T> entities) {
        Set<Long> ids = new HashSet<Long>();
        for (BaseEntity entity : entities) {
            ids.add(entity.getId());
        }
        return ids;
    }

}
