package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class CommonEventTypesModel extends FieldIDSpringModel<List<ThingEventType>> {

    @SpringBean
    private EventTypeService eventTypeService;

    private List<AssetType> assetTypes;

    public CommonEventTypesModel(List<AssetType> assetTypes) {
        this.assetTypes = assetTypes;
    }

    @Override
    protected List<ThingEventType> load() {
        return eventTypeService.getCommonEventTypesExcludingActions(assetTypes);
    }

}
