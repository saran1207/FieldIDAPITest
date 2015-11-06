package com.n4systems.fieldid.wicket.pages.setup.score.result;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.score.validator.SecondRangeValueValidator;
import com.n4systems.model.ResultRange;
import com.n4systems.model.ScoreComparator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

public abstract class ScoreResultRangePanel extends Panel {

    Label percentage1;
    Label percentage2;

    public ScoreResultRangePanel(String id, final IModel<ResultRange> model, boolean isPercentage) {
        super(id, model);
        setOutputMarkupId(true);

        FidDropDownChoice<ScoreComparator> comparatorChoice;

        PropertyModel<ScoreComparator> comparatorModel = new PropertyModel<ScoreComparator>(model, "comparator");
        add(comparatorChoice = new FidDropDownChoice<ScoreComparator>("comparatorSelect", comparatorModel, Arrays.asList(ScoreComparator.values()), createComparatorRenderer()));
        comparatorChoice.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(ScoreResultRangePanel.this);
            }
        });
        TextField<Double> value1Field;
        TextField<Double> value2Field;

        PropertyModel<Double> value1Model = new PropertyModel<Double>(model, "value1");
        add(value1Field = new RequiredTextField<Double>("value1", value1Model));
        add(value2Field = new TextField<Double>("value2", new PropertyModel<Double>(model, "value2")) {
            @Override
            public boolean isVisible() {
                return model.getObject().getComparator().isBinary();
            }
        });
        value2Field.add(new SecondRangeValueValidator(comparatorModel, value1Field) {
            @Override
            protected boolean validateMe() {
                return isValidationRequired();
            }
        });
        ValidationBehavior.addValidationBehaviorToComponent(value1Field);
        ValidationBehavior.addValidationBehaviorToComponent(value2Field);
        EnclosureContainer enclosureContainer = new EnclosureContainer("enclosureContainer", value2Field);
        add(enclosureContainer);

        percentage2 = new Label("percentage2", new FIDLabelModel("label.percent"));
        percentage2.setVisible(isPercentage);

        percentage1 = new Label("percentage1", new FIDLabelModel("label.percent"));
        percentage1.setVisible(isPercentage);
        add(percentage1);

        enclosureContainer.add(value2Field);
        enclosureContainer.add(percentage2);

    }

    public IChoiceRenderer<ScoreComparator> createComparatorRenderer() {
        return new IChoiceRenderer<ScoreComparator>() {
            @Override
            public Object getDisplayValue(ScoreComparator object) {
                return new FIDLabelModel(object.getLabel()).getObject();
            }

            @Override
            public String getIdValue(ScoreComparator object, int index) {
                return object.name();
            }
        };
    }

    public void showPercentage(boolean show) {
        percentage1.setVisible(show);
        percentage2.setVisible(show);
    }

    protected abstract boolean isValidationRequired();
}
