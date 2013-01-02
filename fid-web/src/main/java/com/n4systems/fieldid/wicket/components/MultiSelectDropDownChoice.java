package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public class MultiSelectDropDownChoice<T extends Serializable> extends ListMultipleChoice<T> {

    private static final Logger logger= Logger.getLogger(MultiSelectDropDownChoice.class);

    public MultiSelectDropDownChoice(String id, IModel<List<T>> model, List<T> choices) {
        super(id, model, choices);
    }

    public MultiSelectDropDownChoice(String id, IModel<List<T>> model, List<T> choices, IChoiceRenderer<T> renderer) {
        super(id,model,choices,renderer);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new JChosenBehavior());
    }

    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        final CharSequence multiple = tag.getAttribute("multiple");
        if (multiple==null || !multiple.equals("multiple")) {
            logger.error("you must use the 'multiple=\"multiple\"' attribute for this type of select : " + getId());
        }
    }

}
