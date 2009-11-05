package com.n4systems.fieldid.actions.shared;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@UserPermissionFilter(userRequiresOneOf={Permissions.Tag, Permissions.ManageSystemConfig, Permissions.ManageSystemUsers, 
											Permissions.CreateInspection, Permissions.EditInspection, Permissions.ManageJobs})
public class UploadFileAction extends AbstractAction {
	private File upload;
	private String uploadFileName;
	private String uploadContentType;
	
	private String frameId;
	private Long frameCount;
	private String typeOfUpload;
	
	private String uploadedFilePath;
	
	public UploadFileAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@SkipValidation
	public String doForm() {
		return SUCCESS;
	}
	
	public String doUpload() {
		File tempFilePath = PathHandler.getTempFile( uploadFileName );
		try {
			FileUtils.copyFile(upload, tempFilePath);
			uploadedFilePath = new File( tempFilePath.getParent() ).getName() + '/' + tempFilePath.getName(); 
		} catch (Exception e) {
			addActionError( getText("error.fileuploadfailed" ) );
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String getUploadedFile() {
		return uploadedFilePath;
	}

	@Validations(
			requiredFields = { @RequiredFieldValidator( message="", key="error.fileisrequired" ) },
			customValidators = { @CustomValidator( type="fileExistsValidator", message ="", key = "error.fileuploadfailed" ),
								 @CustomValidator( type="fileSizeValidator", message="", key="error.file_size_limit") }
			)
	public File getUpload() {
		return upload;
	}
	
	public void setUpload( File upload ) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName( String uploadFileName ) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType( String uploadContentType ) {
		this.uploadContentType = uploadContentType;
	}

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId( String frameId ) {
		this.frameId = frameId;
	}

	public Long getFrameCount() {
		return frameCount;
	}

	public void setFrameCount( Long frameCount ) {
		this.frameCount = frameCount;
	}

	public String getUploadedFilePath() {
		return uploadedFilePath;
	}

	
	
	public Long fileSizeLimitInKB() {
		ConfigEntry fileSizeLimit = ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_DEFAULT_IN_KB;
		if (typeOfUpload != null) {
			if ("productAttachment".equals(typeOfUpload)) {
				fileSizeLimit = ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_PRODUCT_ATTACHMENT_IN_KB;
			} else if ("productTypeImage".equals(typeOfUpload)) {
				fileSizeLimit = ConfigEntry.UPLOAD_FILE_SIZE_LIMIT_PRODUCT_TYPE_IMAGE_IN_KB;
			} 
		}
		
		return ConfigContext.getCurrentContext().getLong(fileSizeLimit);
	}

	public String getTypeOfUpload() {
		return typeOfUpload;
	}

	public void setTypeOfUpload(String typeOfUpload) {
		this.typeOfUpload = typeOfUpload;
	}
}
