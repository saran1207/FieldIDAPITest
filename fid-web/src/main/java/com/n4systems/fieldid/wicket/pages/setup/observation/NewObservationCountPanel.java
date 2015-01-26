package com.n4systems.fieldid.wicket.pages.setup.observation;

import com.n4systems.model.ObservationCount;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class NewObservationCountPanel extends Panel {


    public NewObservationCountPanel(String id, IModel<ObservationCount> model) {
        super(id, model);
        add(new ObservationCountForm("observationCountForm", model));
    }

    private class ObservationCountForm extends Form<ObservationCount> {

        public ObservationCountForm(String id, IModel<ObservationCount> model) {
            super(id, model);
            setOutputMarkupId(true);

            add(new RequiredTextField<String>("name", new PropertyModel<>(model, "name")));

/*            FidDropDownChoice<Boolean> includeChoice;

            add(includeChoice = new FidDropDownChoice<Boolean>("counted",
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
            includeChoice.add(new UpdateComponentOnChange());*/



        }
    }
}
