package com.n4systems.fieldid.api.pub.protocolbuffers.wire;

import com.n4systems.util.reflection.Reflector;
import com.squareup.wire.ExtendableMessage;
import com.squareup.wire.Extension;
import com.squareup.wire.Message;
import com.squareup.wire.Message.Datatype;
import com.squareup.wire.Message.Label;
import com.squareup.wire.ProtoField;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

@Provider
public class WireProtobufMessageBodyWriter implements MessageBodyWriter<com.squareup.wire.Message> {


    private static final HashSet<String> supportedApplicationSubTypes;

    static {
        supportedApplicationSubTypes = new HashSet<>();
        supportedApplicationSubTypes.add("x-protobuf");
        supportedApplicationSubTypes.add("x-protobuf64");
        supportedApplicationSubTypes.add("json");
    }

    @Override
    public long getSize(Message generatedMessage, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return generatedMessage.getSerializedSize() * 2;
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
    {
        return mediaType.getType().equalsIgnoreCase("application") && supportedApplicationSubTypes.contains(mediaType.getSubtype());
    }

    @Override
    public void writeTo(Message message, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream outputStream) throws IOException, WebApplicationException  {
        switch(mediaType.getSubtype())
        {
            case "x-protobuf":
                writeProtobuf(message, outputStream);
                break;
            case "x-protobuf64":
                writeProtobuf64(message, outputStream);
                break;
            case "json":
                writeJson(message, aClass, outputStream);
                break;
            default:
                throw new IOException("Unsupported subtype");
        }
    }

    private static void writeProtobuf(Message message, OutputStream outputStream) throws IOException {
        outputStream.write(message.toByteArray());
    }

    private static void writeProtobuf64 (Message message, OutputStream outputStream) throws IOException {
        outputStream.write(Base64.getEncoder().encode(message.toByteArray()));
    }

    private static void writeJson(Message message, Class<?> messageClass, OutputStream outputStream) throws IOException {
        JsonGeneratorFactory jsonFactory = Json.createGeneratorFactory(new HashMap<String, Object>());
        JsonGenerator generator = jsonFactory.createGenerator(outputStream);

        generator.writeStartObject();
        writeMessage(generator, message, messageClass);
        generator.writeEnd();
        generator.flush();
        generator.close();

    }

    private static void writeMessage(JsonGenerator generator, Message message, Class<?> messageClass) {
        try {
            List<NormalizedMessageField> fieldList = new ArrayList<>();

            for(Field field : Reflector.findAllFieldsWithAnnotation(messageClass, ProtoField.class)) {
                fieldList.add(new NormalizedMessageField(field, message));
            }

            if(ExtendableMessage.class.isAssignableFrom(messageClass)) {
                ExtendableMessage msg = (ExtendableMessage)message;

                for(Extension ext : (List<Extension>)msg.getExtensions()) {
                    fieldList.add(new NormalizedMessageField(ext, msg));
                }
            }

            for(NormalizedMessageField field : fieldList) {
                if(field.value == null) {
                    continue;
                }
                if(field.label == Label.REPEATED) {
                    generator.writeStartArray(field.name);
                    switch(field.type) {
                        case MESSAGE:
                            for(Message m : (List<Message>)field.value) {
                                generator.writeStartObject();
                                writeMessage(generator, m, field.clazz);
                                generator.writeEnd();
                            }
                            break;
                        default:
                            break;
                    }
                    generator.writeEnd();
                } else {
                    switch(field.type) {
                        case STRING:
                            if(field.name == "notes")
                                System.out.println((String)field.value);
                            generator.write(field.name, (String)field.value);
                            break;
                        case INT32:
                            generator.write(field.name, (Integer)field.value);
                            break;
                        case INT64:
                            generator.write(field.name, (Long)field.value);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static class NormalizedMessageField {
        Label label;
        Datatype type;
        String name;
        Object value;
        Class<?> clazz;

        public NormalizedMessageField(Extension ext, ExtendableMessage instance) {
            label = ext.getLabel();
            type = ext.getDatatype();
            name = ext.getName();
            value = instance.getExtension(ext);

            if(type == Datatype.MESSAGE) {
                clazz = ext.getMessageType();
            }
        }

        public NormalizedMessageField(Field field, Object instance) throws IllegalAccessException {
            ProtoField annotation = field.getAnnotation(ProtoField.class);

            label = annotation.label();
            type = annotation.type();
            name = field.getName();
            value = field.get(instance);
        }
    }
}
