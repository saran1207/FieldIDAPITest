package com.n4systems.model.utils;

import java.util.Date;

public class NonConvertingDateTime extends Date {

    public NonConvertingDateTime(Date date) {
        super(date.getTime());
    }

}
