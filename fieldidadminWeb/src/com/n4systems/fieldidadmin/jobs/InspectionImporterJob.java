package com.n4systems.fieldidadmin.jobs;

import java.io.File;
import java.util.Collection;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.n4systems.fieldidadmin.utils.importer.InspectionImporter;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;


public class InspectionImporterJob implements Job {
		
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		JobDataMap map = arg0.getTrigger().getJobDataMap();
		
		try {
			
			File defaultDirectory = (File)map.get( "defaultDirectory" );
			String tenantName = (String)map.get( "tenantName" );
			Boolean createMissingDivision = (Boolean)map.get( "createMissingDivision" );
			
			Tenant tenant = TenantCache.getInstance().findTenant(tenantName);
			PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(tenant.getId());
			
			InspectionImporter importer = new InspectionImporter(defaultDirectory, primaryOrg, createMissingDivision.booleanValue());
			
			Collection<File> importingFiles = InspectionImporter.filesAvailableForProcessing( tenant, defaultDirectory );
			for (File targetFile : importingFiles) {
				importer.processFile( targetFile );
			}
			
		} catch (Exception e ) {
			System.err.println( "failed inspection importer  " + e.getMessage() );
		}

	}

}
