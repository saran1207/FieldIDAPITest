package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

public class FidDropDownChoice<T> extends DropDownChoice<T>{
    
    private String placeholder = " ";

    public FidDropDownChoice(String id) {
        super(id);
    }

    public FidDropDownChoice(String id, List<? extends T> choices) {
        super(id, choices);
    }

    public FidDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, choices, iChoiceRenderer);
    }

    public FidDropDownChoice(String id, IModel<T> tiModel, List<? extends T> choices) {
        super(id, tiModel, choices);
    }

    public FidDropDownChoice(String id, IModel<T> tiModel, List<? extends T> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, tiModel, choices, iChoiceRenderer);
    }

    public FidDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
    }

    public FidDropDownChoice(String id, IModel<T> tiModel, IModel<? extends List<? extends T>> choices) {
        super(id, tiModel, choices);
    }

    public FidDropDownChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, choices, iChoiceRenderer);
    }

    public FidDropDownChoice(String id, IModel<T> tiModel, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, tiModel, choices, iChoiceRenderer);
    }

    private void addJChosenBehavior() {
        add(new AttributeAppender("data-placeholder", placeholder));
        add(new JChosenBehavior());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addJChosenBehavior();
    }
}
