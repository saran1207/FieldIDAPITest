package com.n4systems.model.utils;

import java.util.Date;

public class DummyDate extends Date {
    public DummyDate(Date nextDate) {
        super(nextDate.getTime());
    }
}
