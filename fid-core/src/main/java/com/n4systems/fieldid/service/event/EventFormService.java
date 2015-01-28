package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.localization.Translation;
import com.n4systems.services.localization.LocalizationService;
import com.n4systems.util.eventform.CriteriaSectionCopyUtil;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EventFormService extends FieldIdPersistenceService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LocalizationService localizationService;

    private static final Field criteriaTextField;
    private static final Field recommendationsField;
    private static final Field deficienciesField;
    private static final Field sectionTextField;

    static {
        try {
            criteriaTextField = Criteria.class.getDeclaredField("displayText");
            recommendationsField = Criteria.class.getDeclaredField("recommendations");
            deficienciesField = Criteria.class.getDeclaredField("deficiencies");
            sectionTextField = CriteriaSection.class.getDeclaredField("title");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveNewEventForm(Long eventTypeId, List<CriteriaSection> newCriteriaSections) {
        EventType eventType = persistenceService.find(EventType.class, eventTypeId);

        EventForm eventForm = new EventForm();
        eventForm.setTenant(persistenceService.findNonSecure(Tenant.class, securityContext.getUserSecurityFilter().getTenantId()));
        eventForm.setSections(createCopiesOf(newCriteriaSections));

        EventForm oldEventForm = eventType.getEventForm();
        if (oldEventForm != null) {
            oldEventForm.setState(Archivable.EntityState.RETIRED);
            //Scoring
            eventForm.setScoreCalculationType(oldEventForm.getScoreCalculationType());
            eventForm.setFailRange(oldEventForm.getFailRange());
            eventForm.setPassRange(oldEventForm.getPassRange());
            eventForm.setUseScoreForResult(oldEventForm.isUseScoreForResult());
            //Observations
            eventForm.setObservationcountFailCalculationType(oldEventForm.getObservationcountFailCalculationType());
            eventForm.setObservationcountFailRange(oldEventForm.getObservationcountFailRange());
            eventForm.setObservationcountPassCalculationType(oldEventForm.getObservationcountPassCalculationType());
            eventForm.setObservationcountPassRange(oldEventForm.getObservationcountPassRange());
            eventForm.setObservationCountGroup(oldEventForm.getObservationCountGroup());

            persistenceService.update(oldEventForm);
        }

        eventType.setEventForm(eventForm);
        eventType.incrementFormVersion();

        persistenceService.save(eventForm);
        persistenceService.update(eventType);

        restoreTranslations(eventForm);
    }

    private void restoreTranslations(EventForm eventForm) {
        String criteriaTextOgnl = localizationService.getOgnlFor(criteriaTextField);
        String sectionTextOgnl = localizationService.getOgnlFor(sectionTextField);
        String recommendationsTextOgnl = localizationService.getOgnlFor(recommendationsField);
        String deficienciesTextOgnl = localizationService.getOgnlFor(deficienciesField);

        for (CriteriaSection sect : eventForm.getSections()) {
            if (sect.getOldId() != null) {
                findAndCopyTranslation(sectionTextOgnl, sect.getOldId(), sect.getId());
            }
            for (Criteria criteria : sect.getCriteria()) {
                if (criteria.getOldId() != null) {
                    findAndCopyTranslation(criteriaTextOgnl, criteria.getOldId(), criteria.getId());
                    findAndCopyObservationsTranslations(recommendationsTextOgnl, criteria.getOldId(), criteria.getId());
                    findAndCopyObservationsTranslations(deficienciesTextOgnl, criteria.getOldId(), criteria.getId());
                }
            }
        }
    }

    private void findAndCopyTranslation(String criteriaTextOgnl, Long oldId, Long newId) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addSimpleWhere("id.ognl", criteriaTextOgnl);
        query.addSimpleWhere("id.entityId", oldId);
        copyMatchingTranslations(newId, query);
    }

    private void findAndCopyObservationsTranslations(String observationsBaseOgnl, Long oldId, Long newId) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "id.ognl", observationsBaseOgnl, WhereParameter.WILDCARD_RIGHT);
        query.addSimpleWhere("id.entityId", oldId);
        copyMatchingTranslations(newId, query);
    }

    private void copyMatchingTranslations(Long newId, QueryBuilder<Translation> query) {
        List<Translation> existingTranslations = persistenceService.findAll(query);
        for (Translation existingTranslation : existingTranslations) {
            Translation.CompoundKey translationKey = new Translation.CompoundKey(existingTranslation.getId().getTenantId(), newId, existingTranslation.getId().getOgnl(), existingTranslation.getId().getLanguage());
            Translation translation = new Translation();
            translation.setId(translationKey);
            translation.setValue(existingTranslation.getValue());
            persistenceService.save(translation);
        }
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
