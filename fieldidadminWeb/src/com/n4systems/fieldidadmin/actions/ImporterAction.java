package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.n4systems.fieldidadmin.jobs.EndUserImporterJob;
import com.n4systems.fieldidadmin.jobs.ImportAllJob;
import com.n4systems.fieldidadmin.jobs.InspectionImporterJob;
import com.n4systems.fieldidadmin.jobs.ProductSerialImporterJob;
import com.n4systems.fieldidadmin.utils.importer.EndUserImporter;
import com.n4systems.fieldidadmin.utils.importer.InspectionImporter;
import com.n4systems.fieldidadmin.utils.importer.ProductSerialImporter;
import com.n4systems.model.OrganizationalUnitType;
import com.n4systems.model.TenantOrganization;

public class ImporterAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;

	
	private Integer numberOfSerials;
	private Collection<TenantOrganization> tenants;
	private String tenantName;
	
	private File defaultDirectory = new File( "/var/fieldid/importer" );
	
	private Collection<File> importingFiles;
	private Collection<File> processingFiles;
	private Boolean createMissingDivisions;
	
	public boolean isCreateMissingDivisions() {
		return createMissingDivisions.booleanValue();
	}

	public void setCreateMissingDivisions( boolean createMissingDivisions) {
		this.createMissingDivisions = new Boolean( createMissingDivisions );
	}

	public ImporterAction() {
		super();
		tenantName = "nischain";
		createMissingDivisions = new Boolean( true );
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String manufacturer) {
		this.tenantName = manufacturer;
	}

	
	public Collection<File> getProcessingFiles() {
		return processingFiles;
	}

	public Collection<File> getImportingFiles() {
		return importingFiles;
	}

	public void setImportingFiles(Collection<File> importingFiles) {
		this.importingFiles = importingFiles;
	}

	public Collection<TenantOrganization> getTenants() {
		return tenants;
	}

	public Integer getNumberOfSerials() {
		return numberOfSerials;
	}

	public void setNumberOfSerials(Integer numberOfSerials) {
		this.numberOfSerials = numberOfSerials;
	}

	public String doList() {
		return SUCCESS;
	}
	
	public void fillManufacturers() {
		Map<String, Object> whereParams = new HashMap<String, Object>();
		whereParams.put("type", OrganizationalUnitType.ORGANIZATION);
		
		tenants = persistenceManager.findAll(TenantOrganization.class, whereParams);
	}
	
	public String listEndUserFiles() {
		fillManufacturers();
		TenantOrganization man = persistenceManager.findByName(TenantOrganization.class, tenantName);
		importingFiles = EndUserImporter.filesAvailableForProcessing( man, defaultDirectory );
		processingFiles = EndUserImporter.filesProcessing( man, defaultDirectory );
		return SUCCESS;
	}
	
	public String listProductSerialFiles() {
		fillManufacturers();
		TenantOrganization man = persistenceManager.findByName(TenantOrganization.class, tenantName);
		importingFiles = ProductSerialImporter.filesAvailableForProcessing( man, defaultDirectory );
		processingFiles = ProductSerialImporter.filesProcessing( man, defaultDirectory );
		return SUCCESS;
	}
	
	public String listInspectionFiles() {
		fillManufacturers();
		TenantOrganization man = persistenceManager.findByName(TenantOrganization.class, tenantName);
		importingFiles = InspectionImporter.filesAvailableForProcessing( man, defaultDirectory );
		processingFiles = InspectionImporter.filesProcessing( man, defaultDirectory );
		return SUCCESS;
	}
	
	public String listAllFiles() {
		fillManufacturers();
		TenantOrganization man = persistenceManager.findByName(TenantOrganization.class, tenantName);
		importingFiles = new ArrayList<File>();
		processingFiles = new ArrayList<File>();
		importingFiles.addAll( EndUserImporter.filesAvailableForProcessing( man, defaultDirectory ) );
		processingFiles.addAll( EndUserImporter.filesProcessing( man, defaultDirectory ) );
		importingFiles.addAll( ProductSerialImporter.filesAvailableForProcessing( man, defaultDirectory ) );
		processingFiles.addAll( ProductSerialImporter.filesProcessing( man, defaultDirectory ) );
		importingFiles.addAll( InspectionImporter.filesAvailableForProcessing( man, defaultDirectory ) );
		processingFiles.addAll( InspectionImporter.filesProcessing( man, defaultDirectory ) );
		
		return SUCCESS;
	}
	
	public String doImportEndUsers() {
		JobDataMap map = new JobDataMap();
		
		map.put( "tenantName", tenantName);
		map.put( "defaultDirectory", defaultDirectory);
		map.put( "createMissingDivision", createMissingDivisions );
		
		schedule( "endUserImporter", EndUserImporterJob.class, map, "endUserImporter" );
				
		return listEndUserFiles();
	}
	
	public String doImportAll() {
		JobDataMap map = new JobDataMap();

		map.put( "defaultDirectory", defaultDirectory);
		map.put( "tenantName", tenantName);
		map.put( "createMissingDivision", createMissingDivisions );
		
		schedule( "allDataImporter", ImportAllJob.class, map, "allDataImporter" );
		
		return listAllFiles();
	}
	
	public String doImportProductSerials() {
		
		JobDataMap map = new JobDataMap();
		
		map.put( "tenantName", tenantName);
		map.put( "defaultDirectory", defaultDirectory);
		map.put( "createMissingDivision", createMissingDivisions );
		
		schedule( "productSerialImporter", ProductSerialImporterJob.class, map, "productSerialImporter" );
		
		return listProductSerialFiles();
	}
	

	public String doImportInspections() {
		JobDataMap map = new JobDataMap();

		map.put( "defaultDirectory", defaultDirectory);
		map.put( "tenantName", tenantName);
		map.put( "createMissingDivision", createMissingDivisions );
		
		schedule( "inspectionImporter", InspectionImporterJob.class, map, "inspectionImporter" );
		
		return listInspectionFiles();
	}
	
	private void schedule( String jobName, Class<?> targetJob, JobDataMap map, String triggerName) {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
		try {
			Scheduler sched = schedFact.getScheduler();
			
			if( sched.isShutdown() ) {
				sched.start();
			}
	
			JobDetail jobDetail = new JobDetail( jobName, null, targetJob );
			
			Trigger trigger = new SimpleTrigger( triggerName, "Importer", new Date() );
			trigger.setJobDataMap( map );
			sched.scheduleJob( jobDetail, trigger );
			
		} catch (SchedulerException s ) {
			System.err.println( "scheduler error ");
			s.printStackTrace();
		}
		
	}
	
}
