package com.n4systems.model.utils;

import java.io.Serializable;

import org.joda.time.LocalDate;

public interface DateRangeHandler extends Serializable {
	LocalDate getNowFrom();
	LocalDate getNowTo();
}
