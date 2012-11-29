package com.n4systems.fieldid.actions.autoattribute;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.AutoAttributeExporter;
import com.n4systems.exporting.Importer;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.importexport.AbstractImportAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.notifiers.notifications.AutoAttributeImportFailureNotification;
import com.n4systems.notifiers.notifications.AutoAttributeImportSuccessNotification;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.security.Permissions;
import com.n4systems.util.ArrayUtils;
import org.apache.log4j.Logger;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class AutoAttributeExportAction extends AbstractImportAction {
	private Logger logger = Logger.getLogger(AutoAttributeExportAction.class);
	
	private AutoAttributeCriteria autoAttributeCriteria;
    
	private InputStream exampleExportFileStream;
	private String exampleExportFileSize;

	private DownloadLink downloadLink;
	private String reportName;
	
	private List<AssetType> assetTypes;
	
	public AutoAttributeExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected Importer createImporter(MapReader reader) {
		return getImporterFactory().createAutoAttributeImporter(reader, autoAttributeCriteria);
	}

	@Override
	protected ImportSuccessNotification createSuccessNotification() {
		return new AutoAttributeImportSuccessNotification(getCurrentUser());
	}

	@Override
	protected ImportFailureNotification createFailureNotification() {
		return new AutoAttributeImportFailureNotification(getCurrentUser());
	}
	
	public String doExport() {
		try {
			ListLoader<AutoAttributeDefinition> attribLoader = getLoaderFactory().createPassthruListLoader(autoAttributeCriteria.getDefinitions());
			
			reportName = getExportFileName();
			downloadLink = getDownloadCoordinator().generateAutoAttributeExport(reportName, getDownloadLinkUrl(), attribLoader);
		} catch (RuntimeException e) {
			logger.error("Unable to execute auto attribute export", e);
			addFlashMessage(getText("error.export_failed.autoattrib"));
			return ERROR;
		}
		return SUCCESS;
	}

	private String getExportFileName() {
		String exportName = autoAttributeCriteria.getAssetType().getName();
		return getText("label.export_file.autoattrib", ArrayUtils.newArray(exportName));
	}
	
	public AutoAttributeDefinition createExampleDefinition() {
		AutoAttributeDefinition def = new AutoAttributeDefinition();
		def.setCriteria(autoAttributeCriteria);
		
		InfoOptionBean option;
		for (InfoFieldBean field: autoAttributeCriteria.getInputs()) {
			option = new InfoOptionBean();
			option.setInfoField(field);
			option.setName("");
			option.setStaticData(false);
			def.getInputs().add(option);
		}
		
		for (InfoFieldBean input: autoAttributeCriteria.getOutputs()) {
			option = new InfoOptionBean();
			option.setInfoField(input);
			option.setName("");
			option.setStaticData(false);
			def.getOutputs().add(option);
		}
		
		return def;
	}
	
	public String doDownloadExample() {
		AutoAttributeExporter exporter = new AutoAttributeExporter(getLoaderFactory().createPassthruListLoader(Arrays.asList(createExampleDefinition())));
		
		MapWriter writer = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			writer = new ExcelMapWriter(byteOut, getPrimaryOrg().getDateFormat(), getCurrentUser().getTimeZone());
			exporter.export(writer);
			
		} catch (Exception e) {
			logger.error("Failed generating example auto-attribute export", e);
			return ERROR;
		} finally {
			StreamUtils.close(writer);
		}
		
		byte[] bytes = byteOut.toByteArray();
		exampleExportFileSize = String.valueOf(bytes.length);
		exampleExportFileStream = new ByteArrayInputStream(bytes);
		
		return SUCCESS;
	}

	public AutoAttributeCriteria getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(AutoAttributeCriteria autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}
	
	public Long getCriteriaId() {
		return autoAttributeCriteria.getId();
	}

	public void setCriteriaId(Long criteriaId) {
		if (autoAttributeCriteria == null || !autoAttributeCriteria.getId().equals(criteriaId)) {
			autoAttributeCriteria = getLoaderFactory().createFilteredIdLoader(AutoAttributeCriteria.class).setId(criteriaId).setPostFetchFields("inputs", "outputs", "assetType.name", "definitions.outputs").load();
		}
	}

	/*
	 * Example export file download params
	 */
	
	public String getFileName() {
		return ContentType.EXCEL.prepareFileName(getExportFileName());
	}
	
	public String getFileSize() {
		return exampleExportFileSize;
	}

	public String getContentType() {
		return ContentType.EXCEL.getMimeType();
	}
	
	public InputStream getFileStream() {
		return exampleExportFileStream;
	}

	public DownloadLink getDownloadLink() {
		return downloadLink;
	}

	public String getReportName() {
		return reportName;
	}
	
	public List<AssetType> getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = new ArrayList<AssetType>();
			List<AssetType> allAssetTypes = getLoaderFactory().createAssetTypeListLoader().setPostFetchFields("autoAttributeCriteria").load();
			for(AssetType assetType: allAssetTypes) {
				if(assetType.getAutoAttributeCriteria()!=null) {
					assetTypes.add(assetType);
				}
			}
		}
		return assetTypes;
	}
}
