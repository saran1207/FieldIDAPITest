package com.n4systems.ejb.impl;

import java.util.Date;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;

public interface LastEventDateFinder {

	public Date findLastEventDate(Asset asset);

	public Date findLastEventDate(Long scheduleId);

	public Date findLastEventDate(EventSchedule schedule);

	public Date findLastEventDate(Asset asset, InspectionType inspectionType);

}