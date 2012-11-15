package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.model.Criteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;

public class ObservationsPanel extends Panel {

    private StringListEditor recommendationsEditor;
    private StringListEditor deficienciesEditor;

    public ObservationsPanel(String id, Model<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_observations", this, null)));
        add(recommendationsEditor = new StringListEditor("recommendationsEditor", new PropertyModel<List<String>>(criteriaModel, "recommendations")));
        add(deficienciesEditor = new StringListEditor("deficienciesEditor", new PropertyModel<List<String>>(criteriaModel, "deficiencies")));
    }

}
