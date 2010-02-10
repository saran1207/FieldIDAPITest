package com.n4systems.fieldidadmin.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.CustomerOrgViewConverter;
import com.n4systems.api.conversion.DivisionOrgViewConverter;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.exporting.CustomerCSVExporter;
import com.n4systems.exporting.ExportException;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.beanutils.ExportMapUnmarshaler;
import com.n4systems.exporting.io.CsvMapReader;
import com.n4systems.model.Tenant;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
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
	
	public String doImport() {
		try {
			SecurityFilter filter = new TenantOnlySecurityFilter(tenant.getId());
			
			OrgSaver orgSaver = new OrgSaver();
			CustomerOrgViewConverter customerConverter = new CustomerOrgViewConverter(filter);
			DivisionOrgViewConverter divisionConverter = new DivisionOrgViewConverter(filter);
			
			CsvMapReader mapReader = new CsvMapReader(importFile);

			ExportMapUnmarshaler<FullExternalOrgView> unmarshaler = new ExportMapUnmarshaler<FullExternalOrgView>(FullExternalOrgView.class, mapReader.getTitles());
			
			CustomerOrg customer;
			DivisionOrg division;
			FullExternalOrgView orgView;
			Map<String, String> row;
			while ((row = mapReader.readMap()) != null) {
				orgView = unmarshaler.toBean(row);
				
				if (orgView.isCustomer()) {
					customer = customerConverter.toModel(orgView);
					customer = (CustomerOrg)orgSaver.saveOrUpdate(customer);
					divisionConverter.setParentCustomer(customer);
				} else if (orgView.isDivision()) {
					division = divisionConverter.toModel(orgView);
					orgSaver.saveOrUpdate(division);
				} else {
					throw new ConversionException("Organization was neighter a Customer or Division");
				}
			}
			
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
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

			Exporter exporter = new CustomerCSVExporter(new OutputStreamWriter(byteOut), new TenantOnlySecurityFilter(tenant.getId()));
			exporter.export();
			
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
	
	public File getUpload() {
		return importFile;
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
