package com.n4systems.fieldid.api.pub;

import com.google.protobuf.GeneratedMessage;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class ProtobufParamConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> tClass, Type type, Annotation[] annotations) {
        if(!GeneratedMessage.class.isAssignableFrom(tClass)) {
            return null;
        }

        return new ProtobufMessageParamConverter<>(tClass);
    }
}
