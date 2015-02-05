package com.n4systems.fieldid.wicket.components.event.criteria.factory;

import com.n4systems.fieldid.wicket.components.event.criteria.view.*;
import com.n4systems.model.*;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class CriteriaResultFactory {

    @SuppressWarnings("unchecked")
    public static Component createResultPanelFor(String componentId, IModel<? extends CriteriaResult> resultModel) {
        CriteriaResult result = resultModel.getObject();
        if (result instanceof TextFieldCriteriaResult) {
            return new StringCriteriaResultPanel(componentId, resultModel);
        } else if (result instanceof SelectCriteriaResult) {
            return new StringCriteriaResultPanel(componentId, resultModel);
        } else if (result instanceof OneClickCriteriaResult) {
            return new OneClickCriteriaResultPanel(componentId, (IModel<OneClickCriteriaResult>) resultModel);
        } else if (result instanceof NumberFieldCriteriaResult) {
            return new NumberCriteriaResultPanel(componentId, (IModel<NumberFieldCriteriaResult>) resultModel);
        } else if (result instanceof ScoreCriteriaResult) {
            return new ScoreCriteriaResultPanel(componentId, (IModel<ScoreCriteriaResult>) resultModel);
        } else if (result instanceof DateFieldCriteriaResult) {
            return new DateCriteriaResultPanel(componentId, (IModel<DateFieldCriteriaResult>) resultModel);
        } else if (result instanceof ObservationCountCriteriaResult) {
            return new ObservationCountCriteriaResultPanel(componentId, (IModel<ObservationCountCriteriaResult>) resultModel);
        } else if (result instanceof UnitOfMeasureCriteriaResult) {
            return new UnitOfMeasureCriteriaResultPanel(componentId, (IModel<UnitOfMeasureCriteriaResult>) resultModel);
        } else if (result instanceof SignatureCriteriaResult) {
            return new SignatureCriteriaResultPanel(componentId, (IModel<SignatureCriteriaResult>) resultModel);
        } else if (result instanceof ComboBoxCriteriaResult) {
            return new StringCriteriaResultPanel(componentId, resultModel);
        } else {
            return new WebMarkupContainer(componentId);
        }
    }

}
