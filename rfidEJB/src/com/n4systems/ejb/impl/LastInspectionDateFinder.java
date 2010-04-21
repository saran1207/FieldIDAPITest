package com.n4systems.ejb.impl;

import java.util.Date;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;

public interface LastInspectionDateFinder {

	public Date findLastInspectionDate(Product product);

	public Date findLastInspectionDate(Long scheduleId);

	public Date findLastInspectionDate(InspectionSchedule schedule);

	public Date findLastInspectionDate(Product product, InspectionType inspectionType);

}