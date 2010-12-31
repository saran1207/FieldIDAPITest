package com.n4systems.fieldid.wicket.pages.eventform;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaSectionsPanel;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaSectionCopyUtil;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class EventFormEditPage extends WebPage {

    private List<CriteriaSection> criteriaSections;

    private Long eventTypeId;
    private Long oldEventFormId;

    private CriteriaSectionsPanel criteriaSectionsPanel;
    private CriteriaPanel criteriaPanel;
    private CriteriaDetailsPanel criteriaDetailsPanel;

    public EventFormEditPage(PageParameters params) {
        super(params);
        add(new DoneForm("doneForm"));

        eventTypeId = params.getAsLong("id");

        FilteredIdLoader<EventType> idLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventType.class);
        idLoader.setPostFetchFields("eventForm.sections");
        idLoader.setId(eventTypeId);
        EventType eventType = idLoader.load();

        criteriaSections = new ArrayList<CriteriaSection>();
        if (eventType.getEventForm() != null) {
            oldEventFormId = eventType.getEventForm().getId();
            criteriaSections .addAll(eventType.getEventForm().getSections());
        }

        add(CSSPackageResource.getHeaderContribution("style/fieldid.css"));
        add(CSSPackageResource.getHeaderContribution("style/eventFormEdit.css"));

        add(criteriaSectionsPanel = new CriteriaSectionsPanel("criteriaSectionsPanel", new PropertyModel<List<CriteriaSection>>(this, "criteriaSections"))
        {
            @Override
            public void onCriteriaSectionAdded(AjaxRequestTarget target, CriteriaSection section) {
                section.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                criteriaSections.add(section);
                setSelectedIndex(criteriaSections.size() - 1);
                onCriteriaSectionSelected(target, criteriaSections.size() - 1);
                focusOnCriteriaNameField(target);
            }

            @Override
            public void onCriteriaSectionSelected(AjaxRequestTarget target, int index) {
                updateCriteriaPanel(new Model<CriteriaSection>(criteriaSections.get(index)));
                criteriaPanel.clearSelection();
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }

            @Override
            protected void onCriteriaSectionListUpdated(AjaxRequestTarget target) {
                refreshAllComponents(target);
            }

            @Override
            protected void onCurrentCriteriaSectionDeleted(AjaxRequestTarget target) {
                criteriaPanel.setVisible(false);
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }
        });
        add(criteriaPanel = new CriteriaPanel("criteriaPanel") {
            @Override
            public void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) {
                criteria.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                setSelectedIndex(newIndex);
                onCriteriaSelected(target, criteria);
                focusOnCriteriaNameField(target);
            }

            @Override
            protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) {
                criteriaDetailsPanel.setDefaultModelObject(criteria);
                criteriaDetailsPanel.setVisible(true);
                refreshAllComponents(target);
            }

            @Override
            protected void onCurrentCriteriaDeleted(AjaxRequestTarget target) {
                criteriaDetailsPanel.setVisible(false);
                refreshAllComponents(target);
            }

            @Override
            protected void onCriteriaListUpdated(AjaxRequestTarget target) {
                refreshAllComponents(target);
            }
        });
        criteriaPanel.setVisible(false);

        add(criteriaDetailsPanel = new CriteriaDetailsPanel("criteriaDetailsPanel", new Model<Criteria>()));
        criteriaDetailsPanel.setVisible(false);
    }

    public void updateCriteriaPanel(IModel<CriteriaSection> newModel) {
        criteriaPanel.setDefaultModel(newModel);
        criteriaPanel.setVisible(true);
    }

    private void refreshAllComponents(AjaxRequestTarget target) {
        target.addComponent(criteriaSectionsPanel);
        target.addComponent(criteriaPanel);
        target.addComponent(criteriaDetailsPanel);
    }

    private void focusOnCriteriaNameField(AjaxRequestTarget target) {
        target.appendJavascript("document.getElementById('"+criteriaPanel.getAddTextFieldId()+"').focus()");
    }

    class DoneForm extends Form {

        public DoneForm(String id) {
            super(id);
            add(new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Transaction tx = PersistenceManager.startTransaction();

                    FilteredIdLoader<EventForm> eventFormLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventForm.class);
                    if (oldEventFormId != null) {
                        EventForm oldEventForm = eventFormLoader.setId(oldEventFormId).load(tx);
                        oldEventForm.setState(Archivable.EntityState.RETIRED);
                        new EventFormSaver().update(tx, oldEventForm);
                    }

                    FilteredIdLoader<EventType> eventTypeLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventType.class);

                    EventType eventType = eventTypeLoader.setId(eventTypeId).setPostFetchFields("eventForm.sections").load(tx);

                    EventForm eventForm = new EventForm();
                    eventForm.setSections(createCopiesOf(criteriaSections));
                    eventForm.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                    eventType.setEventForm(eventForm);
                    eventType.incrementFormVersion();
                    
                    new EventFormSaver().save(tx, eventForm);
                    new EventTypeSaver().update(tx, eventType);

                    target.appendJavascript("parent.location.reload();");

                    tx.commit();

                    FieldIDSession.get().storeInfoMessageForStruts("Event Form saved.");
                }
            });
        }

    }

    private List<CriteriaSection> createCopiesOf(List<CriteriaSection> criteriaSections) {
        CriteriaSectionCopyUtil copyUtil = new CriteriaSectionCopyUtil();
        List<CriteriaSection> copiedSections = new ArrayList<CriteriaSection>();
        for (CriteriaSection section : criteriaSections) {
            copiedSections.add(copyUtil.copySection(section));
        }
        return copiedSections;
    }

}
