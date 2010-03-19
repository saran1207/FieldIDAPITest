package com.n4systems.fieldidadmin.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ImportAllJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {		
		InspectionImporterJob inspectionImporter = new InspectionImporterJob();
		inspectionImporter.execute( arg0 );
	}

}
