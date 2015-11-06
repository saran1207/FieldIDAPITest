package com.n4systems.fieldid.wicket.pages.setup.eventform;

import com.n4systems.fieldid.service.event.EventFormService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaConfigurationPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaPanel;
import com.n4systems.fieldid.wicket.components.eventform.CriteriaSectionsPanel;
import com.n4systems.fieldid.wicket.components.eventform.save.SavePanel;
import com.n4systems.fieldid.wicket.pages.setup.eventtype.LegacyEventTypePage;
import com.n4systems.model.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class EventFormEditPage extends LegacyEventTypePage {

    @SpringBean
    private EventFormService eventFormService;

    private List<CriteriaSection> criteriaSections;

    private CriteriaSectionsPanel criteriaSectionsPanel;
    private CriteriaPanel criteriaPanel;
    private CriteriaConfigurationPanel criteriaDetailsPanel;

    private SavePanel topSavePanel;
    private SavePanel bottomSavePanel;

    public EventFormEditPage(PageParameters params) {
        super(params);

        add(topSavePanel = createSavePanel("topSavePanel"));
        add(bottomSavePanel = createSavePanel("bottomSavePanel"));

        criteriaSections = new ArrayList<>();
        if (eventTypeModel.getObject().getEventForm() != null) {
            criteriaSections.addAll(eventTypeModel.getObject().getEventForm().getAvailableSections());
        }

        add(criteriaSectionsPanel = new CriteriaSectionsPanel("criteriaSectionsPanel", new PropertyModel<>(this, "criteriaSections"))
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
                updateComponentStatesForSectionSelected(index);
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

            @Override
            protected void onAllSectionCriteriaRequired(AjaxRequestTarget target) {
                criteriaDetailsPanel.redrawSettingsPanel(target);
            }
        });

        boolean isMaster = false;

        if(eventTypeModel.getObject() instanceof ThingEventType) {
            isMaster = ((ThingEventType)eventTypeModel.getObject()).isMaster();
        }

        add(criteriaPanel = new CriteriaPanel("criteriaPanel", new PropertyModel<>(eventTypeModel, "eventForm"), isMaster) {
            @Override
            public void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) {
                criteria.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                setSelectedIndex(newIndex);
                onCriteriaSelected(target, criteria);
                focusOnCriteriaNameField(target);
            }

            @Override
            protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) {
                updateComponentsForCriteriaSelected(criteria);
                criteriaDetailsPanel.redrawSettingsPanel(target);
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

        add(criteriaDetailsPanel = new CriteriaConfigurationPanel("criteriaDetailsPanel",new Model<>()) {
            @Override protected void setPreviouslySelectedScoreGroup(ScoreGroup scoreGroup) {
                criteriaPanel.setPreviouslySelectedScoreGroup(scoreGroup);
            }

            @Override protected void setPreviouslySelectedStateSet(ButtonGroup buttonGroup) {
                criteriaPanel.setPreviouslySelectedButtonGroup(buttonGroup);
            }

            @Override
            protected void setPreviousSetsResultValue(boolean setsResult) {
                criteriaPanel.setPreviousSetsResultValue(setsResult);
            }
        });
        criteriaDetailsPanel.setVisible(false);

        // Initialize the state to first section selected, first criteria of that section selected
        if (criteriaSections.size() > 0) {
            updateComponentStatesForSectionSelected(0);
            if (criteriaSections.get(0).getAvailableCriteria().size() > 0) {
                updateComponentsForCriteriaSelected(criteriaSections.get(0).getAvailableCriteria().get(0));
            }
        }
    }

    private void updateComponentsForCriteriaSelected(Criteria criteria) {
        criteriaDetailsPanel.setCriteria(criteria);
        criteriaDetailsPanel.setVisible(true);
    }

    private void updateComponentStatesForSectionSelected(int index) {
        CriteriaSection criteriaSection = criteriaSections.get(index);
        updateCriteriaPanel(new Model<>(criteriaSection));
        criteriaDetailsPanel.setVisible(false);
        if (criteriaSection.getAvailableCriteria().size() > 0) {
            updateComponentsForCriteriaSelected(criteriaSection.getAvailableCriteria().get(0));
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/fieldid.css");
        response.renderCSSReference("style/legacy/eventFormEdit.css");
        response.renderCSSReference("style/legacy/newCss/component/header_reorder_link_button.css");
    }

    private SavePanel createSavePanel(String panelID) {
        return new SavePanel(panelID) {
            @Override
            protected void onSaveAndContinueClicked(AjaxRequestTarget target) {
                saveEventForm();
                target.add(topSavePanel);
                target.add(bottomSavePanel);
            }

            @Override
            protected void onSaveAndFinishClicked(AjaxRequestTarget target) {
                saveEventForm();
                FieldIDSession.get().storeInfoMessageForStruts("Event Form saved.");
                target.appendJavaScript("promptBeforeLeaving = false; window.location='/fieldid/eventType.action?uniqueID=" + eventTypeId + "'");
            }
        };
    }

    public void updateCriteriaPanel(IModel<CriteriaSection> newModel) {
        criteriaPanel.setDefaultModel(newModel);
        criteriaPanel.setVisible(true);
    }

    private void refreshAllComponents(AjaxRequestTarget target) {
        target.add(criteriaSectionsPanel);
        target.add(criteriaPanel);
        target.add(criteriaDetailsPanel);
    }

    private void focusOnCriteriaNameField(AjaxRequestTarget target) {
        target.focusComponent(criteriaPanel.getAddTextField());
    }

    private void saveEventForm() {
        eventFormService.saveNewEventForm(eventTypeId, criteriaSections);
    }

}
