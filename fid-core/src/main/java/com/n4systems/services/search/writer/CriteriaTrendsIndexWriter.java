package com.n4systems.services.search.writer;

import com.n4systems.model.*;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

public class CriteriaTrendsIndexWriter extends IndexWriter<Event> {

    public CriteriaTrendsIndexWriter() {
        super(Event.class);
    }

    @Override
    protected void begin(EntityManager em) {
        em.getTransaction().begin();
    }

    @Override
    protected void index(EntityManager em, Tenant tenant, List<Event> items, boolean update) {
        for (Event item : items) {
            index(em, tenant, item);
        }
    }

    private void index(EntityManager em, Tenant tenant, Event event) {
        removeExistingTrendsEntries(em, tenant, event);

        if (event.getWorkflowState() == WorkflowState.COMPLETED && event.isActive()) {
            saveNewTrendsEntries(em, tenant, event);
        }
    }

    private void saveNewTrendsEntries(EntityManager em, Tenant tenant, Event event) {
        Iterator<CriteriaResult> results;
        try {
            results = event.getResults().iterator();
        } catch(org.hibernate.InstantiationException e) {
            // This is happening unfortunately because of some criteria results that are in a bad state -- ie they have no proper subtype
            // in the inheritance tables.
            logger.error("Could not index event for criteria trends on event: " + event.getID() + " tenant: " + tenant.getName());
            return;
        }

        for (;results.hasNext();) {
            CriteriaResult criteriaResult = results.next();
            if (criteriaResult instanceof OneClickCriteriaResult) {
                OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult) criteriaResult;
                CriteriaTrendsEntry entry = new CriteriaTrendsEntry();
                entry.setEvent(event);
                entry.setDueDate(event.getDueDate());
                entry.setCompletedDate(event.getCompletedDate());
                entry.setAssignedGroup(event.getAssignedGroup());
                entry.setAssignee(event.getAssignee());
                entry.setCriteria(oneClickResult.getCriteria());
                entry.setCriteriaName(oneClickResult.getCriteria().getDisplayName());
                entry.setResultText(oneClickResult.getButton().getDisplayText());
                entry.setEventForm(event.getEventForm());
                entry.setPerformedBy(event.getPerformedBy());
                entry.setOwner(event.getOwner());
                entry.setEventType(event.getType());
                entry.setTenant(tenant);
                em.persist(entry);
            }
        }
    }

    private void removeExistingTrendsEntries(EntityManager em, Tenant tenant, Event event) {
        QueryBuilder<CriteriaTrendsEntry> query = new QueryBuilder<CriteriaTrendsEntry>(CriteriaTrendsEntry.class, new TenantOnlySecurityFilter(tenant));
        query.addSimpleWhere("event", event);
        for (CriteriaTrendsEntry criteriaTrendsEntry : query.getResultList(em)) {
            em.remove(criteriaTrendsEntry);
        }
    }

    @Override
    protected void cleanup(EntityManager em) {
        try {
            em.getTransaction().commit();
        } finally {
            super.cleanup(em);
        }
    }

}
