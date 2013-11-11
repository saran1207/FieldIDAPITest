package com.n4systems.fieldid.wicket.components.text;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.model.IModel;

import java.util.Collection;
import java.util.List;

public class LabelledDropDown<M> extends LabelledComponent<AbstractChoice,M> {

    public LabelledDropDown(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected AbstractChoice<Collection<M>,M> createLabelledComponent(String id, IModel<M> model) {
        return new FidDropDownChoice(id, model, getChoices());
    }

    protected List<M> getChoices() {
        return Lists.newArrayList();
    }

}
