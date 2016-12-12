package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.log4j.Logger;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.Date;

public class InfoOptionBeanPropertyModel extends PropertyModel<String> {

    private static final Logger logger= Logger.getLogger(InfoOptionBeanPropertyModel.class);

    private InfoFieldBean.InfoFieldType type;
    private boolean includeTime;
    private DateTimeDefinition dateTimeDefinition;

    public InfoOptionBeanPropertyModel(InfoOptionBean modelObject, String expression, DateTimeDefinition dateTimeDefinition) {
        super(modelObject, expression);
        this.type = modelObject.getInfoField().getType();
        this.includeTime = modelObject.getInfoField().isIncludeTime();
        this.dateTimeDefinition = dateTimeDefinition;
    }

    @Override
    public String getObject() {
        String value = super.getObject();
        switch (type) {
            case DateField:  // format as date. the rest we just leave as strings.
                return formatMsStringAsDate(value);
            default:
                return value;
        }
    }

    private String formatMsStringAsDate(String value) {
        try {
            long ms = Long.parseLong(value);
            // Flat date without times should not be timezone converted

            return new FieldIdDateFormatter(new Date(ms),dateTimeDefinition, includeTime, includeTime).format();
        } catch (NumberFormatException e) {
            logger.error("can't parse date from expected MS string '" + value + "'");
            return value;
        }
    }


}
