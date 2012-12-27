package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.richText.RichText;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Criteria;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import static ch.lambdaj.Lambda.on;

public class InstructionsPanel extends Panel {

    private RichText text;

    public InstructionsPanel(final String id, final Model<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new InstructionsForm("form", (Model<Criteria>) getDefaultModel()));
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_instructions",this,null)));
    }

    class InstructionsForm extends Form {

        public InstructionsForm(String id, final Model<Criteria> criteriaModel) {
            super(id, criteriaModel);
            add(text = new RichText("text", ProxyModel.of(criteriaModel, on(Criteria.class).getInstructions())) {
                @Override
                protected String getImagePath() {
                    String path = (criteriaModel.getObject()==null) ? "empty" : criteriaModel.getObject().getId().toString();
                    return String.format("/criteria/%s/instructions/images/", path);
                }
            }.withAutoUpdate().withWidth("310px"));
        }
    }

}
