package com.n4systems.fieldid.wicket.components.observationCount;


import com.n4systems.model.ObservationCountResult;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


/**
 * Created by rrana on 2015-02-02.
 */
public class ObservationCountCounterPanel extends Panel {

    private WebMarkupContainer widget;
    private int currentCount;
    private boolean showButtons;
    private Link minusButton;
    private Link plusButton;
    private TextField count;

    public ObservationCountCounterPanel(String id, IModel<ObservationCountResult> result, boolean showButtons) {
        super(id);

        this.showButtons = showButtons;

        widget = new WebMarkupContainer("container");

        widget.add(new Label("label", new PropertyModel<String>(result.getObject().getObservationCount(), "name")));

        count = new TextField("textField", new PropertyModel<>(result, "value"));

        if (result.getObject().getObservationCount() == null) {
            currentCount = 0;
        } else {
            currentCount = result.getObject().getValue();
        }

        minusButton = new Link("minusButton") {
            @Override
            public void onClick() {
                currentCount = (currentCount - 1);
                result.getObject().setValue(currentCount);
            }
        };
        minusButton.setOutputMarkupId(true);
        minusButton.setVisible(showButtons);

        plusButton = new Link("plusButton") {
            @Override
            public void onClick() {
                currentCount = (currentCount + 1);
                result.getObject().setValue(currentCount);
            }
        };
        plusButton.setOutputMarkupId(true);
        plusButton.setVisible(showButtons);

        widget.add(minusButton);
        widget.add(count);
        widget.add(plusButton);

        add(widget);
    }
}
