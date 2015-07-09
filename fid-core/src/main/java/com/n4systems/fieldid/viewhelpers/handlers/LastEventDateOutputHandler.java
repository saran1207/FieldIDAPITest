package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.time.DateUtil;

import java.util.Date;

/**
 * This WebOutputHandler is used to PROPERLY localize the Last Event Date column for Assets in the Asset Search.
 *
 * For some reason, localization isn't working as expected in the DateTimeHandler, so this will fix the issue without
 * impacting the multitude of other places DateTimeHandler is used.
 *
 * Created by Jordan Heath on 2015-07-07.
 */
public class LastEventDateOutputHandler extends WebOutputHandler {
    public LastEventDateOutputHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        String cellString = "";
        if(value instanceof Date) {
//            value = DateHelper.localizeDate((Date) value, contextProvider.getTimeZone());
            cellString = new FieldIdDateFormatter(((Date)value), contextProvider, true, !DateUtil.isMidnight((Date) value)).format();
        }

        return cellString;
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return value;
    }
}
