package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.model.ButtonGroup;
import com.n4systems.model.Criteria;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class CriteriaConfigurationPanel extends Panel {

    private CriteriaDetailsPanel settings;
    private Component instructions;
    private Component observations;

    private WebMarkupContainer settingsButton;
    private WebMarkupContainer instructionsButton;
    private WebMarkupContainer observationsButton;
    private WebMarkupContainer pressedButton;

    public CriteriaConfigurationPanel(String id, Model<Criteria> criteriaModel) {
        super(id);
        add(settingsButton = new WebMarkupContainer("settingsButton"));
        add(instructionsButton = new WebMarkupContainer("instructionsButton"));
        add(observationsButton = new WebMarkupContainer("observationsButton"));

        add(settings = createDetailsPanel(criteriaModel));
        add(instructions = new InstructionsPanel("instructions", criteriaModel));
        add(observations = new ObservationsPanel("observations", criteriaModel));

        settings.setOutputMarkupPlaceholderTag(true);
        instructions.setOutputMarkupPlaceholderTag(true);
        observations.setOutputMarkupPlaceholderTag(true);

        initializeButton(settingsButton, settings);
        initializeButton(instructionsButton, instructions);
        initializeButton(observationsButton, observations);

        instructions.setVisible(false);
        observations.setVisible(false);
        pressedButton = settingsButton;

        setOutputMarkupPlaceholderTag(true);
        setOutputMarkupId(true);
    }

    private void initializeButton(final WebMarkupContainer button, final Component panel) {
        button.add(new AjaxEventBehavior("onclick") {
            @Override protected void onEvent(AjaxRequestTarget target) {
                updateTabs(target, button, panel);
            }
        });
        button.add(new AttributeAppender("class", getCssModel(button), " "));
    }

    private CriteriaDetailsPanel createDetailsPanel(final Model<Criteria> criteriaModel) {
        return new CriteriaDetailsPanel("settings", criteriaModel) {
            @Override protected void onStateSetSelected(ButtonGroup buttonGroup) {
                setPreviouslySelectedStateSet(buttonGroup);
            }
            @Override protected void onSetsResultSelected(boolean setsResult) {
                setPreviousSetsResultValue(setsResult);
            }
            @Override protected void onScoreGroupSelected(ScoreGroup scoreGroup) {
                setPreviouslySelectedScoreGroup(scoreGroup);
            }
        };
    }

    protected void setPreviouslySelectedScoreGroup(ScoreGroup scoreGroup) { }

    protected void setPreviousSetsResultValue(boolean setsResult) { }

    protected void setPreviouslySelectedStateSet(ButtonGroup buttonGroup) { }

    private IModel<?> getCssModel(final WebMarkupContainer button) {
        return new Model<String>() {
            @Override public String getObject() {
                return button == pressedButton ? "active" : " ";
            }
        };
    }

    private void updateTabs(AjaxRequestTarget target, WebMarkupContainer button, Component panel) {
        instructions.setVisible(false);
        observations.setVisible(false);
        settings.setVisible(false);
        pressedButton = button;
        panel.setVisible(true);
        target.add(CriteriaConfigurationPanel.this);
    }

    public void setCriteria(Criteria criteria) {
        settings.setDefaultModelObject(criteria);
        instructions.setDefaultModelObject(criteria);
    }
}
