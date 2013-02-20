package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.ServiceLocator;

import java.util.Date;

public class LastEventDateHandler extends WebOutputHandler {

    public LastEventDateHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        Date cell = ServiceLocator.getLastEventDateService().findLastEventDate(entityId);
        String cellString = "";
        if (cell != null) {
            cellString = new FieldIdDateFormatter(cell, contextProvider, true, true).format();
        }

        return cellString;
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return ServiceLocator.getLastEventDateService().findLastEventDate(entityId);
    }

}
