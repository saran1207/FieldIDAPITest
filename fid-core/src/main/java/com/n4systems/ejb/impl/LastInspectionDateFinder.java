package com.n4systems.ejb.impl;

import java.util.Date;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Asset;

public interface LastInspectionDateFinder {

	public Date findLastInspectionDate(Asset asset);

	public Date findLastInspectionDate(Long scheduleId);

	public Date findLastInspectionDate(InspectionSchedule schedule);

	public Date findLastInspectionDate(Asset asset, InspectionType inspectionType);

}