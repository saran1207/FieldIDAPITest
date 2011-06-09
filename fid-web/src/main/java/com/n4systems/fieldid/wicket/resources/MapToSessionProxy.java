package com.n4systems.fieldid.wicket.resources;

import com.n4systems.fieldid.wicket.FieldIDSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class MapToSessionProxy implements InvocationHandler {

    @SuppressWarnings("unchecked")
    public static Map<String,String> createProxy() {
        return (Map<String, String>) Proxy.newProxyInstance(MapToSessionProxy.class.getClassLoader(), new Class[]{Map.class}, new MapToSessionProxy());
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object result;
        Map<String,String> realTarget = FieldIDSession.get().getCache();
        try {
            result = method.invoke(realTarget, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
        return result;
    }

}
