package com.n4systems.services.safetyNetwork.catalog;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

import java.util.*;

public class CatalogScoreGroupImportHandler extends CatalogImportHandler {

    private Set<Long> eventTypeIds;
    private List<ScoreGroup> scoreGroups;
    private User importUser;
    private Map<Long, ScoreGroup> mappedScoreGroups = new HashMap<Long, ScoreGroup>();

    public CatalogScoreGroupImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
        super(persistenceManager, tenant, importCatalog);
    }

    @Override
    public void importCatalog() throws ImportFailureException {
        Set<ScoreGroup> scoreGroupSet = new HashSet<ScoreGroup>();
        for (Long eventTypeId : eventTypeIds) {
            EventType type = persistenceManager.find(EventType.class, eventTypeId, "eventForm.sections");
            if (type.getEventForm() != null) {
                for (CriteriaSection criteriaSection : type.getEventForm().getAvailableSections()) {
                    for (Criteria criteria : criteriaSection.getAvailableCriteria()) {
                        if (criteria instanceof ScoreCriteria) {
                            scoreGroupSet.add(((ScoreCriteria) criteria).getScoreGroup());
                        }
                    }
                }
            }
        }
        scoreGroups = new ArrayList<ScoreGroup>(scoreGroupSet);
        for (ScoreGroup scoreGroup : scoreGroups) {
            importScoreGroup(scoreGroup);
        }
    }

    private void importScoreGroup(ScoreGroup scoreGroup) {
        scoreGroup = persistenceManager.find(ScoreGroup.class, scoreGroup.getId(), "scores");
        Long originalId = scoreGroup.getId();
//        scoreGroup = cloneItem(scoreGroup);
        scoreGroup.reset();
        scoreGroup.setTenant(tenant);
        scoreGroup.setCreatedBy(importUser);
        scoreGroup.setCreated(new Date());

        List<Score> copiedScores = new ArrayList<Score>();
        for (Score score : scoreGroup.getScores()) {
            score.reset();
            score.setTenant(tenant);
            score.setCreatedBy(importUser);
            score.setCreated(new Date());
            copiedScores.add(score);
        }

//        scoreGroup.getScores().clear();
        scoreGroup.setScores(copiedScores);

//        persistenceManager.reattchAndFetch(scoreGroup.getScores());
        mappedScoreGroups.put(originalId, scoreGroup);
//        scoreGroup.setScores(scores);
        persistenceManager.save(scoreGroup);
        System.out.println("the new score group has an id of : " + scoreGroup.getId());
    }

    private <T extends BaseEntity> T cloneItem(T item) {
        try {
            return (T) item.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void rollback() {
        for (ScoreGroup scoreGroup : scoreGroups) {
            persistenceManager.delete(scoreGroup);
        }
    }

    public CatalogScoreGroupImportHandler setImportUser(User user) {
        this.importUser = user;
        return this;
    }

    public CatalogScoreGroupImportHandler setEventTypIds(Set<Long> eventTypeIds) {
        this.eventTypeIds = eventTypeIds;
        return this;
    }

    public Map<Long, ScoreGroup> getMappedScoreGroups() {
        return mappedScoreGroups;
    }

}
