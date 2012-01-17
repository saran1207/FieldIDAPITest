package com.n4systems.fieldid.service.massupdate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.ejb.AssetManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;

public class MassUpdateService extends FieldIdPersistenceService {
	
	@Autowired private AssetManager assetManager;
	@Autowired private AssetService assetService;
	
	private Logger logger = Logger.getLogger(MassUpdateService.class);
	    
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
		
	public Long deleteAssets(List<Long> ids, User modifiedBy) throws UpdateFailureException {
		Long result = 0L;

		try {
			for (Long id : ids) {
				Asset asset = assetManager.findAssetAllFields(id, new OpenSecurityFilter());
				result++;
				assetManager.archive(asset, modifiedBy);
			}
		} catch (Exception e) {
			throw new UpdateFailureException(e);
		}

		return result;
	}
	
	

}
