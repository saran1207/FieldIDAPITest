package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.SimpleFrequency;
import org.apache.wicket.model.IModel;

import java.util.Arrays;

public class FrequencyDropDownChoice extends GroupedDropDownChoice<SimpleFrequency, String> {

    public FrequencyDropDownChoice(String id, IModel<SimpleFrequency> model) {
        super(id, model, Arrays.asList(SimpleFrequency.values()), new ListableLabelChoiceRenderer<SimpleFrequency>());
    }

    @Override
    protected String getGroup(SimpleFrequency choice) {
        return choice.getGroupLabel();
    }

    @Override
    protected String getGroupLabel(String group) {
        return new FIDLabelModel(group).getObject();
    }

}
