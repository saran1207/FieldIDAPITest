package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Observation;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class ObservationEditPanel<T extends Observation> extends Panel {
    
    protected IModel<CriteriaResult> criteriaResultModel;

    public ObservationEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel,
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
                            getCurrentObservations().add(recommendation);
                        } else {
                            getCurrentObservations().remove(observation);
                        }
                        target.add(ObservationEditPanel.this);
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
    }
    
    protected abstract T createObservation();
    protected abstract List<T> getCurrentObservations();
    
    public String getComments() {
        Observation commentObservation = findCommentObservation();
        if (commentObservation == null) {
            return null;
        }
        return commentObservation.getText();
    }
    
    public void setComments(String comments) {
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
        for (T recommendation : getCurrentObservations()) {
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
            getCurrentObservations().add(commentObservation);
        }
        return commentObservation;
    }
    
    private T findCommentObservation() {
        for (T observation : getCurrentObservations()) {
            if (observation.getState() == Observation.State.COMMENT) {
                return observation;
            }
        }
        return null;
    }

}
