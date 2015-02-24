package com.n4systems.fieldid.wicket.pages.setup.observationcount;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.NoBarsValidator;
import com.n4systems.fieldid.wicket.util.NoColonsValidator;
import com.n4systems.model.ObservationCount;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

public class NewObservationCountPanel extends Panel {


    public NewObservationCountPanel(String id, IModel<ObservationCount> model) {
        super(id, model);
        add(new ObservationCountForm("observationCountForm", model));
    }

    private class ObservationCountForm extends Form<ObservationCount> {

        public ObservationCountForm(String id, IModel<ObservationCount> model) {
            super(id, model);
            setOutputMarkupId(true);
            RequiredTextField nameField;
            add(nameField = new RequiredTextField<String>("name", new PropertyModel<>(model, "name")));
            nameField.add(new NoBarsValidator());
            nameField.add(new NoColonsValidator());
            nameField.add(new StringValidator.MaximumLengthValidator(10));

            add(new DropDownChoice<Boolean>("counted",
                    new PropertyModel<>(model, "counted"),
                    Lists.newArrayList(Boolean.TRUE, Boolean.FALSE),
                    new IChoiceRenderer<Boolean>() {
                        @Override
                        public String getDisplayValue(Boolean object) {
                            return object ? new FIDLabelModel("label.yes").getObject() : new FIDLabelModel("label.no").getObject();
                        }

                        @Override
                        public String getIdValue(Boolean object, int index) {
                            return object.toString();
                        }
                    }));
        }
    }
}
