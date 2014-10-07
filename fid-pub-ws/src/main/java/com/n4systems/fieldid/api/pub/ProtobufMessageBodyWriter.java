package com.n4systems.fieldid.api.pub;

import com.google.protobuf.GeneratedMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Base64;

@Provider
public class ProtobufMessageBodyWriter implements MessageBodyWriter<com.google.protobuf.GeneratedMessage> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return mediaType.getType().equalsIgnoreCase("application") && mediaType.getSubtype().equalsIgnoreCase("x-protobuf64");
    }

    @Override
    public long getSize(GeneratedMessage generatedMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return generatedMessage.getSerializedSize() * 2;
    }

    @Override
    public void writeTo(GeneratedMessage generatedMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> stringObjectMultivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        outputStream.write(Base64.getEncoder().encode(generatedMessage.toByteArray()));
    }
}
