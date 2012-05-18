package com.n4systems.fieldid.wicket.components.event.observations;

import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class DeficienciesEditPanel extends ObservationEditPanel<Deficiency> {

    public DeficienciesEditPanel(String id, final IModel<CriteriaResult> criteriaResultModel) {
        super(id, criteriaResultModel,
                new PropertyModel<List<? extends String>>(criteriaResultModel, "criteria.deficiencies"),
                "defSelected");
    }
    
    @Override
    protected Deficiency createObservation() {
        return new Deficiency();
    }

    @Override
    protected List<Deficiency> getCurrentObservations() {
        return criteriaResultModel.getObject().getDeficiencies();
    }

}
