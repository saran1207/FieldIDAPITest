package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import rfid.ejb.session.LegacyProductType;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProductType;
import com.n4systems.reporting.PathHandler;

public class DownloadProductTypeAttachedFile extends DownloadAction {
	private static final long serialVersionUID = 1L;
	
	private ProductType productType;
	private LegacyProductType productTypeManager;
	
	public DownloadProductTypeAttachedFile(LegacyProductType productTypeManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.productTypeManager = productTypeManager;
	}

	public String doDownload() {
		
		// load the product type
		productType =  productTypeManager.findProductTypeAllFields( uniqueID, getTenantId() );
		
		if( productType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this producttype
		for(FileAttachment attach: productType.getAttachments()) {
			if(attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		
		// we did not find the attachment
		if(attachment == null) {
			addActionError(getText("error.noproducttypeattachedfile", fileName));
			return MISSING;
		}
		
		// construct a file path to our attachment
		File productTypeDirectory = PathHandler.getProductTypeAttachmentFile(productType);
		File attachedFile = new File(productTypeDirectory.getAbsolutePath(), attachment.getFileName());
		
		// make sure the file actually exists
		if( !attachedFile.exists() ) {
			addActionError( getText( "error.noproducttypeattachedfile", fileName ) );
			return MISSING;
		}
		
		// stream the file back to the browser
		fileSize = new Long( attachedFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( attachedFile );
			return sendFile( input );
		} catch( IOException e ) {
			failure = true;
		} finally {
			
		}
		
		return (failure) ? ERROR : null;
	}
	
	public String doDownloadImage() {
		productType =  productTypeManager.findProductType( uniqueID, getTenantId() );
		
		if( productType == null ) {
			addActionError( getText( "error.noproducttype" ) );
			return MISSING;
		} 
		
		if( productType.getImageName() == null ) {
			addActionError( getText( "error.noproductimage") );
			return MISSING;
		}
		
		
		File productTypeDirectory = PathHandler.getProductTypeImageFile( productType );
		File imageFile = new File( productTypeDirectory.getAbsolutePath() + '/' + productType.getImageName() );
		fileName = productType.getImageName();
		if( !imageFile.exists() ) {
			addActionError( getText( "error.noproductimage" ) );
			return MISSING;
		}
		
		fileSize = new Long( imageFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( imageFile );
			return sendFile( input );
		} catch( IOException e ) {
		} finally {
			failure = true;
		}
		
		return (failure) ? ERROR : null;
	}
	

	

}
