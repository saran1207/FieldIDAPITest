package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Recommendation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsEditPanel extends ObservationsEditPanel<Recommendation> {
    
    private List<Recommendation> transientRecommendations;

    public RecommendationsEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel) {
        super(id, criteriaResultModel,
                new PropertyModel<List<? extends String>>(criteriaResultModel, "criteria.recommendations"),
                "recSelected");

        transientRecommendations = new ArrayList<Recommendation>(criteriaResultModel.getObject().getRecommendations());
    }

    @Override
    protected Recommendation createObservation() {
        return new Recommendation();
    }

    @Override
    protected List<Recommendation> getTransientObservations() {
        return transientRecommendations;
    }

    @Override
    protected void storeObservations() {
        criteriaResultModel.getObject().setRecommendations(transientRecommendations);
    }
}
