package com.n4systems.fieldid.api.pub;

import com.google.protobuf.GeneratedMessage;
import com.n4systems.fieldid.api.pub.serialization.Messages;

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
import java.util.Base64;

@Provider
public class ProtobufMessageBodyReader implements MessageBodyReader<com.google.protobuf.GeneratedMessage> {
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Messages.TestEntityCreateMessage.class.isAssignableFrom(aClass);
    }

    @Override
    public GeneratedMessage readFrom(Class<GeneratedMessage> generatedMessageClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream) throws IOException, WebApplicationException {

        if(mediaType.getSubtype().endsWith("64"))
            return parseFromBase64(generatedMessageClass, inputStream, Integer.parseInt(headers.getFirst("Content-Length")));

        return null;
    }

    private static GeneratedMessage parseFromBase64(Class<GeneratedMessage> targetClass, InputStream inputStream, int streamLen) throws IOException
    {
        Method parseFrom;
        try {
            parseFrom = targetClass.getMethod("parseFrom", byte[].class);
        } catch (NoSuchMethodException e) {return null;}

        byte streamData[] = new byte[streamLen];
        final int read = inputStream.read(streamData, 0, streamLen);

        if(read < streamLen) {
            return null;
        }
        try {
            return (GeneratedMessage) parseFrom.invoke(null, new Object[]{Base64.getDecoder().decode(streamData)});
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }


    /*@Override
    public Messages.TestEntityCreateMessage readFrom(Class<Messages.TestEntityCreateMessage> testEntityCreateMessageClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream) throws IOException, WebApplicationException {
        if(mediaType.toString().endsWith("64"))
        {
            int len = Integer.parseInt(headers.getFirst("Content-Length"));
            byte streamData[] = new byte[len];
            inputStream.read(streamData, 0, len);
            return Messages.TestEntityCreateMessage.parseFrom(Base64.getDecoder().decode(streamData));
        }
        return Messages.TestEntityCreateMessage.parseFrom(inputStream);
    }*/
}
