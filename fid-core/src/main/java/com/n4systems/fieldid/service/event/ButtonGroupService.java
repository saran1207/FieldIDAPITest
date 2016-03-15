package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.ButtonGroup;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

public class ButtonGroupService extends CrudService<ButtonGroup>{

    private static final Logger logger = Logger.getLogger(ButtonGroupService.class);

    public ButtonGroupService() {
        super(ButtonGroup.class);
    }

    public Boolean isUniqueName(String name) {
        return persistenceService.isUniqueName(ButtonGroup.class, name, null);
    }

    @Override
    @Transactional
    public ButtonGroup update(ButtonGroup buttonGroup) {
        touchEventTypes(buttonGroup);
        return super.update(buttonGroup);
    }

    //When button groups are updated we need to update the event types so that the updated groups get synced
    private void touchEventTypes(ButtonGroup buttonGroup) {
        String updateQuery =
                "UPDATE eventtypes et INNER JOIN eventforms ef ON et.eventform_id = ef.id " +
                "INNER JOIN eventforms_criteriasections efcs on ef.id = efcs.eventform_id " +
                "INNER JOIN criteriasections_criteria csc ON efcs.sections_id = csc.criteriasections_id " +
                "INNER JOIN oneclick_criteria occ ON csc.criteria_id = occ.id " +
                "INNER JOIN button_groups bg ON occ.button_group_id = bg.id " +
                "SET et.modified = NOW() " +
                "WHERE et.state = 'ACTIVE' AND ef.state = 'ACTIVE' AND bg.id = :buttonGroupId";

        Query touchQuery = getEntityManager().createNativeQuery(updateQuery);
        touchQuery.setParameter("buttonGroupId", buttonGroup.getId());

        int rowsUpdated = touchQuery.executeUpdate();

        logger.debug(rowsUpdated + " eventtypes updated.");
    }

    public List<ButtonGroup> getButtonGroups() {
        //Heh... one line.  YEAH BABY!!!!
        return persistenceService.findAll(createUserSecurityBuilder(ButtonGroup.class));
    }

    public Long getButtonGroupCount() {
        return persistenceService.count(createUserSecurityBuilder(ButtonGroup.class));
    }
}
