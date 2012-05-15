package com.n4systems.fieldid.wicket.components.event.criteria.factory;

import com.n4systems.fieldid.wicket.components.event.criteria.*;
import com.n4systems.model.*;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public class CriteriaEditorFactory {

    @SuppressWarnings("unchecked")
    public static Component createEditorFor(String componentId, IModel<? extends CriteriaResult> resultModel) {
        CriteriaResult result = resultModel.getObject();
        if (result instanceof TextFieldCriteriaResult) {
            return new TextCriteriaEditPanel(componentId, (IModel<TextFieldCriteriaResult>) resultModel);
        } else if (result instanceof SelectCriteriaResult) {
            return new SelectCriteriaEditPanel(componentId, (IModel<SelectCriteriaResult>) resultModel);
        } else if (result instanceof OneClickCriteriaResult) {
            return new OneClickCriteriaEditPanel(componentId, (IModel<OneClickCriteriaResult>) resultModel);
        } else if (result instanceof NumberFieldCriteriaResult) {
            return new NumberFieldCriteriaEditPanel(componentId, (IModel<NumberFieldCriteriaResult>) resultModel);
        } else if (result instanceof ScoreCriteriaResult) {
            return new ScoreCriteriaEditPanel(componentId, (IModel<ScoreCriteriaResult>) resultModel);
        } else if (result instanceof DateFieldCriteriaResult) {
            return new DateCriteriaEditPanel(componentId, (IModel<DateFieldCriteriaResult>) resultModel);
        } else if (result instanceof UnitOfMeasureCriteriaResult) {
            return new UnitOfMeasureCriteriaEditPanel(componentId, (IModel<UnitOfMeasureCriteriaResult>) resultModel);
        } else if (result instanceof SignatureCriteriaResult) {
            return new SignatureCriteriaResultEditPanel(componentId, (IModel<SignatureCriteriaResult>) resultModel);
        } else if (result instanceof ComboBoxCriteriaResult) {
            return new ComboBoxCriteriaEditPanel(componentId, (IModel<ComboBoxCriteriaResult>) resultModel);
        } else {
            return new WebMarkupContainer(componentId);
        }
    }

}
