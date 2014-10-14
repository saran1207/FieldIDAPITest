package com.n4systems.fieldid.api.pub.protocolbuffers.google;


import com.google.protobuf.Message;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Provider
public class GoogleProtobufMessageBodyReader implements MessageBodyReader<Message> {

    @Override
    public boolean isReadable(Class<?> targetClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(targetClass);
    }

    @Override
    public Message readFrom(Class<Message> messageClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream) throws IOException, WebApplicationException {
        if(mediaType.getSubtype().endsWith("64"))
            return parseFromBase64(messageClass, inputStream, Integer.parseInt(headers.getFirst("Content-Length")));

        return null;
    }

    private static Message parseFromBase64(Class<Message> messageClass, InputStream inputStream, int streamLen) throws IOException {
        byte streamData[] = new byte[streamLen];
        final int read = inputStream.read(streamData, 0, streamLen);

        if(read < streamLen) {
            return null;
        }

        Method parseFrom = null;
        try {
            parseFrom = messageClass.getMethod("parseFrom", byte[].class);
            return (Message)parseFrom.invoke(null, streamData);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // can't actually happen
            e.printStackTrace();
        }
        return null;
    }

}
