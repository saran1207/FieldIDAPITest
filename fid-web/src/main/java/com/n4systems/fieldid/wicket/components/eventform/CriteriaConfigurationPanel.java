package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.model.Criteria;
import com.n4systems.model.ScoreGroup;
import com.n4systems.model.StateSet;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class CriteriaConfigurationPanel extends Panel {

    private CriteriaDetailsPanel settings;
    private WebMarkupContainer instructions;
    private WebMarkupContainer observations;

    private WebMarkupContainer settingsButton;
    private WebMarkupContainer instructionsButton;
    private WebMarkupContainer observationsButton;
    private WebMarkupContainer pressedButton;

    public CriteriaConfigurationPanel(String id, Model<Criteria> criteriaModel) {
        super(id);
        add(settingsButton = new WebMarkupContainer("settingsButton"));
        add(instructionsButton = new WebMarkupContainer("instructionsButton"));
        add(observationsButton = new WebMarkupContainer("observationsButton"));

        add(settings = new CriteriaDetailsPanel("settings", criteriaModel) {
            @Override protected void onStateSetSelected(StateSet stateSet) {
                setPreviouslySelectedStateSet(stateSet);
            }
            @Override protected void onSetsResultSelected(boolean setsResult) {
                setPreviousSetsResultValue(setsResult);
            }
            @Override protected void onScoreGroupSelected(ScoreGroup scoreGroup) {
                setPreviouslySelectedScoreGroup(scoreGroup);
            }
        });
        add(instructions = new WebMarkupContainer("instructions"));
        add(observations = new ObservationsPanel("observations", criteriaModel));

        settings.setOutputMarkupPlaceholderTag(true);
        instructions.setOutputMarkupPlaceholderTag(true);
        observations.setOutputMarkupPlaceholderTag(true);

        instructions.setVisible(false);
        observations.setVisible(false);

        settingsButton.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                updateTabs(target, settingsButton, settings);
            }
        });

        instructionsButton.add(new AjaxEventBehavior("onclick") {
            @Override protected void onEvent(AjaxRequestTarget target) {
                updateTabs(target, instructionsButton, instructions);
            }
        });

        observationsButton.add(new AjaxEventBehavior("onclick") {
            @Override protected void onEvent(AjaxRequestTarget target) {
                updateTabs(target, observationsButton, observations);
            }
        });

        pressedButton = settingsButton;
        settingsButton.add(new AttributeAppender("class", getCssModel(settingsButton), " "));
        observationsButton.add(new AttributeAppender("class", getCssModel(observationsButton), "  "));
        instructionsButton.add(new AttributeAppender("class", getCssModel(instructionsButton), " " ));

        setOutputMarkupPlaceholderTag(true);
        setOutputMarkupId(true);
    }

    protected void setPreviouslySelectedScoreGroup(ScoreGroup scoreGroup) { }

    protected void setPreviousSetsResultValue(boolean setsResult) { }

    protected void setPreviouslySelectedStateSet(StateSet stateSet) { }

    private IModel<?> getCssModel(final WebMarkupContainer button) {
        return new Model<String>() {
            @Override public String getObject() {
                return button == pressedButton ? "active" : " ";
            }
        };
    }

    private void updateTabs(AjaxRequestTarget target, WebMarkupContainer button, WebMarkupContainer panel) {
        instructions.setVisible(false);
        observations.setVisible(false);
        settings.setVisible(false);
        pressedButton = button;
        panel.setVisible(true);
        target.add(CriteriaConfigurationPanel.this);
    }

    public void setCriteria(Criteria criteria) {
        settings.setDefaultModelObject(criteria);
    }
}
