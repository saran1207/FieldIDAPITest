package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.model.Criteria;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

public class ObservationsPanel extends Panel {

    private StringListEditor recommendationsEditor;
    private StringListEditor deficienciesEditor;

    private static int MAXLENGTH = 2048;

    public ObservationsPanel(String id, Model<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_observations", this, null)));
        add(recommendationsEditor = new StringListEditor("recommendationsEditor", new PropertyModel<List<String>>(criteriaModel, "recommendations")) {
            @Override
            protected void withValidation(TextField<String> addItemTextField) {
                addItemTextField.add(StringValidator.maximumLength(MAXLENGTH));
            }
        });
        add(deficienciesEditor = new StringListEditor("deficienciesEditor", new PropertyModel<List<String>>(criteriaModel, "deficiencies")){
            @Override
            protected void withValidation(TextField<String> addItemTextField) {
                addItemTextField.add(StringValidator.maximumLength(MAXLENGTH));
            }
        });
    }

}
