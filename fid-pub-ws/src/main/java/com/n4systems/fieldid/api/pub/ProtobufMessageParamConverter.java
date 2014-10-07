package com.n4systems.fieldid.api.pub;

import javax.ws.rs.ext.ParamConverter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

public class ProtobufMessageParamConverter<M> implements ParamConverter<M> {

    private Method parseFrom;

    public ProtobufMessageParamConverter(Class<M> type)
    {
        try {
            parseFrom = type.getMethod("parseFrom", byte[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public M fromString(String s) {

        try {
            return (M)parseFrom.invoke(null, new Object[]{Base64.getDecoder().decode(s.getBytes())});
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toString(M m) {
        return null;
    }
}
