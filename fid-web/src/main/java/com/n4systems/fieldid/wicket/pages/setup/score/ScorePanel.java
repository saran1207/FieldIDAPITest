package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.model.Score;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ScorePanel extends Panel {

    private IModel<Score> model;
    private TextField<String> nameTextField;
    private TextField<Double> valueTextField;

    public ScorePanel(String id, final IModel<Score> model) {
        super(id, model);
        this.model = model;

        setOutputMarkupId(true);
        add(nameTextField = new TextField<String>("name", new PropertyModel<String>(model, "name")));
        add(valueTextField = new TextField<Double>("value", new PropertyModel<Double>(model, "value")));

        addBlurSubmitBehavior(nameTextField);
        addBlurSubmitBehavior(valueTextField);

        add(new AjaxCheckBox("na", new PropertyModel<Boolean>(model, "na")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                valueTextField.setEnabled(!model.getObject().isNa());
                model.getObject().setValue(null);
                target.addComponent(ScorePanel.this);
            }
        });
    }

    public void addBlurSubmitBehavior(TextField textField) {
        textField.add(new AjaxFormSubmitBehavior("onblur") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }
        });
    }

    public IModel<Score> getModel() {
        return model;
    }

}
