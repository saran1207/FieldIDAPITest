package com.n4systems.fieldid.wicket.components.select;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import java.util.List;

// A subclass of DropDownChoice that allows the use of <optgroup> tags.
// To use, the model should provide a list of items already sorted in an order that groups the items properly
public abstract class GroupedDropDownChoice<T,G> extends DropDownChoice<T> {

    protected G currentGroup;

    @Override
    protected void appendOptionHtml(AppendingStringBuffer buffer, T choice, int index, String selected) {
        beginGroupIfNecessary(buffer, choice);
        super.appendOptionHtml(buffer, choice, index, selected);
        endGroupIfAtEndOfList(buffer, index);
    }

    protected void beginGroupIfNecessary(AppendingStringBuffer buffer, T choice) {
        G nextGroup = getGroup(choice);
        if (currentGroup == null || !currentGroup.equals(nextGroup)) {
            if (currentGroup != null) {
                buffer.append("</optgroup>");
            }
            if (!(currentGroup == null && nextGroup == null)) {
                buffer.append("<optgroup label=\""+getGroupLabel(nextGroup)+"\">");
            }

        }
        currentGroup = nextGroup;
    }

    protected void endGroupIfAtEndOfList(AppendingStringBuffer buffer, int index) {
        if (index == getChoices().size() - 1) {
            buffer.append("</optgroup>");
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        currentGroup = null;
    }

    protected abstract G getGroup(T choice);

    protected abstract String getGroupLabel(G group);

    public GroupedDropDownChoice(String id) {
        super(id);
    }

    public GroupedDropDownChoice(String id, List<? extends T> choices) {
        super(id, choices);
    }

    public GroupedDropDownChoice(String id, List<? extends T> data, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, data, iChoiceRenderer);
    }

    public GroupedDropDownChoice(String id, IModel<T> tiModel, List<? extends T> choices) {
        super(id, tiModel, choices);
    }

    public GroupedDropDownChoice(String id, IModel<T> tiModel, List<? extends T> data, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, tiModel, data, iChoiceRenderer);
    }

    public GroupedDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
    }

    public GroupedDropDownChoice(String id, IModel<T> tiModel, IModel<? extends List<? extends T>> choices) {
        super(id, tiModel, choices);
    }

    public GroupedDropDownChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, choices, iChoiceRenderer);
    }

    public GroupedDropDownChoice(String id, IModel<T> tiModel, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> iChoiceRenderer) {
        super(id, tiModel, choices, iChoiceRenderer);
    }

}
