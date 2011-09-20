package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Score;
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

    public void addScore(ScoreGroup scoreGroup, Score score) {
        persistenceService.save(score);
        scoreGroup.getScores().add(score);
        persistenceService.update(scoreGroup);
    }

    public void archiveScore(ScoreGroup scoreGroup, Score score) {
        scoreGroup.getScores().remove(score);
        persistenceService.archive(score);
        persistenceService.update(scoreGroup);
    }

}
