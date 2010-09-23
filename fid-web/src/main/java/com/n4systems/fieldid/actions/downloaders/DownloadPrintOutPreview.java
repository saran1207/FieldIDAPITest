package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.PrintOut;
import com.n4systems.reporting.PathHandler;

public class DownloadPrintOutPreview extends DownloadAction {

	private static final long serialVersionUID = 1L;

	private PrintOut printOut;

	private File previewFile;

	public DownloadPrintOutPreview(PersistenceManager persistenceManager) {
		super(persistenceManager);
		forceDownload = false;
	}

	@Override
	public String doDownload() {
		getPrintOut();

		if (printOutAvailable()) {
			previewFile = PathHandler.getPreviewImage(printOut);
			fileName = printOut.getFullPdfImage();
			
			return sendFile();
		}
		return MISSING;
	}
	
	public String doDownloadThumb() {
		getPrintOut();

		if (printOutAvailable()) {
			previewFile = PathHandler.getPreviewThumb(printOut);
			fileName = printOut.getThumbNailImage();
			return sendFile();
		}
		
		return MISSING;
	}

	private void getPrintOut() {
		printOut = persistenceManager.find(PrintOut.class, uniqueID);
	}
	
	private boolean printOutAvailable() {
		return (printOut != null) && (!printOut.isCustom() || getTenant().equals(printOut.getTenant()));
	}
	
	private String sendFile() {
		// make sure the file actually exists
		if (!previewFile.exists()) {
			addActionErrorText("error.nopreviewimagefound");
			return MISSING;
		}

		// stream the file back to the browser
		fileSize = new Long(previewFile.length()).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream(previewFile);
			return super.sendFile(input);
		} catch (IOException e) {
		} finally {
			failure = true;
		}

		return (failure) ? ERROR : null;
	}
	
	

}
