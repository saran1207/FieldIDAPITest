package com.n4systems.fieldid.wicket.pages.setup.score.result;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.behavior.validation.ValidationBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.score.validator.SecondRangeValueValidator;
import com.n4systems.model.ScoreComparator;
import com.n4systems.model.ScoreResultRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

public class ScoreResultRangePanel extends Panel {

    public ScoreResultRangePanel(String id, final IModel<ScoreResultRange> model) {
        super(id, model);
        setOutputMarkupId(true);

        DropDownChoice<ScoreComparator> comparatorChoice;

        PropertyModel<ScoreComparator> comparatorModel = new PropertyModel<ScoreComparator>(model, "comparator");
        add(comparatorChoice = new DropDownChoice<ScoreComparator>("comparatorSelect", comparatorModel, Arrays.asList(ScoreComparator.values()), createComparatorRenderer()));
        comparatorChoice.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(ScoreResultRangePanel.this);
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
        value2Field.add(new SecondRangeValueValidator(comparatorModel, value1Field));
        ValidationBehavior.addValidationBehaviorToComponent(value1Field);
        ValidationBehavior.addValidationBehaviorToComponent(value2Field);
        EnclosureContainer enclosureContainer = new EnclosureContainer("enclosureContainer", value2Field);
        add(enclosureContainer);
        enclosureContainer.add(value2Field);
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

}
