package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class CommonEventTypesModel extends FieldIDSpringModel<List<EventType>> {

    @SpringBean
    private EventTypeService eventTypeService;

    private List<AssetType> assetTypes;

    public CommonEventTypesModel(List<AssetType> assetTypes) {
        this.assetTypes = assetTypes;
    }

    @Override
    protected List<EventType> load() {
        return eventTypeService.getCommonEventTypes(assetTypes);
    }

}
