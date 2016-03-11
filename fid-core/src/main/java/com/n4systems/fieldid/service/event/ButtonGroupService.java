package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.EventType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonGroupService extends CrudService<ButtonGroup>{

    public ButtonGroupService() {
        super(ButtonGroup.class);
    }

    public Boolean isUniqueName(String name) {
        return persistenceService.isUniqueName(ButtonGroup.class, name, null);
    }

    public void touchEventTypes(ButtonGroup buttonGroup) {
        String queryString = "SELECT DISTINCT et FROM " + EventType.class.getName() + " et INNER JOIN eventforms ef ON et.eventform_id = ef.id\n" +
                "                                   INNER JOIN eventforms_criteriasections efcs on ef.id = efcs.eventform_id\n" +
                "                                   INNER JOIN criteriasections_criteria csc ON efcs.sections_id = csc.criteriasections_id\n" +
                "                                   INNER JOIN oneclick_criteria occ ON csc.criteria_id = occ.id\n" +
                "                                   INNER JOIN button_groups bg ON occ.button_group_id = bg.id\n" +
                "WHERE et.state = 'ACTIVE' AND ef.state = 'ACTIVE' AND bg.id = :buttonGroupId GROUP BY et.id;";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tenantId", securityContext.getTenantSecurityFilter().getTenantId());
        params.put("buttonGroupId", buttonGroup.getId());

        List<EventType> eventTypes = (List<EventType>) persistenceService.runQuery(queryString, params);

        eventTypes.stream().forEach(et -> et.touch());

        persistenceService.update(eventTypes);
    }
}
