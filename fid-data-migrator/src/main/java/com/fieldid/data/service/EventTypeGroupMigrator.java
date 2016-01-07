package com.fieldid.data.service;

import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EventTypeGroupMigrator extends DataMigrator<EventTypeGroup> {

	public EventTypeGroupMigrator() {
		super(EventTypeGroup.class);
	}

	@Override
	@Transactional
	protected EventTypeGroup copy(EventTypeGroup srcGroup, Tenant newTenant, String newName) {
		EventTypeGroup dstGroup = new EventTypeGroup();
		dstGroup.setTenant(newTenant);
		dstGroup.setName(newName);
		dstGroup.setReportTitle(srcGroup.getReportTitle());
		dstGroup.setPrintOut(findByName(PrintOut.class, srcGroup.getPrintOut().getName(), newTenant));
		dstGroup.setObservationPrintOut(findByName(PrintOut.class, srcGroup.getObservationPrintOut().getName(), newTenant));
		persistenceService.save(dstGroup);
		return dstGroup;
	}

}
