package com.n4systems.fieldid.api.pub.protocolbuffers.google;


import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import org.apache.commons.codec.binary.Base64InputStream;

import javax.json.*;
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

    private static Message parseFromJson(Class<Message> messageClass, InputStream inputStream, @SuppressWarnings("unused") int streamLen) throws IOException {
        JsonReaderFactory factory = Json.createReaderFactory(new HashMap<>());
        JsonReader reader = factory.createReader(inputStream);
        JsonObject rawObject = reader.readObject();

        Method newBuilder;
        Message.Builder builder;


        try {
            newBuilder = messageClass.getMethod("newBuilder");
            builder = (GeneratedMessage.Builder) newBuilder.invoke(null);
            populateMessageFromJsonObject(builder, rawObject);

            return builder.build();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            /* should never happen, just shutting up warnings */
        }
        /* we should never get here */
        return null;
    }

    private static void populateMessageFromJsonObject(Message.Builder builder, JsonObject source) {
        for(Descriptors.FieldDescriptor descriptor : builder.getDescriptorForType().getFields()) {
			String fieldName = GoogleProtobufUtils.getSerializedFieldName(descriptor);

			if (source.get(fieldName) != null) {
				switch (descriptor.getJavaType()) {
					case MESSAGE:
						if (descriptor.isRepeated()) {
							JsonArray repeatedField = source.getJsonArray(fieldName);
							for (int i = 0; i < repeatedField.size(); i++) {
								Message.Builder repeatedFieldBuilder = builder.newBuilderForField(descriptor);
								populateMessageFromJsonObject(repeatedFieldBuilder, repeatedField.getJsonObject(i));
								builder.addRepeatedField(descriptor, repeatedFieldBuilder.build());
							}
						} else {
							populateMessageFromJsonObject(builder.getFieldBuilder(descriptor), source.getJsonObject(fieldName));
						}
						break;
					case ENUM:
						builder.setField(descriptor, descriptor.getEnumType().findValueByName(source.getString(fieldName)));
						break;
					case STRING:
						builder.setField(descriptor, source.getString(fieldName));
						break;
					case INT:
						builder.setField(descriptor, source.getInt(fieldName));
						break;
					case LONG:
						builder.setField(descriptor, source.getJsonNumber(fieldName).longValue());
						break;
					case BOOLEAN:
						builder.setField(descriptor, source.getBoolean(fieldName));
						break;
					case FLOAT:
						builder.setField(descriptor, (float) source.getJsonNumber(fieldName).doubleValue());
						break;
					case DOUBLE:
						builder.setField(descriptor, source.getJsonNumber(fieldName).doubleValue());
						break;
				}
			}
        }
    }

    private static Message parseFromBase64(Class<Message> messageClass, InputStream inputStream, @SuppressWarnings("unused") int streamLen) throws IOException {

        InputStream decodedStream = new Base64InputStream(inputStream);

        Method parseFrom;
        try {
            parseFrom = messageClass.getMethod("parseFrom", InputStream.class);
            return (Message)parseFrom.invoke(null, decodedStream);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            // can't actually happen
            e.printStackTrace();
        }
        return null;
    }

}
