package com.n4systems.fieldid.wicket.components.select;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedListableDropDownChoice extends GroupedDropDownChoice<GroupedListingPair,Object>  {

    @Override
    protected Object getGroup(GroupedListingPair choice) {
        return choice.getGroup();
    }

    @Override
    protected String getGroupLabel(Object group) {
        return group.toString();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new JChosenBehavior());
    }

    public GroupedListableDropDownChoice(String id) {
        super(id);
    }

    public GroupedListableDropDownChoice(String id, List<? extends GroupedListingPair> choices) {
        super(id, choices);
    }

    public GroupedListableDropDownChoice(String id, List<? extends GroupedListingPair> data, IChoiceRenderer<? super GroupedListingPair> iChoiceRenderer) {
        super(id, data, iChoiceRenderer);
    }

    public GroupedListableDropDownChoice(String id, IModel<GroupedListingPair> groupedListingPairIModel, List<? extends GroupedListingPair> choices) {
        super(id, groupedListingPairIModel, choices);
    }

    public GroupedListableDropDownChoice(String id, IModel<GroupedListingPair> groupedListingPairIModel, List<? extends GroupedListingPair> data, IChoiceRenderer<? super GroupedListingPair> iChoiceRenderer) {
        super(id, groupedListingPairIModel, data, iChoiceRenderer);
    }

    public GroupedListableDropDownChoice(String id, IModel<? extends List<? extends GroupedListingPair>> choices) {
        super(id, choices);
    }

    public GroupedListableDropDownChoice(String id, IModel<GroupedListingPair> groupedListingPairIModel, IModel<? extends List<? extends GroupedListingPair>> choices) {
        super(id, groupedListingPairIModel, choices);
    }

    public GroupedListableDropDownChoice(String id, IModel<? extends List<? extends GroupedListingPair>> choices, IChoiceRenderer<? super GroupedListingPair> iChoiceRenderer) {
        super(id, choices, iChoiceRenderer);
    }

    public GroupedListableDropDownChoice(String id, IModel<GroupedListingPair> groupedListingPairIModel, IModel<? extends List<? extends GroupedListingPair>> choices, IChoiceRenderer<? super GroupedListingPair> iChoiceRenderer) {
        super(id, groupedListingPairIModel, choices, iChoiceRenderer);
    }

}
