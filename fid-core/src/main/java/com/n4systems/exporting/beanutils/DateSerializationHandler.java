package com.n4systems.exporting.beanutils;

import com.n4systems.util.DateHelper;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializationHandler extends SimpleSerializationHandler<Date>{

    TimeZone timeZone;

    public DateSerializationHandler(Field field) {
        super(field);
    }

    @Override
    public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
        Object cleanValue;
        if(value instanceof Date) {
            cleanValue = DateHelper.convertToUserTimeZone((Date)value, timeZone);
        } else {
            cleanValue = cleanImportValue(value);
        }
        setFieldValue(bean, cleanValue);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
