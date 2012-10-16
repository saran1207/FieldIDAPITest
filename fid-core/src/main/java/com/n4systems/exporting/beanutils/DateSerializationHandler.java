package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializationHandler extends SimpleSerializationHandler<Date>{

    TimeZone timeZone;

    public DateSerializationHandler(Field field) {
        super(field);
    }

    //JXL doesn't take daylights savings into consideration, we need to shift the time offset by one hour to correct this for dates that are not timezone sensitive.
    @Override
    public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
        Object cleanValue;
        if(value instanceof Date) {
            Date date = (Date) value;

            if(timeZone.inDaylightTime(date)) {
                cleanValue = new Date(date.getTime() + timeZone.getDSTSavings());
            } else
                cleanValue = date;
        } else {
            cleanValue = cleanImportValue(value);
        }
        setFieldValue(bean, cleanValue);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
