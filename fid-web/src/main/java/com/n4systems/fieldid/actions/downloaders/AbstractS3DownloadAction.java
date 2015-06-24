package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.io.ByteArrayInputStream;

/**
 * This is the base for any new S3-enabled DownloadAction classes.  These classes are using S3 and doing things the
 * "right way."  We handle these downloads with byte streams routed from a source in S3, allowing the files to bypass
 * any need to be stored on disk whether it be temporarily or permanently.
 *
 * Created by Jordan Heath on 2015-06-23.
 */
public abstract class AbstractS3DownloadAction extends AbstractDownloadAction {

    public AbstractS3DownloadAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    @SkipValidation
    public String doDownload() {
        //This new logic handles files specifically from S3 for various download actions, which works quite differently
        //from how we handle files that are physically on disk.
        //We did, however, need to snatch a good chunk of that old logic.
        if(!initializeDownload()) {
            return failActionResult;
        }

        //Instead of making an input stream from a file, we're going to use a byte array...
        byte[] fileContents = getFileBytes();

        if(fileContents == null) {
            return failActionResult;
        }

        //...and we're going to make a ByteArrayInputStream from that byte array!
        fileStream = new ByteArrayInputStream(fileContents);

        //We'll also want the size of that byte array, since we can't grabt he size of the actual file anymore.  Don't
        //worry, though, the .size() method from a file returns size in bytes.  Our array is conveniently made up of
        //bytes... so we'll just get the length of that array and be done with it.
        fileSize = String.valueOf(fileContents.length);

        return successActionResult;
    }

    protected abstract byte[] getFileBytes();
}
