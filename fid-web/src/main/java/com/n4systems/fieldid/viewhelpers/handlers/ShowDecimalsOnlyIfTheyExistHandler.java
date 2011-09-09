package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;

public class ShowDecimalsOnlyIfTheyExistHandler extends WebOutputHandler {

    protected ShowDecimalsOnlyIfTheyExistHandler(WebContextProvider contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        if (!(value instanceof  Double)) {
            return value.toString();
        }
        return simplifyDouble((Double) value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        if (!(value instanceof  Double)) {
            return value.toString();
        }
        return simplifyDouble((Double) value);
    }

    public String simplifyDouble(Double d) {
        if (d - d.intValue() != 0) {
            return d.toString();
        } else {
            return d.intValue()+"";
        }
    }

}

