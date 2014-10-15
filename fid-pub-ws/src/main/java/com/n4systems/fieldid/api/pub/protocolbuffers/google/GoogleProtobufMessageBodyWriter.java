package com.n4systems.fieldid.api.pub.protocolbuffers.google;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;
import com.n4systems.util.reflection.Reflector;

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
import java.util.stream.Collectors;

@Provider
public class GoogleProtobufMessageBodyWriter implements MessageBodyWriter<Message> {


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

        return Message.class.isAssignableFrom(aClass) && mediaType.getType().equalsIgnoreCase("application") && supportedApplicationSubTypes.contains(mediaType.getSubtype());
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
        writeMessage(generator, message);
        generator.writeEnd();
        generator.flush();
        generator.close();

    }

    private static void writeMessage(JsonGenerator generator, Message message) {

        for(Map.Entry<Descriptors.FieldDescriptor, Object> entry : message.getAllFields().entrySet()) {
            Descriptors.FieldDescriptor descriptor = entry.getKey();
            Object value = entry.getValue();

            if(value == null) {
                continue;
            }

            if(List.class.isAssignableFrom(value.getClass())) {
                generator.writeStartArray(GoogleProtobufUtils.getSerializedFieldName(descriptor));

                switch(descriptor.getJavaType()) {
                    case MESSAGE:
                        for(Message m : (List<Message>)value) {
                            generator.writeStartObject();
                            writeMessage(generator, m);
                            generator.writeEnd();
                        }
                        break;
                    case STRING:
                        for(String s : (List<String>)value) {
                            generator.write(s);
                        }
                        break;
                    case INT:
                        for(Integer i : (List<Integer>)value) {
                            generator.write(i);
                        }
                        break;
                    case LONG:
                        for(Long l : (List<Long>)value) {
                            generator.write(l);
                        }
                        break;
                    case BOOLEAN:
                        for(Boolean b : (List<Boolean>)value) {
                            generator.write(b);
                        }
                        break;
                    case FLOAT:
                        for(Float f : (List<Float>)value) {
                            generator.write(f);
                        }
                        break;
                    case DOUBLE:
                        for(Double d : (List<Double>)value) {
                            generator.write(d);
                        }
                        break;
                }

                generator.writeEnd();
            }
            else {
                switch(descriptor.getJavaType()) {
                    case MESSAGE:
                        generator.writeStartObject(GoogleProtobufUtils.getSerializedFieldName(descriptor));
                        writeMessage(generator, (Message) value);
                        generator.writeEnd();
                        break;
                    case STRING:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (String)value);
                        break;
                    case INT:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (Integer)value);
                        break;
                    case LONG:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (Long)value);
                        break;
                    case BOOLEAN:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (Boolean)value);
                        break;
                    case FLOAT:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (Float)value);
                        break;
                    case DOUBLE:
                        generator.write(GoogleProtobufUtils.getSerializedFieldName(descriptor), (Double)value);
                        break;
                }
            }
        }
    }
}
