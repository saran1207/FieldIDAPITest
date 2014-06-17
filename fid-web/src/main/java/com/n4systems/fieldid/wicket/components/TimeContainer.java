package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.util.EnumPropertyChoiceRenderer;
import com.n4systems.model.RecurrenceTimeOfDay;
import com.n4systems.model.RecurrenceType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import java.util.Arrays;
import java.util.List;

public class TimeContainer extends WebMarkupContainer {

    private FidDropDownChoice singleTime;
    private MultiSelectDropDownChoice<RecurrenceTimeOfDay> multipleTime;

    public TimeContainer(String id, IModel<RecurrenceTimeOfDay> timeOfDayModel, IModel<List<RecurrenceTimeOfDay>> timeofDayListModel) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        singleTime = new FidDropDownChoice<RecurrenceTimeOfDay>("time", timeOfDayModel, Arrays.asList(RecurrenceTimeOfDay.values()), new EnumPropertyChoiceRenderer<RecurrenceTimeOfDay>());
        add(singleTime.setNullValid(true).setOutputMarkupId(true));
        multipleTime = new MultiSelectDropDownChoice<RecurrenceTimeOfDay>("multipleTimes", timeofDayListModel, Arrays.asList(RecurrenceTimeOfDay.values()), new EnumPropertyChoiceRenderer<RecurrenceTimeOfDay>());
        add(multipleTime.setOutputMarkupId(true));

        singleTime.add(new UpdateComponentOnChange());
        multipleTime.add(new UpdateComponentOnChange());
    }

    public void updateComponents(RecurrenceType recurrenceType) {
        toggle(recurrenceType.canHaveMultipleTimes());
    }

    private void toggle(boolean b) {
        multipleTime.setVisible(b);
        singleTime.setVisible(!b);
    }

    public MultiSelectDropDownChoice<RecurrenceTimeOfDay> getMultipleTime() {
        return multipleTime;
    }

    public FidDropDownChoice getSingleTime() {
        return singleTime;
    }
}

