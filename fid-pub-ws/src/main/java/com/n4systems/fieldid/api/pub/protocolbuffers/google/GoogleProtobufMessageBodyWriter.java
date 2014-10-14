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
        writeMessage(generator, message);
        generator.writeEnd();
        generator.flush();
        generator.close();

    }

    private static void writeMessage(JsonGenerator generator, Message message) {
        try {
            List<NormalizedMessageField> fieldList = new ArrayList<>();

            Map<Descriptors.FieldDescriptor, Object> fields = message.getAllFields();

            for(Map.Entry<Descriptors.FieldDescriptor, Object> field : fields.entrySet()) {
                fieldList.add(new NormalizedMessageField(field.getKey(), field.getValue()));
            }

            if(GeneratedMessage.ExtendableMessage.class.isAssignableFrom(message.getClass())) {
                GeneratedMessage.ExtendableMessage msg = (GeneratedMessage.ExtendableMessage) message;
                for(Descriptors.FieldDescriptor field : message.getDescriptorForType().getExtensions()) {
                    fieldList.add(new NormalizedMessageField(field, msg.getField(field)));
                }
            }



            /*for(Field field : Reflector.findAllFieldsWithAnnotation(messageClass, ProtoField.class)) {
                fieldList.add(new NormalizedMessageField(field, message));
            }

            if(ExtendableMessage.class.isAssignableFrom(messageClass)) {
                ExtendableMessage msg = (ExtendableMessage)message;

                for(Extension ext : (List<Extension>)msg.getExtensions()) {
                    fieldList.add(new NormalizedMessageField(ext, msg));
                }
            }*/

            for(NormalizedMessageField field : fieldList) {
                if(field.value == null) {
                    continue;
                }
                if(field.repeated) {
                    generator.writeStartArray(field.name);
                    switch(field.type) {
                        case MESSAGE:
                            for(Message m : (List<Message>)field.value) {
                                generator.writeStartObject();
                                writeMessage(generator, m);
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
                            generator.write(field.name, (String)field.value);
                            break;
                        case INT:
                            generator.write(field.name, (Integer)field.value);
                            break;
                        case LONG:
                            generator.write(field.name, (Long)field.value);
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static class NormalizedMessageField {
        boolean repeated;
        Descriptors.FieldDescriptor.JavaType type;
        String name;
        Object value;
        Descriptors.Descriptor messageDescriptor;

        /*public NormalizedMessageField(Extension ext, ExtendableMessage instance) {
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
        }*/

        public NormalizedMessageField(Descriptors.FieldDescriptor fieldDescriptor, Object value) {
            this.value = value;

            name = fieldDescriptor.getName();
            type = fieldDescriptor.getJavaType();
            repeated = List.class.isAssignableFrom(value.getClass());
            if(type == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                messageDescriptor = fieldDescriptor.getMessageType();
            }

            for(Map.Entry<Descriptors.FieldDescriptor, Object> opt : fieldDescriptor.getOptions().getAllFields().entrySet()) {
                if(opt.getKey().getName().equals("serialized_name"))
                    name = (String)opt.getValue();
            }

        }
    }
}
