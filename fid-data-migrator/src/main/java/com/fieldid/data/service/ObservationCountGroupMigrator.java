package com.fieldid.data.service;

import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountGroup;
import com.n4systems.model.Tenant;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.n4systems.util.FunctionalUtils.bind;
import static com.n4systems.util.FunctionalUtils.map;

@Component
public class ObservationCountGroupMigrator extends DataMigrator<ObservationCountGroup> {

	public ObservationCountGroupMigrator() {
		super(ObservationCountGroup.class);
	}

	@Override
	@Transactional
	protected ObservationCountGroup copy(ObservationCountGroup srcGroup, Tenant newTenant, String newName) {
		ObservationCountGroup dstGroup = new ObservationCountGroup();
		dstGroup.setTenant(newTenant);
		dstGroup.setName(newName);
		dstGroup.getObservationCounts().addAll(map(srcGroup.getObservationCounts(), bind(this::migrateObservationCount, newTenant)));
		persistenceService.save(dstGroup);
		return dstGroup;
	}

	private ObservationCount migrateObservationCount(Tenant newTenant, ObservationCount srcCount) {
		ObservationCount dstCount = new ObservationCount();
		dstCount.setTenant(newTenant);
		dstCount.setName(srcCount.getName());
		dstCount.setCounted(srcCount.isCounted());
		return dstCount;
	}

}
