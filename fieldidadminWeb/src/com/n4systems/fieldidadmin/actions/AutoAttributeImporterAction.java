package com.n4systems.fieldidadmin.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.n4systems.fieldidadmin.utils.DualListView;
import com.n4systems.importing.ImportManager;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Listable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;

public class AutoAttributeImporterAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	
	private ImportManager importManager;
	
	private Long tenantId;
	private Long productTypeId;

	private File autoAttributesCsv;
    private String autoAttributesCsvContentType;
    private String autoAttributesCsvFileName;
	
    List<DualListView> tenantViewCache;
    
	public String doShow() {
		return SUCCESS;
	}
	
	public String doImport() {
		File autoAttributeFile = PathHandler.getTempFile(autoAttributesCsvFileName);
		
		try {
			// ensure the tmp dir exists
			autoAttributeFile.getParentFile().mkdirs();
			
			// copy the temp file to a more permanent temp.  observationCsv will be removed by Struts once the action has completed.
			FileUtils.copyFile(autoAttributesCsv, autoAttributeFile);
			
			long attributesImported = importManager.importAutoAttributes(productTypeId, autoAttributeFile);
			
			addActionMessage("Imported Successful.  Imported " + attributesImported + " auto-attributes.");
			
		} catch(Exception e) {
			String errorMessage = "Import Failed: " + e.getMessage();
			if(e.getCause() != null) {
				errorMessage += " (" + e.getCause().getMessage() + ")";
			}
			addActionError(errorMessage);
			return ERROR;
		} finally {
			// clean up the import file
			autoAttributeFile.delete();
		}
		
		return SUCCESS;
	}

	
	public void setImportManager(ImportManager importManager) {
		this.importManager = importManager;
	}
	
	public List<DualListView> getTenantView() {
		if(tenantViewCache == null) {
			tenantViewCache = loadTenantView();
		}
		return tenantViewCache;
	}
	
	public List<DualListView> loadTenantView() {
		QueryBuilder<Listable<Long>> tenantBuilder = new QueryBuilder<Listable<Long>>(Tenant.class, new OpenSecurityFilter());
		tenantBuilder.setSimpleSelect();
		tenantBuilder.setOrder("name");
		
		QueryBuilder<Listable<Long>> typeBuilder = new QueryBuilder<Listable<Long>>(ProductType.class, new OpenSecurityFilter());
		typeBuilder.setSimpleSelect();
		typeBuilder.setOrder("name");
		
		List<DualListView> tenantListView = new ArrayList<DualListView>();
		try {
			
			for(Listable<Long> tenant: persistenceManager.findAll(tenantBuilder)) {
				typeBuilder.addSimpleWhere("tenant.id", tenant.getId());
				tenantListView.add(new DualListView(tenant.getId(), tenant.getDisplayName(), persistenceManager.findAll(typeBuilder)));
			}
			
		} catch(Exception e) {
			addActionError("Unable to load Tenants: " + e.getMessage());
		}
		
		return tenantListView;
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	
	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public File getAutoAttributesCsv() {
		return autoAttributesCsv;
	}

	public void setAutoAttributesCsv(File observationCsv) {
		this.autoAttributesCsv = observationCsv;
	}

	public String getAutoAttributesCsvContentType() {
		return autoAttributesCsvContentType;
	}

	public void setAutoAttributesCsvContentType(String observationCsvContentType) {
		this.autoAttributesCsvContentType = observationCsvContentType;
	}

	public String getAutoAttributesCsvFileName() {
		return autoAttributesCsvFileName;
	}

	public void setAutoAttributesCsvFileName(String observationCsvFileName) {
		this.autoAttributesCsvFileName = observationCsvFileName;
	}
	
}
