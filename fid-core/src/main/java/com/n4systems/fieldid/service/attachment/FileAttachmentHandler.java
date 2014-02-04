package com.n4systems.fieldid.service.attachment;

import com.google.common.collect.Lists;
import com.n4systems.model.attachment.LocalFileAttachment;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class FileAttachmentHandler extends AbstractAttachmentHandler<LocalFileAttachment> {

    @Override
    public void upload(LocalFileAttachment attachment) throws AttachmentException {
        upload(new File(attachment.getPath()), attachment.getBytes());
    }

    @Override
    public void uploadTemp(LocalFileAttachment attachment) throws AttachmentException {
        upload(new File(getTempPath(attachment)), attachment.getBytes());
    }

    private void upload(File file, byte[] bytes) throws AttachmentException {
        try {
            if (file.exists()) {
                throw new IOException("attachment file " + file.getCanonicalPath() + " already exists");
            }
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new AttachmentException(e);
        }
    }

    @Override
    public void finalize(LocalFileAttachment attachment) throws AttachmentException {
        try {
            FileUtils.copyFile(new File(getTempPath(attachment)), new File(attachment.getPath()));
        } catch (IOException e) {
            throw new AttachmentException(e);
        }
    }

    @Override
    @Deprecated  // should use #load when dealing with files.
    public URL getUrl(LocalFileAttachment attachment) {
        try {
            return new File(attachment.getPath()).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new AttachmentException(e);
        }
    }

    @Override
    @Deprecated  // should use #load when dealing with files.
    public URL getUrl(LocalFileAttachment attachment, String flavourRequest) {
        throw new UnsupportedOperationException("local files do not support different flavours");
    }

    @Override
    public int remove(LocalFileAttachment attachment) {
        new File(attachment.getPath()).delete();
        return 1;
    }

    @Override
    public String getTempPath(LocalFileAttachment attachment) {
        return "temp/"+attachment.getPath();
    }

    public byte[] load(LocalFileAttachment attachment) {
        try {
            return FileUtils.readFileToByteArray(new File(attachment.getPath()));
        } catch (IOException e) {
            throw new AttachmentException(e);
        }
    }
}
