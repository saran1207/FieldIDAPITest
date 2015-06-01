package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ActionsPanel extends Panel {

    private Component actionsListPanel;
    private Component addEditActionPanel;
    private Component actionDetailsPanel;

    private IModel<CriteriaResult> criteriaResultModel;
    private Class<? extends Event> eventClass;
    private boolean readOnly;
    private boolean assetSummaryContext;

    public ActionsPanel(String id, IModel<CriteriaResult> criteriaResultModel, Class<? extends Event> eventClass, IModel<Event> eventModel, boolean readOnly, boolean assetSummaryContext) {
        super(id, criteriaResultModel);
        this.criteriaResultModel = criteriaResultModel;
        this.eventClass = eventClass;
        this.readOnly = readOnly;
        this.assetSummaryContext = assetSummaryContext;

        actionsListPanel = new ActionsListPanel("actionsListPanel", criteriaResultModel, eventClass, readOnly) {
            @Override
            public void onAddAction(AjaxRequestTarget target) {
                actionsListPanel.setVisible(false);
                addEditActionPanel.replaceWith(addEditActionPanel = getAddEditActionPanel(null));
                addEditActionPanel.setParent(ActionsPanel.this);
                target.add(ActionsPanel.this);
            }

            @Override
            public void onShowDetailsPanel(AjaxRequestTarget target, IModel<Event> eventModel) {
                actionsListPanel.setVisible(false);
                actionDetailsPanel.replaceWith(actionDetailsPanel = getActionsDetailsPanel(eventModel));
                actionDetailsPanel.setParent(ActionsPanel.this);
                target.add(ActionsPanel.this);
            }
        };
        actionsListPanel.setOutputMarkupPlaceholderTag(true);
        add(actionsListPanel);

        addEditActionPanel = getAddEditActionPanel(null);
        add(addEditActionPanel);

        actionDetailsPanel = getActionsDetailsPanel(eventModel);
        actionDetailsPanel.setOutputMarkupPlaceholderTag(true);
        add(actionDetailsPanel);

        setVisibility(eventModel == null);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/component/event_actions.css");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");

        //Old CSS file - remove when site is completely moved over to framework styles.
        response.renderCSSReference("style/legacy/newCss/layout/feedback_errors.css");
    }

    private Component getAddEditActionPanel(IModel<Event> eventModel) {
        return new AddEditActionPanel("addEditActionPanel", criteriaResultModel, eventClass, eventModel, assetSummaryContext) {
            @Override
            public void onShowListPanel(AjaxRequestTarget target) {
                addEditActionPanel.setVisible(false);
                actionsListPanel.setVisible(true);
                target.add(ActionsPanel.this);
            }

            @Override
            public void onShowDetailsPanel(AjaxRequestTarget target, IModel<Event> eventModel) {
                addEditActionPanel.setVisible(false);
                actionDetailsPanel.replaceWith(actionDetailsPanel = getActionsDetailsPanel(eventModel));
                actionDetailsPanel.setParent(ActionsPanel.this);
                target.add(ActionsPanel.this);
            }

        }.setOutputMarkupPlaceholderTag(true);
    }

    private Component getActionsDetailsPanel(IModel<Event> eventModel) {
        if(eventModel == null)
            return new EmptyPanel("actionDetailsPanel");
        else {
            return new ActionDetailsPanel("actionDetailsPanel", criteriaResultModel, eventClass, eventModel, assetSummaryContext) {
                @Override
                public void onBackToList(AjaxRequestTarget target) {
                    actionsListPanel.setVisible(true);
                    actionDetailsPanel.setVisible(false);
                    target.add(ActionsPanel.this);
                }

                @Override
                public void onEditAction(AjaxRequestTarget target, IModel<Event> eventModel) {
                    actionDetailsPanel.setVisible(false);
                    addEditActionPanel.replaceWith(addEditActionPanel = getAddEditActionPanel(eventModel));
                    addEditActionPanel.setParent(ActionsPanel.this);
                    target.add(ActionsPanel.this);
                }

                @Override
                protected boolean isEditable() {
                    return !readOnly;
                }
            }.setOutputMarkupPlaceholderTag(true);
        }
    }

    private void setVisibility(boolean isDisplayList) {
        addEditActionPanel.setVisible(false);
        actionsListPanel.setVisible(isDisplayList);
        actionDetailsPanel.setVisible(!isDisplayList);
    }
}
