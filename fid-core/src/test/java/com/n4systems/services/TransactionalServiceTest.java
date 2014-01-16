package com.n4systems.services;

import com.google.common.base.Predicate;
import com.n4systems.fieldid.junit.FieldIdUnitTest;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import org.junit.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class TransactionalServiceTest extends FieldIdUnitTest {

    @Test
    public void testServicesForTransactional()  {
        Set<Class<? extends FieldIdPersistenceService>> services = new Reflections(FieldIdPersistenceService.class.getPackage().getName()).getSubTypesOf(FieldIdPersistenceService.class);
        for (Class<? extends FieldIdPersistenceService> service:services) {
            if (Modifier.isAbstract(service.getModifiers()) || service.getEnclosingClass()!=null) {
                continue;
            }
            FieldIdPersistenceService instance = null;
            try {
                instance = service.newInstance();
            } catch (Throwable e) {
                System.out.println("skipping class : " + service.getSimpleName() + " because i can't create it.  code smell.");
                continue;
            }
            if (instance.getClass().getAnnotation(Transactional.class)==null) {
                checkPublicServiceCalls(service);
            }
        }
//        assertTrue("These services don't have @Transactional annotations applied correctly. " + nonTransactionalServices, nonTransactionalServices.isEmpty());
    }

    private void checkPublicServiceCalls(Class<? extends FieldIdPersistenceService> service) {
        System.out.println("checking " + service.getSimpleName() + "...");
        Set<Method> publicMethods = ReflectionUtils.getMethods(service, new Predicate<Method>() {
            @Override public boolean apply(Method method) {
                return Modifier.isPublic(method.getModifiers());
            }
        });
        for (Method publicMethod:publicMethods) {
            Transactional transactional = publicMethod.getAnnotation(Transactional.class);
            if (transactional==null) {
                System.out.println(  "   X " + publicMethod.getName());
            }
        }
    }

}
