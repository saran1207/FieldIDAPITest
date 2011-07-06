package com.n4systems.fieldid.wicket.util;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.AssetManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.actions.helpers.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.AssetType;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.EventType;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicColumnsConverter {

    public static void updateDynamicEventColumns(PersistenceManager persistenceManager, IModel<List<ColumnMappingGroupView>> dynamicColumnsModel, IModel<EventType> selectedEventTypeModel, IModel<List<EventType>> currentlyAvailableEventTypesModel) {
        EventAttributeDynamicGroupGenerator dynamicGenerator = new EventAttributeDynamicGroupGenerator(persistenceManager);
        EventType selectedEventType = selectedEventTypeModel.getObject();
        Long selectedEventTypeId = selectedEventType == null ? null : selectedEventType.getId();
        List<ColumnMappingGroupView> dynamicColumns = dynamicGenerator.getDynamicGroups(selectedEventTypeId, idSet(currentlyAvailableEventTypesModel.getObject()), FieldIDSession.get().getTenant().getId(), "event_search", FieldIDSession.get().getSessionUser().getSecurityFilter());
        dynamicColumnsModel.setObject(dynamicColumns);
    }

    public static void updateDynamicAssetColumns(AssetManager assetManager, IModel<List<ColumnMappingGroupView>> dynamicColumnsModel, IModel<AssetType> selectedAssetTypeModel, IModel<List<AssetType>> currentlyAvailableAssetTypesModel) {
        InfoFieldDynamicGroupGenerator dynamicGenerator = new InfoFieldDynamicGroupGenerator(new AssetManagerBackedCommonAssetAttributeFinder(assetManager), "event_search", "asset");
        AssetType selectedAssetType = selectedAssetTypeModel.getObject();
        Long selectedAssetTypeId = selectedAssetType == null ? null : selectedAssetType.getId();
        List<ColumnMappingGroupView> dynamicColumns = dynamicGenerator.getDynamicGroups(selectedAssetTypeId, idList(currentlyAvailableAssetTypesModel.getObject()));
        dynamicColumnsModel.setObject(dynamicColumns);
    }

    private static <T extends BaseEntity> List<Long> idList(List<T> items) {
        List<Long> ids = new ArrayList<Long>(items.size());
        for (T item : items) {
            ids.add(item.getId());
        }
        return ids;
    }

    private static <T extends BaseEntity> Set<Long> idSet(List<T> entities) {
        Set<Long> availableEventTypeIds = new HashSet<Long>();
        for (BaseEntity entity : entities) {
            availableEventTypeIds.add(entity.getId());

        }

        return availableEventTypeIds;
    }

}
