package com.n4systems.fieldid.api.pub.protocolbuffers.google;


import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
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
import java.util.HashMap;

@Provider
public class GoogleProtobufMessageBodyReader implements MessageBodyReader<Message> {

    @Override
    public boolean isReadable(Class<?> targetClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return Message.class.isAssignableFrom(targetClass);
    }

    @Override
    public Message readFrom(Class<Message> messageClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> headers, InputStream inputStream) throws IOException, WebApplicationException {

        if(!mediaType.getType().equalsIgnoreCase("application")) {
            return null;
        }

        String subtype = mediaType.getSubtype();

        if(subtype.endsWith("64"))
            return parseFromBase64(messageClass, inputStream, Integer.parseInt(headers.getFirst("Content-Length")));

        if(subtype.equalsIgnoreCase("json"))
            return parseFromJson(messageClass, inputStream, Integer.parseInt(headers.getFirst("Content-Length")));

        return null;
    }

    private static Message parseFromJson(Class<Message> messageClass, InputStream inputStream, int streamLen) throws IOException {
        JsonReaderFactory factory = Json.createReaderFactory(new HashMap<String, Object>());
        JsonReader reader = factory.createReader(inputStream);
        JsonObject rawObject = reader.readObject();

        Method newBuilder = null;
        Message.Builder builder = null;


        try {
            newBuilder = messageClass.getMethod("newBuilder");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            builder = (GeneratedMessage.Builder) newBuilder.invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        populateMessageFromJsonObject(builder, rawObject);

        return builder.build();
    }

    private static void populateMessageFromJsonObject(Message.Builder builder, JsonObject source) {
        for(Descriptors.FieldDescriptor descriptor : builder.getDescriptorForType().getFields()) {
            switch(descriptor.getJavaType()) {
                case MESSAGE:
                    populateMessageFromJsonObject(builder.getFieldBuilder(descriptor), source.getJsonObject(GoogleProtobufUtils.getSerializedFieldName(descriptor)));
                    break;
                case STRING:
                    builder.setField(descriptor, source.getString(GoogleProtobufUtils.getSerializedFieldName(descriptor)));
                    break;
                case INT:
                    builder.setField(descriptor, source.getInt(GoogleProtobufUtils.getSerializedFieldName(descriptor)));
                    break;
                case LONG:
                    builder.setField(descriptor, source.getJsonNumber(GoogleProtobufUtils.getSerializedFieldName(descriptor)).longValue());
                    break;
                case BOOLEAN:
                    builder.setField(descriptor, source.getBoolean(GoogleProtobufUtils.getSerializedFieldName(descriptor)));
                    break;
                case FLOAT:
                    builder.setField(descriptor, (float)source.getJsonNumber(GoogleProtobufUtils.getSerializedFieldName(descriptor)).doubleValue());
                    break;
                case DOUBLE:
                    builder.setField(descriptor, source.getJsonNumber(GoogleProtobufUtils.getSerializedFieldName(descriptor)).doubleValue());
                    break;
            }
        }
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
