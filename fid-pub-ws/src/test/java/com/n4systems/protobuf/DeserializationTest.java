package com.n4systems.protobuf;

import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.protocolbuffers.google.GoogleProtobufMessageBodyReader;
import junit.framework.Assert;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeserializationTest {
    private static GoogleProtobufMessageBodyReader getReader() {
        return new GoogleProtobufMessageBodyReader();
    }

    private static void populateTestSimpleMessageBuilder(Messages.TestSimpleMessage.Builder builder) {
        builder.setDoubleField(1.0)
                .setFloatField(2)
                .setInt32Field(3)
                .setInt64Field(4l)
                .setUint32Field(5)
                .setUint64Field(6)
                .setSint32Field(7)
                .setSint64Field(8l)
                .setFixed32Field(9)
                .setFixed64Field(10l)
                .setSfixed32Field(11)
                .setSfixed64Field(12l)
                .setBoolField(true)
                .setStringField("string value");
    }

    private static void populateTestContainerMessageBuilder(Messages.TestContainerMessage.Builder builder) {
        builder.setInt32Field(1)
                .setNested(Messages.TestNestedMessage.newBuilder()
                        .setStringField("string value").build());
    }

    @Test
    public void test_simple_message_deserialization() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator generator = Json.createGenerator(outputStream);
        GoogleProtobufMessageBodyReader reader = getReader();

        generator.writeStartObject();
        generator.write("double_field", 1.0)
                .write("float_field", (float)2)
                .write("int32_field", 3)
                .write("int64_field", 4l)
                .write("uint32_field", 5)
                .write("uint64_field", 6l)
                .write("sint32_field", 7)
                .write("sint64_field", 8l)
                .write("fixed32_field", 9)
                .write("fixed64_field", 10l)
                .write("sfixed32_field", 11)
                .write("sfixed64_field", 12)
                .write("bool_field", true)
                .write("string_field", "string value")
                .writeEnd();

        generator.flush();
        generator.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        MediaType jsonMediaType = new MediaType("application", "json");
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("Content-Length", Integer.toString(outputStream.size()));


        Messages.TestSimpleMessage message = (Messages.TestSimpleMessage) reader.readFrom((Class) Messages.TestSimpleMessage.class, null, null, jsonMediaType, headers, inputStream);

        Assert.assertEquals(1.0, message.getDoubleField());
        Assert.assertEquals((float)2, message.getFloatField());
        Assert.assertEquals(3, message.getInt32Field());

    }
}
