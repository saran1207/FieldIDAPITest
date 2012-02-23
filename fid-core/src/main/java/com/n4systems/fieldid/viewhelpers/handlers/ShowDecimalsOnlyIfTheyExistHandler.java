package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.util.DoubleFormatter;

public class ShowDecimalsOnlyIfTheyExistHandler extends WebOutputHandler {

    public ShowDecimalsOnlyIfTheyExistHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        if (!(value instanceof  Double)) {
            return value.toString();
        }
        return DoubleFormatter.simplifyDouble((Double) value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        if (!(value instanceof  Double)) {
            return value.toString();
        }
        return DoubleFormatter.simplifyDouble((Double) value);
    }

}

