package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.util.ContentTypeUtil;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This is the base for any Legacy DownloadAction classes.  These classes haven't yet fully been moved off of disk
 * due to potential further implications of rolling over to using S3.  This allows an easier transition from Legacy
 * to S3, creating a path that allows us to move one action at a time and carefully consider the implications.
 *
 * Created by Jordan Heath on 2015-06-23.
 */
public abstract class AbstractLegacyDownloadAction extends AbstractDownloadAction {

    public AbstractLegacyDownloadAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    /**
     * Called when a FileNotFoundException is thrown while opening the InputStream
     *
     * @param e FileNotFoundException
     * @return String to be used for the action result
     */
    protected abstract String onFileNotFoundException(FileNotFoundException e);

    /** @return File path to the download file */
    public abstract File getFile();

    @SkipValidation
    public String doDownload() {
		/*
		 * this should be called asap to ensure the implemented methods
		 * have values
		 */
        if (!initializeDownload()) {
            return failActionResult;
        }

        File downloadFile = getFile();
        fileSize = String.valueOf(downloadFile.length());

        try {
            fileStream = new FileInputStream(downloadFile);
        } catch(FileNotFoundException e) {
            return onFileNotFoundException(e);
        }

        return successActionResult;
    }

    public String getContentType() {
        return ContentTypeUtil.getContentType(getFileName());
    }
}
