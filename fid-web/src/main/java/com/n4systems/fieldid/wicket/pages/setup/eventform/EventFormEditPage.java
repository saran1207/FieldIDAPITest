package com.n4systems.fieldid.wicket.pages.setup.eventform;

import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaSectionsPanel;
import com.n4systems.fieldid.wicket.components.eventform.save.SavePanel;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaSectionCopyUtil;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.pages.setup.eventtype.EventTypePage;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.StateSet;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventFormEditPage extends EventTypePage {

    @SpringBean
    private EventFormService eventFormService;

    private List<CriteriaSection> criteriaSections;

    private CriteriaSectionsPanel criteriaSectionsPanel;
    private CriteriaPanel criteriaPanel;
    private CriteriaDetailsPanel criteriaDetailsPanel;

    private SavePanel topSavePanel;
    private SavePanel bottomSavePanel;

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_event_type_id", eventTypeModel.getObject().getName()));
    }

    public EventFormEditPage(PageParameters params) {
        super(params);

        add(CSSPackageResource.getHeaderContribution("style/fieldid.css"));
        add(CSSPackageResource.getHeaderContribution("style/eventFormEdit.css"));

        add(topSavePanel = createSavePanel("topSavePanel"));
        add(bottomSavePanel = createSavePanel("bottomSavePanel"));

        criteriaSections = new ArrayList<CriteriaSection>();
        if (eventTypeModel.getObject().getEventForm() != null) {
            criteriaSections.addAll(eventTypeModel.getObject().getEventForm().getAvailableSections());
        }

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

        add(criteriaDetailsPanel = new CriteriaDetailsPanel("criteriaDetailsPanel", new Model<Criteria>()) {
            @Override
            protected void onStateSetSelected(StateSet stateSet) {
                criteriaPanel.setPreviouslySelectedStateSet(stateSet);
            }

            @Override
            protected void onSetsResultSelected(boolean setsResult) {
                criteriaPanel.setPreviousSetsResultValue(setsResult);
            }
        });
        criteriaDetailsPanel.setVisible(false);
    }

    private SavePanel createSavePanel(String panelID) {
        return new SavePanel(panelID) {
            @Override
            protected void onSaveAndContinueClicked(AjaxRequestTarget target) {
                saveEventForm();
                target.addComponent(topSavePanel);
                target.addComponent(bottomSavePanel);
            }

            @Override
            protected void onSaveAndFinishClicked(AjaxRequestTarget target) {
                saveEventForm();
                FieldIDSession.get().storeInfoMessageForStruts("Event Form saved.");
                getRequestCycle().setRequestTarget(new RedirectRequestTarget("/eventType.action?uniqueID=" + eventTypeId));
            }
        };
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
        target.focusComponent(criteriaPanel.getAddTextField());
    }

    private void saveEventForm() {
        EventForm eventForm = new EventForm();
        eventForm.setSections(createCopiesOf(criteriaSections));
        eventForm.setTenant(FieldIDSession.get().getSessionUser().getTenant());

        eventFormService.saveNewEventForm(eventTypeId, eventForm);
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
