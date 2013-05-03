package com.n4systems.fieldid.wicket.components.text;

import com.n4systems.fieldid.wicket.components.ComboBox;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;
import java.util.List;

public class LabelledComboBox<M> extends LabelledComponent<ComboBox,M>{


    public LabelledComboBox(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected ComboBox createLabelledComponent(String id, IModel<M> model) {
        return new ComboBox(id, (IModel<String>) model, getChoices());
    }

    protected IModel<List<String>> getChoices() {
        return new ListModel<String>(new ArrayList<String>());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("new toCombo('"+component.getInputName()+"');");
    }

    public void addBehavior(Behavior... behaviors) {
        component.add(behaviors);
    }
}
