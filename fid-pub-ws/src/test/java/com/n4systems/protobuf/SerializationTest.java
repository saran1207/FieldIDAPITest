package com.n4systems.protobuf;

import com.n4systems.fieldid.api.pub.protocolbuffers.google.GoogleProtobufMessageBodyWriter;
import com.n4systems.fieldid.api.pub.serialization.TestMessages;
import org.junit.Test;
import org.junit.Assert;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializationTest {
    private static GoogleProtobufMessageBodyWriter getWriter() {
        return new GoogleProtobufMessageBodyWriter();
    }

    private static void populateTestSimpleMessageBuilder(TestMessages.TestSimpleMessage.Builder builder) {
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

    private static void populateTestContainerMessageBuilder(TestMessages.TestContainerMessage.Builder builder) {
        builder.setInt32Field(1)
                .setNested(TestMessages.TestNestedMessage.newBuilder()
                .setStringField("string value").build());
    }

    @Test
    public void test_simple_message_serialization_eligibility() {
        GoogleProtobufMessageBodyWriter writer = getWriter();

        MediaType jsonMediaType = new MediaType("application", "json");
        MediaType htmlMediaType = new MediaType("text", "html");

        Assert.assertTrue(writer.isWriteable(TestMessages.TestSimpleMessage.class, null, null, jsonMediaType));
        Assert.assertFalse(writer.isWriteable(TestMessages.TestSimpleMessage.class, null, null, htmlMediaType));
        Assert.assertFalse(writer.isWriteable(Object.class, null, null, jsonMediaType));
    }

    @Test
    public void test_simple_message_json_serialization() throws IOException {
        GoogleProtobufMessageBodyWriter writer = getWriter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TestMessages.TestSimpleMessage.Builder builder = TestMessages.TestSimpleMessage.newBuilder();
        populateTestSimpleMessageBuilder(builder);
        TestMessages.TestSimpleMessage message = builder.build();
        MediaType jsonMediaType = new MediaType("application", "json");

        writer.writeTo(message, message.getClass(), null, null, jsonMediaType, null, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        JsonReader reader = Json.createReader(inputStream);
        JsonObject serializedObj = reader.readObject();

        Assert.assertEquals(serializedObj.getJsonNumber("double_field").doubleValue(), message.getDoubleField(), 0);
        Assert.assertEquals((float)serializedObj.getJsonNumber("float_field").doubleValue(), message.getFloatField(), 0);
        Assert.assertEquals(serializedObj.getInt("int32_field"), message.getInt32Field());
        Assert.assertEquals(serializedObj.getJsonNumber("int64_field").longValue(), message.getInt64Field());
        Assert.assertEquals(serializedObj.getInt("uint32_field"), message.getUint32Field());
        Assert.assertEquals(serializedObj.getJsonNumber("uint64_field").longValue(), message.getUint64Field());
        Assert.assertEquals(serializedObj.getInt("sint32_field"), message.getSint32Field());
        Assert.assertEquals(serializedObj.getJsonNumber("sint64_field").longValue(), message.getSint64Field());
        Assert.assertEquals(serializedObj.getInt("fixed32_field"), message.getFixed32Field());
        Assert.assertEquals(serializedObj.getJsonNumber("fixed64_field").longValue(), message.getFixed64Field());
        Assert.assertEquals(serializedObj.getInt("sfixed32_field"), message.getSfixed32Field());
        Assert.assertEquals(serializedObj.getJsonNumber("sfixed64_field").longValue(), message.getSfixed64Field());
        Assert.assertEquals(serializedObj.getBoolean("bool_field"), message.getBoolField());
        Assert.assertEquals(serializedObj.getString("string_field"), message.getStringField());

    }

    @Test
    public void test_nested_message_json_serialization() throws IOException {
        GoogleProtobufMessageBodyWriter writer = getWriter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TestMessages.TestContainerMessage.Builder builder = TestMessages.TestContainerMessage.newBuilder();
        populateTestContainerMessageBuilder(builder);
        TestMessages.TestContainerMessage message = builder.build();

        MediaType jsonMediaType = new MediaType("application", "json");

        writer.writeTo(message, message.getClass(), null, null, jsonMediaType, null, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        JsonReader reader = Json.createReader(inputStream);
        JsonObject serializedObj = reader.readObject();
        JsonObject nestedObj = serializedObj.getJsonObject("nested");

        Assert.assertNotNull(nestedObj);
        Assert.assertEquals(serializedObj.getInt("int32_field"), message.getInt32Field());
        Assert.assertEquals(nestedObj.getString("string_field"), message.getNested().getStringField());
    }
}
