package com.n4systems.reporting.mapbuilders;

import com.n4systems.fieldid.certificate.model.Job;
import com.n4systems.model.Project;
import com.n4systems.persistence.Transaction;

public class JobCertificateDataProducer extends AbstractMapBuilder<Project>{

	@Override
	protected void setAllFields(Project entity, Transaction transaction) {
		if (entity != null) {
			setField(ReportField.JOB, new Job(entity.getProjectID(), entity.getName()));
		}
	}

	
}
