package com.n4systems.fieldid.wicket.pages.setup.score;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.score.validator.MustHaveAValueIfNotNAValidator;
import com.n4systems.model.Score;

@SuppressWarnings("serial")
public class NewScorePanel extends Panel {

    public NewScorePanel(String id, final IModel<Score> model) {
        super(id, model);

        setOutputMarkupId(true);
        add(new ScoreForm("scoreForm", model));
    }

    class ScoreForm extends Form<Score> {

		public ScoreForm(String id, final IModel<Score> model) {
            super(id, model);
            setOutputMarkupId(true);

            final TextField<Double> valueTextField;

            add(new RequiredTextField<String>("name", new PropertyModel<String>(model, "name")));

            add(valueTextField = new TextField<Double>("value", new PropertyModel<Double>(model, "value")) {
                @Override
                public boolean isVisible() {
                    return !model.getObject().isNa();
                }
            });
            valueTextField.setOutputMarkupId(true);
            valueTextField.setOutputMarkupPlaceholderTag(true);
      

            final DropDownChoice<Boolean> typeChoice;
            add(typeChoice = new DropDownChoice<Boolean>("typeChoice", new PropertyModel<Boolean>(model, "na"), Arrays.<Boolean>asList(false, true), new ScoreNaChoiceRenderer()));

            typeChoice.add(new UpdateComponentOnChange() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(valueTextField);
                    target.add(typeChoice);
                }
            });           

            valueTextField.add(new MustHaveAValueIfNotNAValidator(model));
        }

    }

    class ScoreNaChoiceRenderer implements IChoiceRenderer<Boolean> {
        @Override
        public Object getDisplayValue(Boolean object) {
            if (object) {
                return new FIDLabelModel("label.indicates_na").getObject();
            }
            return new FIDLabelModel("label.has_a_value_of").getObject();
        }

        @Override
        public String getIdValue(Boolean object, int index) {
            return object.toString();
        }
    }

}
