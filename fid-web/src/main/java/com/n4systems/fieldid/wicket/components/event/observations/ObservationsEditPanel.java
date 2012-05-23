package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Observation;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class ObservationsEditPanel<T extends Observation> extends Panel {
    
    protected IModel<CriteriaResult> criteriaResultModel;
    private String comments;

    public ObservationsEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel,
                                 IModel<List<? extends String>> preconfiguredObservationsModel,
                                 final String selectedClass) {
        super(id);
        setOutputMarkupId(true);
        this.criteriaResultModel = criteriaResultModel;

        add(new ListView<String>("preConfiguredObservations", preconfiguredObservationsModel) {
            @Override
            protected void populateItem(final ListItem<String> item) {
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        T observation = findObservation(item.getModelObject());
                        if (observation == null) {
                            T recommendation = createObservation();
                            recommendation.setState(Observation.State.OUTSTANDING);
                            recommendation.setText(item.getModelObject());
                            getTransientObservations().add(recommendation);
                        } else {
                            getTransientObservations().remove(observation);
                        }
                        target.add(ObservationsEditPanel.this);
                    }
                });
                item.add(new AttributeAppender("class", new Model<String>(selectedClass), " ") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return findObservation(item.getModelObject()) != null;
                    }
                });
                item.add(new Label("observationName", item.getModel()));
            }
        });

        TextArea<String> commentsArea = new TextArea<String>("comments", new PropertyModel<String>(this, "comments"));
        add(commentsArea);
        commentsArea.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        
        add(new AjaxLink("saveButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                storeObservations();
                storeComment(comments);
                onClose(target);
            }
        });

        add(new AjaxLink<Void>("clearLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getTransientObservations().clear();
                target.add(ObservationsEditPanel.this);
            }
        });

        add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onClose(target);
            }
        });
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        comments = getTransientComment();
    }


    protected abstract T createObservation();
    protected abstract List<T> getTransientObservations();
    protected abstract void storeObservations();

    protected void onClose(AjaxRequestTarget target) {}
    
    public String getTransientComment() {
        Observation commentObservation = findCommentObservation();
        if (commentObservation == null) {
            return null;
        }
        return commentObservation.getText();
    }
    
    public void storeComment(String comments) {
        Observation commentObservation = findCommentObservation();
        if (StringUtils.isBlank(comments)) {
            if (commentObservation != null) {
                criteriaResultModel.getObject().getRecommendations().remove(commentObservation);
            }
        } else {
            Observation newCommentObservation = findOrCreateCommentObservation();
            newCommentObservation.setText(comments);
        }
    }
    
    private T findObservation(String observationName) {
        for (T recommendation : getTransientObservations()) {
            if (recommendation.getState() == Observation.State.OUTSTANDING && observationName.equals(recommendation.getText())) {
                return recommendation;
            }
        }
        return null;
    }

    private T findOrCreateCommentObservation() {
        T commentObservation = findCommentObservation();
        if (commentObservation == null) {
            commentObservation = createObservation();
            commentObservation.setState(Observation.State.COMMENT);
            getTransientObservations().add(commentObservation);
        }
        return commentObservation;
    }
    
    private T findCommentObservation() {
        for (T observation : getTransientObservations()) {
            if (observation.getState() == Observation.State.COMMENT) {
                return observation;
            }
        }
        return null;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }
}
