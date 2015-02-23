package com.n4systems.fieldid.wicket.components.observationCount;


import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;


/**
 * Created by rrana on 2015-02-02.
 */
public class ObservationCountCounterPanel extends Panel {

    private static int COUNTER_MIN = 0;
    private static int COUNTER_MAX = 999;

    private WebMarkupContainer widget;
    private int currentCount;

    private WebMarkupContainer minusButton;
    private WebMarkupContainer plusButton;

    private TextField count;

    public ObservationCountCounterPanel(String id, IModel<ObservationCountResult> result, boolean showButtons) {
        super(id);

        widget = new WebMarkupContainer("container1");
        widget.add(new Label("label", new PropertyModel<String>(result.getObject().getObservationCount(), "name")));

        count = new TextField("textField", new PropertyModel<>(result, "value"), Integer.class);
        count.setOutputMarkupId(true);

        //Make it read only if the flag is true
        if(!showButtons) {
            count.add(new AttributeAppender("readonly", new Model<String>("true"), ""));
        }

        //validate negative values
        RangeValidator validator = new RangeValidator<Integer>(COUNTER_MIN, COUNTER_MAX);
        count.add(validator);
        count.setRequired(true);

        count.add(new OnChangeAjaxBehavior() {

            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e)
            {
                result.getObject().setValue(0);
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                int value;
                try {
                    value = Integer.valueOf(count.getInput());
                    if(value >= 0) {
                        currentCount = value;
                        result.getObject().setValue(currentCount);
                        //target.add(count);
                    } else {
                        //set count's value to be the original
                        count.setConvertedInput(currentCount);
                        result.getObject().setValue(currentCount);
                        target.add(count);
                    }
                } catch (NumberFormatException e) {
                    result.getObject().setValue(currentCount);
                    target.add(count);
                }
            }
        });

        if (result.getObject().getObservationCount() == null) {
            currentCount = 0;
        } else {
            currentCount = result.getObject().getValue();
        }

        minusButton = new WebMarkupContainer("minusButton");
        minusButton.setOutputMarkupId(true);
        minusButton.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if(currentCount > COUNTER_MIN) {
                    currentCount = (currentCount - 1);
                    result.getObject().setValue(currentCount);
                    target.add(count);
                }
            }
        });

        minusButton.setVisible(showButtons);

        plusButton = new WebMarkupContainer("plusButton");
        plusButton.setOutputMarkupId(true);
        plusButton.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if(currentCount < COUNTER_MAX) {
                    currentCount = (currentCount + 1);
                    result.getObject().setValue(currentCount);
                    target.add(count);
                }
            }
        });
        plusButton.setVisible(showButtons);

        widget.add(minusButton);
        widget.add(count);
        widget.add(plusButton);

        add(widget);

    }
}
