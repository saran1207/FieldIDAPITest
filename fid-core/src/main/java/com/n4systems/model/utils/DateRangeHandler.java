package com.n4systems.model.utils;

import java.io.Serializable;

import org.joda.time.LocalDate;
import org.joda.time.Period;

public interface DateRangeHandler extends Serializable {
	LocalDate getNowFrom();
	LocalDate getNowTo();
	Period getPeriod();
}
