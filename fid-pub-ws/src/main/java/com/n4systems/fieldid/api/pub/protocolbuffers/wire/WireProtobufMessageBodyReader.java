package com.n4systems.fieldid.api.pub.protocolbuffers.wire;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.squareup.wire.Message;
import com.squareup.wire.Wire;

public class WireProtobufMessageBodyReader implements MessageBodyReader<com.squareup.wire.Message> {

    @Override
    public boolean isReadable(Class<?> targetClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return com.squareup.wire.Message.class.isAssignableFrom(targetClass);
    }

    @Override
    public Message readFrom(Class<Message> messageClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream) throws IOException, WebApplicationException {
        if(mediaType.getSubtype().endsWith("64"))
            return parseFromBase64(messageClass, inputStream, Integer.parseInt(headers.getFirst("Content-Length")));

        return null;
    }

    private static Message parseFromBase64(Class<Message> messageClass, InputStream inputStream, int streamLen) throws IOException {
        Wire wire = new Wire();
        byte streamData[] = new byte[streamLen];
        final int read = inputStream.read(streamData, 0, streamLen);

        if(read < streamLen) {
            return null;
        }

        return wire.parseFrom(streamData, messageClass);
    }

}
