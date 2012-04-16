package com.n4systems.fieldid.service.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;

@Transactional
public class ScoreService extends FieldIdPersistenceService {

    public List<ScoreGroup> getScoreGroups() {
        return persistenceService.findAll(createTenantSecurityBuilder(ScoreGroup.class));
    }

    public void saveScoreGroup(ScoreGroup scoreGroup) {
        persistenceService.save(scoreGroup);
    }
    
    public void updateScoreGroup(ScoreGroup scoreGroup) {
    	persistenceService.update(scoreGroup);
    	
    	for (EventType eventType: findEventTypesByScoreGroup(scoreGroup)) {
    		eventType.touch();
    		persistenceService.update(eventType);
    	}
    }
    
    public void updateScore(ScoreGroup scoreGroup, Score score) {
    	persistenceService.update(score);
    	updateScoreGroup(scoreGroup);
    }

    public void addScore(ScoreGroup scoreGroup, Score score) {
        persistenceService.save(score);
        scoreGroup.getScores().add(score);
        updateScoreGroup(scoreGroup);
    }

    public void archiveScore(ScoreGroup scoreGroup, Score score) {
        scoreGroup.getScores().remove(score);
        persistenceService.archive(score);
        updateScoreGroup(scoreGroup);
    }
    
    private List<EventType> findEventTypesByScoreGroup(ScoreGroup scoreGroup) {
    	//TODO Replace this query to findEventTypesByScoreGroup.
    	//Right now based on the class designs for EventType -> EventForm -> ........ -> ScoreGroup, we had to do this.
    	//Created to fix the issue IOS-277.
    	String query = "SELECT DISTINCT et FROM " + EventType.class.getName() + " et, IN(et.eventForm.sections) sec, IN(sec.criteria) crit WHERE et.tenant.id = :tenantId AND crit.scoreGroup.id = :scoreGroupId";
		
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("tenantId", securityContext.getTenantSecurityFilter().getTenantId());
    	params.put("scoreGroupId", scoreGroup.getId());
    	List<EventType> eventTypes = (List<EventType>) persistenceService.runQuery(query, params);
    	return eventTypes;
    }

}
