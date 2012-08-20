package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.model.utils.NonConvertingDateTime;

import java.util.Date;

public class NonConvertingDateTimeHandler extends DefaultHandler {

    public NonConvertingDateTimeHandler(TableGenerationContext action) {
        super(action);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        if (value instanceof Date) {
            return new NonConvertingDateTime((Date) value);
        }
        return super.handleExcel(entityId, value);
    }

}
