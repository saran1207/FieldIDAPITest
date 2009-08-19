package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.n4systems.importing.ImportManager;
import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ListHelper;
import com.n4systems.util.persistence.QueryBuilder;

public class ObservationImporterAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	
	private ImportManager importManager;
	
	private Long tenantId;
	private File observationCsv;
    private String observationCsvContentType;
    private String observationCsvFileName;
	
	public String doShow() {
		return SUCCESS;
	}
	
	public String doImport() {
		File observationFile = PathHandler.getTempFile(observationCsvFileName);
		
		try {
			// ensure the tmp dir exists
			observationFile.getParentFile().mkdirs();
			
			// copy the temp file to a more permanent temp.  observationCsv will be removed by Struts once the action has completed.
			FileUtils.copyFile(observationCsv, observationFile);
			
			long observationsImported = importManager.importObservations(tenantId, observationFile);
			
			addActionMessage("Imported Successful.  Imported " + observationsImported + " observations.");
			
		} catch(Exception e) {
			addActionError("Import Failed: " + e.getMessage());
			return ERROR;
		} finally {
			// clean up the import file
			observationFile.delete();
		}
		
		return SUCCESS;
	}

	
	public void setImportManager(ImportManager importManager) {
		this.importManager = importManager;
	}
	
	public Map<Long, String> getTenants() {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class);
		builder.setSimpleSelect();
		builder.setOrder("displayName");
		
		Map<Long, String> tenantMap = new HashMap<Long, String>();
		try {
			tenantMap = ListHelper.longListableToMap(persistenceManager.findAll(builder));
		} catch(Exception e) {
			addActionError("Unable to load Tenants: " + e.getMessage());
		}
		
		return tenantMap;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public File getObservationCsv() {
		return observationCsv;
	}

	public void setObservationCsv(File observationCsv) {
		this.observationCsv = observationCsv;
	}

	public String getObservationCsvContentType() {
		return observationCsvContentType;
	}

	public void setObservationCsvContentType(String observationCsvContentType) {
		this.observationCsvContentType = observationCsvContentType;
	}

	public String getObservationCsvFileName() {
		return observationCsvFileName;
	}

	public void setObservationCsvFileName(String observationCsvFileName) {
		this.observationCsvFileName = observationCsvFileName;
	}
	
}
