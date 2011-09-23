package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.eventform.CriteriaSectionCopyUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EventFormService extends FieldIdPersistenceService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveNewEventForm(Long eventTypeId, List<CriteriaSection> newCriteriaSections) {
        EventType eventType = persistenceService.find(EventType.class, eventTypeId);

        EventForm eventForm = new EventForm();
        eventForm.setTenant(persistenceService.findNonSecure(Tenant.class, securityContext.getUserSecurityFilter().getTenantId()));
        eventForm.setSections(createCopiesOf(newCriteriaSections));

        EventForm oldEventForm = eventType.getEventForm();
        if (oldEventForm != null) {
            oldEventForm.setState(Archivable.EntityState.RETIRED);
            eventForm.setScoreCalculationType(oldEventForm.getScoreCalculationType());
            eventForm.setFailRange(oldEventForm.getFailRange());
            eventForm.setPassRange(oldEventForm.getPassRange());
            eventForm.setUseScoreForResult(oldEventForm.isUseScoreForResult());
            persistenceService.update(oldEventForm);
        }

        eventType.setEventForm(eventForm);
        eventType.incrementFormVersion();

        persistenceService.update(eventForm);
        persistenceService.update(eventType);
    }

    private List<CriteriaSection> createCopiesOf(List<CriteriaSection> criteriaSections) {
        CriteriaSectionCopyUtil copyUtil = new CriteriaSectionCopyUtil();
        List<CriteriaSection> copiedSections = new ArrayList<CriteriaSection>();
        for (CriteriaSection section : criteriaSections) {
            CriteriaSection criteriaSection = copyUtil.copySection(section);
            filterRetiredCriteria(criteriaSection);
            copiedSections.add(criteriaSection);
        }
        return copiedSections;
    }

    private void filterRetiredCriteria(CriteriaSection criteriaSection) {
        List<Criteria> activeCriteria = new ArrayList<Criteria>();
        for (Criteria criteria : criteriaSection.getCriteria()) {
            if (!criteria.isRetired()) {
                activeCriteria.add(criteria);
            }
        }
        criteriaSection.setCriteria(activeCriteria);
    }

}
