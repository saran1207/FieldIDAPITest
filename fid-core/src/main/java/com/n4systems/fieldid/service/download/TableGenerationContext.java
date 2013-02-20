package com.n4systems.fieldid.service.download;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.DateTimeDefinition;

import java.util.TimeZone;

public interface TableGenerationContext extends DateTimeDefinition {

    public TimeZone getTimeZone();

    public String getDateFormat();

    public String getDateTimeFormat();

    public BaseOrg getOwner();

}
