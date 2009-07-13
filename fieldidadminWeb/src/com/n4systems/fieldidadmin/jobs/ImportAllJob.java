package com.n4systems.fieldidadmin.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ImportAllJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		EndUserImporterJob enduserImporter = new EndUserImporterJob();
		enduserImporter.execute( arg0 );
		
		ProductSerialImporterJob productSerialImporter = new ProductSerialImporterJob();
		productSerialImporter.execute( arg0 );
		
		InspectionImporterJob inspectionImporter = new InspectionImporterJob();
		inspectionImporter.execute( arg0 );
	}

}
