package com.n4systems.fieldidadmin.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.n4systems.api.validation.ValidationFailedException;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.exporting.CustomerExporter;
import com.n4systems.exporting.CustomerImporter;
import com.n4systems.exporting.ExportException;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.io.CsvMapWriter;
import com.n4systems.exporting.io.ExcelMapReader;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Tenant;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ListHelper;

public class CustomerDivisionExportAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(CustomerDivisionExportAction.class);
	
	private InputStream stream;
	private Tenant tenant;
	private File importFile;
	private int fileSize;
	
	public String doShow() {
		return SUCCESS;
	}
	
	public String doImport() throws FileNotFoundException {
		try {
			SecurityFilter filter = new TenantOnlySecurityFilter(tenant.getId());

			MapReader mapReader = new ExcelMapReader(new FileInputStream(importFile));
			
			CustomerImporter importer = new CustomerImporter(mapReader, filter);

			importer.validateAndImport();
		} catch (ValidationFailedException e) {
			for (ValidationResult result: e.getFailedValidationResults()) {
				addActionError(String.format("Row %d: %s", result.getRow(), result.getMessage()));
			}
			return ERROR;
		} catch (Exception e) {
			logger.error("Import customer/divions failed", e);
			addActionError("Oh-o, it failed.  Check the logs");
			return ERROR;
		} finally {
			importFile.delete();
		}
		
		addActionMessage("Cool it worked");
		return SUCCESS;
	}
	
	public String doExport() {
		try {
			SecurityFilter filter = new TenantOnlySecurityFilter(tenant.getId());
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			
			Exporter exporter = new CustomerExporter(new CustomerOrgListLoader(filter), filter);
			exporter.export(new CsvMapWriter(byteOut));
			
			byte[] output = byteOut.toByteArray();
			
			fileSize = output.length;
			stream = new ByteArrayInputStream(output);
			
		} catch(ExportException e) {
			logger.error("Export all customer/divions failed", e);
			return ERROR;
		}
		
		return SUCCESS;
	}

	public Map<Long, String> getTenants() {
		return ListHelper.longListableToMap(TenantCache.getInstance().findAllTenants());
	}

	public Long getTenantId() {
		return tenant.getId();
	}

	public void setTenantId(Long tenantId) {
		this.tenant = TenantCache.getInstance().findTenant(tenantId);
	}
	
	public void setUpload(File importFile) {
		this.importFile = importFile;
	}
	
	public String getOutFileName() {
		return ContentType.CSV.prepareFileName(tenant.getName() + "_customers");
	}
	
	public String getContentType() {
		return ContentType.CSV.getMimeType();
	}
	
	public String getFileSize() {
		return String.valueOf(fileSize);
	}
	
	public InputStream getFileStream() {
		return stream;
	}
}
