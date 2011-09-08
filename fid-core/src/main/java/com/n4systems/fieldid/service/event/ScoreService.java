package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ScoreGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class ScoreService extends FieldIdPersistenceService {

    public List<ScoreGroup> getScoreGroups() {
        return persistenceService.findAll(createTenantSecurityBuilder(ScoreGroup.class));
    }

    public void saveScoreGroup(ScoreGroup scoreGroup) {
        persistenceService.save(scoreGroup);
    }

}
