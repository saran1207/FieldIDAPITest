package com.n4systems.fieldid.service.attachment;

import com.google.common.base.Preconditions;
import com.n4systems.model.attachment.Attachment;

public abstract class Flavour implements Attachment {

    Attachment delegate;
    byte[] bytes;
    State state;

    public Flavour() {

    }

    public abstract String getSuffix();

    public Flavour(Attachment delegate) {
        this.delegate = delegate;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public <T extends Flavour> T generateBytes() {
        bytes = createBytes(delegate.getBytes());
        return (T) this;
    }

    protected abstract byte[] createBytes(byte[] bytes);

    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    @Override
    public String getComments() {
        return String.format("%s (%s)",delegate.getComments(), getSuffix());
    }

    @Override
    public String getPath() {
        return insertSuffix(delegate.getPath(),getSuffix());
    }

    private String insertSuffix(String path, String suffix) {
        Preconditions.checkNotNull(path,"you must have a path for attachment flavour " + toString());
        int index = path.lastIndexOf(".");
        if (index==-1) {
            return path + suffix;
        }
        return new StringBuffer(path.substring(0,index))
                .append('.')
                .append(suffix)
                .append(path.substring(index))
                .toString();
    }

    @Override
    public String getTempPath() {
        return insertSuffix(delegate.getTempPath(),getSuffix());
    }

    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Type getType() {
        return delegate.getType();
    }

    @Override
    public String toString() {
        return "Flavour{" +
                "delegate=" + delegate.getPath() +
                '}';
    }
}
