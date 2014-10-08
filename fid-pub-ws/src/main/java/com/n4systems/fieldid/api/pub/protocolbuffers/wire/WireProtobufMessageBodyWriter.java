package com.n4systems.fieldid.api.pub.protocolbuffers.wire;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Base64;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import com.squareup.wire.Message;

public class WireProtobufMessageBodyWriter implements MessageBodyWriter<com.squareup.wire.Message> {

    @Override
    public long getSize(Message generatedMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return generatedMessage.getSerializedSize() * 2;
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return mediaType.getType().equalsIgnoreCase("application") && mediaType.getSubtype().equalsIgnoreCase("x-protobuf64");
    }

    @Override
    public void writeTo(Message generatedMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream outputStream) throws IOException, WebApplicationException
    {
        outputStream.write(Base64.getEncoder().encode(generatedMessage.toByteArray()));
    }
}
