package com.n4systems.ejb.impl;

import com.n4systems.model.Asset;
import com.n4systems.model.EventType;

import java.util.Date;

@Deprecated
public interface LastEventDateFinder {

    public Date findLastEventDate(Asset asset);

    public Date findLastEventDate(Long eventId);

    public Date findLastEventDate(Asset asset, EventType eventType);

}