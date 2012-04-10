package com.n4systems.fieldid.wicket.model.time;

import com.n4systems.fieldid.wicket.components.timezone.HourOfDaySelect;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class HourToStringDisplayModel extends LoadableDetachableModel<String> {

    private IModel<Integer> hourModel;

    public HourToStringDisplayModel(IModel<Integer> hourModel) {
        this.hourModel = hourModel;
    }
    
    @Override
    protected String load() {
        HourOfDaySelect.TimeChoiceRenderer renderer = new HourOfDaySelect.TimeChoiceRenderer();
        return renderer.getDisplayValue(hourModel.getObject()).toString();
    }

    @Override
    public void detach() {
        super.detach();
        hourModel.detach();
    }

}
