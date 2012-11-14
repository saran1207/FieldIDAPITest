package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.richText.RichText;
import com.n4systems.model.Criteria;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class InstructionsPanel extends Panel {

    private RichText text;

    public InstructionsPanel(final String id, final Model<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new InstructionsForm("form", criteriaModel));
    }

    class InstructionsForm extends Form {

        public InstructionsForm(String id, final Model<Criteria> criteriaModel) {
            super(id, criteriaModel);
            add(text = new RichText("text", new PropertyModel<String>(criteriaModel, "instructions")) {
                @Override
                protected String getImagePath() {
                    return String.format("/criteria/%s/instructions/images/", criteriaModel.getObject().getId());
                }
            }.withAutoUpdate());
        }
    }

}
