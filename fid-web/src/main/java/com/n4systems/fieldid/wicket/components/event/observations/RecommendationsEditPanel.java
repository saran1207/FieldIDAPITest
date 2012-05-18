package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Recommendation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class RecommendationsEditPanel extends ObservationEditPanel<Recommendation> {

    public RecommendationsEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel) {
        super(id, criteriaResultModel,
                new PropertyModel<List<? extends String>>(criteriaResultModel, "criteria.recommendations"),
                "recSelected");
    }

    @Override
    protected Recommendation createObservation() {
        return new Recommendation();
    }

    @Override
    protected List<Recommendation> getCurrentObservations() {
        return criteriaResultModel.getObject().getRecommendations();
    }

}
