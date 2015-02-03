package com.n4systems.fieldid.wicket.components.observationCount;


import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


/**
 * Created by rrana on 2015-02-02.
 */
public class ObservationCountCounterPanel extends Panel {

    private WebMarkupContainer widget;
    private int currentCount;

    private WebMarkupContainer minusButton;
    private WebMarkupContainer plusButton;

    private TextField count;

    public ObservationCountCounterPanel(String id, IModel<ObservationCountResult> result, boolean showButtons) {
        super(id);

        widget = new WebMarkupContainer("container1");
        widget.add(new Label("label", new PropertyModel<String>(result.getObject().getObservationCount(), "name")));

        count = new TextField("textField", new PropertyModel<>(result, "value"));
        count.setOutputMarkupId(true);

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
                if(currentCount > 0) {
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
                currentCount = (currentCount + 1);
                result.getObject().setValue(currentCount);
                target.add(count);
            }
        });
        plusButton.setVisible(showButtons);

        widget.add(minusButton);
        widget.add(count);
        widget.add(plusButton);

        add(widget);

    }
}
