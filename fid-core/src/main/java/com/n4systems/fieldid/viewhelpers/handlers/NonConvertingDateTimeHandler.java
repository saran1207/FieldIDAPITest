package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.model.utils.NonConvertingDateTime;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.time.DateUtil;

import java.util.Date;

public class NonConvertingDateTimeHandler extends DefaultHandler {

    public NonConvertingDateTimeHandler(TableGenerationContext action) {
        super(action);
    }

    public String handleWeb(Long entityId, Object cell) {
        String cellString = "";
        if (cell instanceof Date) {
            cellString = new FieldIdDateFormatter((Date)cell, contextProvider, false, !DateUtil.isMidnight((Date) cell)).format();
        }

        return cellString;
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        if (value instanceof Date) {
            return new NonConvertingDateTime((Date) value);
        }
        return super.handleExcel(entityId, value);
    }

}
