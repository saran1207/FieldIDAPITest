package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class DeficienciesEditPanel extends ObservationsEditPanel<Deficiency> {
    
    private List<Deficiency> transientDeficiencies;

    public DeficienciesEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel) {
        super(id, criteriaResultModel,
                new PropertyModel<List<? extends String>>(criteriaResultModel, "criteria.deficiencies"),
                "defSelected");
        
        transientDeficiencies = new ArrayList<Deficiency>(criteriaResultModel.getObject().getDeficiencies());
    }
    
    @Override
    protected Deficiency createObservation() {
        return new Deficiency();
    }

    @Override
    protected List<Deficiency> getTransientObservations() {
        return transientDeficiencies;
    }

    @Override
    protected void storeObservations() {
        criteriaResultModel.getObject().setDeficiencies(transientDeficiencies);
    }
}
