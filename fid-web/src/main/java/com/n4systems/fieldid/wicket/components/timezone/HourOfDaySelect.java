package com.n4systems.fieldid.wicket.components.timezone;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.Arrays;
import java.util.List;

public class HourOfDaySelect extends DropDownChoice<Integer> {

    public HourOfDaySelect(String id, IModel<Integer> integerIModel) {
        super(id, integerIModel, createTimeChoicesModel(), new TimeChoiceRenderer());
        setNullValid(false);
    }
    
    private static List<Integer> createTimeChoicesModel() {
        return Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
    }
    
    public static class TimeChoiceRenderer implements IChoiceRenderer<Integer> {
        @Override
        public Object getDisplayValue(Integer object) {
            if (object < 12) {
                return (object == 0 ? "12" : object) + ":00 am";
            }
            return (object-12 == 0 ? "12" : object-12) + ":00 pm";
        }

        @Override
        public String getIdValue(Integer object, int index) {
            return object.toString();
        }
    }

}
