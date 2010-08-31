package com.n4systems;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.junit.Ignore;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;

//TODO: move to the integeration tests and add integration tests to the ant build.
public class BeanPropertyDescriptorsInWrongOrderFreemarkerWillEndUpInvokingTheWrongMethodTest {

	@Test
    public void should_get_the_parent_method_for_get_id() throws Exception {
		Implementation implementation = new Implementation();
        
        final Class<?> fclz  = implementation.getClass();
     
        
        BeanInfo beanInfo = Introspector.getBeanInfo(fclz);
        PropertyDescriptor[] pda = beanInfo.getPropertyDescriptors();
        
        boolean found = false;
        for (PropertyDescriptor propertyDescriptor : pda) {
			if (propertyDescriptor.getName().equals("id")) {
				assertEquals(Long.class, propertyDescriptor.getReadMethod().getReturnType());
				assertNotNull(propertyDescriptor.getReadMethod().getAnnotation(UseThis.class));
				found = true;
			}
		}
        
        assertTrue(found);
    }
	
	
	
	@Ignore
	@Test
    public void should_get_the_parent_method_implementation_even_with_a_interface_with_the_same_method_using_a_generic() throws Exception {
		ImplementationWithInterface implementation = new ImplementationWithInterface();
        
		Enhancer e = new Enhancer();
		
		e.setSuperclass(implementation.getClass());
		e.setCallback(new MethodInterceptor() {
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				if(method.getAnnotation(UseThis.class) != null) {
					return 1L;
				}
				throw new RuntimeException("could not find the right annotation");
			}
		});
		
		ImplementationWithInterface enhancedEntity = (ImplementationWithInterface) e.create();
     
        
        BeanModel beanModel = new BeanModel(enhancedEntity, new BeansWrapper());
        
        assertThat(beanModel.get("id").toString(), equalTo("1"));
    }
	
	@Ignore
	@Test
    public void should_work_with_enhanced_class() throws Exception {
		ImplementationWithInterface implementation = new ImplementationWithInterface();
		ImplementationWithInterface enhanced = EntitySecurityEnhancer.enhanceEntity(implementation, SecurityLevel.DIRECT);
        
		BeanModel beanModel = new BeanModel(enhanced, new BeansWrapper());
        assertThat(beanModel.get("id"), is(notNullValue()));
        assertThat(beanModel.get("id").toString(), equalTo(enhanced.getId().toString()));
    }
	
	@Test
	@Ignore
    public void should_work_with_enhanced_product() throws Exception {
		Product asset = ProductBuilder.aProduct().build();
		Product enhancedAsset = asset.enhance(SecurityLevel.DIRECT);
        
		BeanModel beanModel = new BeanModel(enhancedAsset, new BeansWrapper());
        assertThat(beanModel.get("id"), is(notNullValue()));
        assertThat(beanModel.get("id").toString(), equalTo(asset.getId().toString()));
    }
	
	
}
