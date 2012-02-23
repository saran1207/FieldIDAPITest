package com.n4systems.fieldid.service.download;

import com.n4systems.model.orgs.BaseOrg;

import java.util.TimeZone;

public class TableGenerationContextImpl implements TableGenerationContext {

    private TimeZone timeZone;
    private String dateFormat;
    private String dateTimeFormat;
    private BaseOrg owner;

    public TableGenerationContextImpl(TimeZone timeZone, String dateFormat, String dateTimeFormat, BaseOrg owner) {
        this.timeZone = timeZone;
        this.dateFormat = dateFormat;
        this.dateTimeFormat = dateTimeFormat;
        this.owner = owner;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }
}
