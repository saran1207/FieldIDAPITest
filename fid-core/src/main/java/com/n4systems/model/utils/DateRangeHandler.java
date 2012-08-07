package com.n4systems.model.utils;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.TimeZone;

public interface DateRangeHandler extends Serializable {
	LocalDate getNowFrom();
	LocalDate getNowTo();
	Period getPeriod();
    void setTimeZone(TimeZone timeZone);
}
