package com.n4systems.fieldid.wicket.pages.eventform;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaSectionsPanel;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.EventTypeService;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class EventFormEditPage extends WebPage {

    private List<CriteriaSection> criteriaSections;

    private EventType eventType;

    private CriteriaSectionsPanel criteriaSectionsPanel;
    private CriteriaPanel criteriaPanel;
    private CriteriaDetailsPanel criteriaDetailsPanel;

    public EventFormEditPage(PageParameters params) {
        super(params);
        Long id = params.getAsLong("id");
        add(new DoneForm("doneForm"));

        FilteredIdLoader<EventType> idLoader = new LoaderFactory(FieldIDSession.get().getSessionUser().getSecurityFilter()).createFilteredIdLoader(EventType.class);
        idLoader.setPostFetchFields("sections");
        idLoader.setId(id);

        eventType = idLoader.load();
        criteriaSections = eventType.getSections();

        add(CSSPackageResource.getHeaderContribution("style/fieldid.css"));
        add(CSSPackageResource.getHeaderContribution("style/eventFormEdit.css"));

        add(criteriaSectionsPanel = new CriteriaSectionsPanel("criteriaSectionsPanel", new PropertyModel<List<CriteriaSection>>(this, "criteriaSections"))
        {
            @Override
            public void onCriteriaSectionAdded(AjaxRequestTarget target, CriteriaSection section) {
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
                    new EventTypeService().retireEventType(eventType.getId());

                    clearOutIds(eventType);

                    new EventTypeSaver().save(eventType);

                    target.appendJavascript("parent.refreshPageToNewEventTypeId("+eventType.getId()+");");

                    FieldIDSession.get().storeInfoMessageForStruts("Event Form saved.");
                }
            });
        }

    }

    private void clearOutIds(EventType eventType) {
        eventType.setId(null);
        for (CriteriaSection section : eventType.getSections()) {
            section.setId(null);
            for (Criteria criteria : section.getCriteria()) {
                criteria.setId(null);
            }
        }
    }

}
