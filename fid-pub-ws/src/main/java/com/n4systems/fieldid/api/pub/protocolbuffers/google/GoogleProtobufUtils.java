package com.n4systems.fieldid.api.pub.protocolbuffers.google;

import com.google.protobuf.Descriptors;

import java.util.Map;

public final class GoogleProtobufUtils {

    private GoogleProtobufUtils() {}

    public static String getSerializedFieldName(Descriptors.FieldDescriptor descriptor) {
        for(Map.Entry<Descriptors.FieldDescriptor, Object> opt : descriptor.getOptions().getAllFields().entrySet()) {
            if(opt.getKey().getName().equals("serialized_name"))
                return (String)opt.getValue();
        }

        return descriptor.getName();
    }
}
