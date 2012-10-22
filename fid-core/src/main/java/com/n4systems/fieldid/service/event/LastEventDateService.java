package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.util.Date;

public class LastEventDateService extends FieldIdPersistenceService {
    
    private static final Logger logger = Logger.getLogger(LastEventDateService.class);

    public Date findLastEventDate(Asset asset) {
        return findLastEventDate(asset, null);
    }


    public Date findLastEventDate(Asset asset, EventType eventType) {

        QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Event.class, new OpenSecurityFilter(), "i");

        qBuilder.setMaxSelect("completedDate");
        qBuilder.addSimpleWhere("asset.id", asset.getId());
        qBuilder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        qBuilder.addSimpleWhere("eventState", Event.EventState.COMPLETED);

        if (eventType != null) {
            qBuilder.addSimpleWhere("type.id", eventType.getId());
        }

        Date lastEventDate = null;
        try {
            lastEventDate = persistenceService.find(qBuilder);
        } catch (InvalidQueryException e) {
            logger.error("Unable to find last event date", e);
        } catch (Exception e) {
            logger.error("Unable to find last event date", e);
        }

        return lastEventDate;
    }

}
