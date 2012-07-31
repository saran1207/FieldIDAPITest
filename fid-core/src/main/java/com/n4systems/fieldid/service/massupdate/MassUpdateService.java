package com.n4systems.fieldid.service.massupdate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.util.EventRemovalSummary;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;

public class MassUpdateService extends FieldIdPersistenceService {
	
	@Autowired private AssetService assetService;

    @Autowired private EventService eventService;
	
	private Logger logger = Logger.getLogger(MassUpdateService.class);


    // TODO: This doesn't seem mass updaty, move to asset service?
	public AssetRemovalSummary testArchive(Asset asset) {
		AssetRemovalSummary summary = new AssetRemovalSummary(asset);
		try {
			QueryBuilder<Event> eventCount = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
			eventCount.setCountSelect().addSimpleWhere("asset", asset).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setEventsToDelete(persistenceService.count(eventCount));

			QueryBuilder<EventSchedule> scheduleCount = new QueryBuilder<EventSchedule>(EventSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("asset", asset);
			summary.setSchedulesToDelete(persistenceService.count(scheduleCount));

			String subEventQuery = "select count(event) From " + Event.class.getName() + " event, IN( event.subEvents ) subEvent WHERE subEvent.asset = :asset AND event.state = :activeState ";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("asset", asset);
			params.put("activeState", EntityState.ACTIVE);
			Query subEventCount = persistenceService.createQuery(subEventQuery, params);
			summary.setAssetUsedInMasterEvent((Long) subEventCount.getSingleResult());
			asset = assetService.fillInSubAssetsOnAsset(asset);
			summary.setSubAssetsToDetach((long) asset.getSubAssets().size());

			summary.setDetachFromMaster(assetService.parentAsset(asset) != null);

			String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s = :asset";
			Query partOfProjectCount = persistenceService.createQuery(partOfProjectQuery, Collections.singletonMap("asset", (Object)asset));
			summary.setProjectToDetachFrom((Long) partOfProjectCount.getSingleResult());

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

    public EventRemovalSummary calculateEventRemovalSummary(List<Long> ids) {
        EventRemovalSummary removalSummary = new EventRemovalSummary();
        for (Long id : ids) {
            Event event = persistenceService.find(Event.class, id);
            if (event != null) {
                if (event.getType().isMaster()) {
                    removalSummary.addMasterEventToDelete();
                    removalSummary.addStandardEventsToDelete(event.getSubEvents().size());
                } else {
                    removalSummary.addStandardEventsToDelete();
                }
                removalSummary.addEventSchedulesToDelete((event.getSchedule() == null) ? 0 : 1);
            }
        }
        return removalSummary;
    }
}
