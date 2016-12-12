package com.n4systems.fieldid.service.attachment;

import com.n4systems.model.attachment.Attachment;

// CAVEAT : all implementations of this should be prototype scoped spring beans.
//  i.e. add them to the applicationContext and they will automatically be picked up.
public abstract class Flavour<T extends FlavourOptions> implements Attachment {

    protected Attachment delegate;
    byte[] bytes;
    protected T options;
    private String name = getClass().getSimpleName();

    public Flavour() {
    }

    public <E extends Flavour> E forAttachment(Attachment delegate) {
        this.delegate = delegate;
        return (E) this;
    }

    public Flavour(Attachment delegate) {
        this.delegate = delegate;
    }

    public <E extends Flavour> E withOptions(T options) {
        this.options = options;
        return (E) this;
    }

    @Override
    public byte[] getBytes() {
        if (bytes==null) {
            bytes = createBytes(delegate.getBytes(), options);
        }
        return bytes;
    }

    @Override
    public String getFileName() {
        return delegate.getFileName();
    }

    protected abstract byte[] createBytes(byte[] bytes, T options);

    @Override
    public String getContentType() {
        return delegate.getContentType();
    }

    @Override
    public String getComments() {
        return String.format("%s (%s)", delegate.getComments(), getName());
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return delegate.getState();
    }

    @Override
    public Type getType() {
        return delegate.getType();
    }

    @Override
    public void setState(State state) {
        throw new IllegalStateException("you should not be setting state on " + getClass().getSimpleName() + " flavour of " + delegate.getFileName());
    }

    @Override
    public String toString() {
        return "Flavour{" +
                "delegate=" + delegate.getFileName() + " ---> " + delegate.getPath() +
                "}";
    }

    public T isSupportedRequest(String[] flavourRequest) {
        return null;
    }
}
