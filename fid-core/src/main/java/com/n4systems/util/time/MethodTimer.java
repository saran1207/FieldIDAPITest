package com.n4systems.util.time;

import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class MethodTimer {

    private LocalDateTime start;
    private PeriodFormatter shortFormatter;
    private PeriodFormatter longFormatter;
    private Logger logger;
    private String format;
    private PeriodFormatter formatter;

    public MethodTimer() {
        shortFormatter = new PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(1)
                .appendSeconds()
                .appendSuffix(".", ".")
                .minimumPrintedDigits(3)
                .appendMillis()
                .appendSuffix(" sec"," sec")
                .toFormatter();
        longFormatter = new PeriodFormatterBuilder()
                .appendHours()
                .appendSuffix(" hr", " hrs")
                .printZeroAlways()
                .appendMinutes()
                .appendSuffix(" min", " mins")
                .minimumPrintedDigits(2)
                .appendSeconds()
                .appendSuffix(" sec"," sec")
                .toFormatter();
        start();
    }

    public MethodTimer withFormatter(PeriodFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public MethodTimer start() {
        start = LocalDateTime.now();
        return this;
    }

    public MethodTimer withLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public MethodTimer withFormat(String format) {
        this.format = format;
        return this;
    }

    public MethodTimer stop() {
        if (logger!=null) {
            logger.debug(getTimeString());
        } else {
            System.out.println(getTimeString());
        }
        return this;
    }

    public String getTimeString() {
        Period period = getPeriod();
        PeriodFormatter formatter = this.formatter;
        if (formatter==null) {
            formatter = period.getMinutes()>0 ? longFormatter : shortFormatter;
        }
        String value = formatter.print(period);
        return (format!=null) ? String.format(format, value) : value;
    }

    public Period getPeriod() {
        return new Period(start, LocalDateTime.now());
    }

}
