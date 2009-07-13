package com.n4systems.test.helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.TestCase;

public abstract class EJBTestCase extends TestCase {

	/**
	  * Inject the given dependency into the named field on the
	  * target object, also verifying that the field has been
	  * annotated properly.
	  */
	public static void injectField( Object target, String fieldName, Object dep ) throws Exception {
	     Field field = target.getClass().getDeclaredField(fieldName);
	     Annotation ejb = field.getAnnotation(EJB.class);
	     Annotation persistenceContext = field.getAnnotation( PersistenceContext.class );
	     
	     boolean wasFound = (ejb != null || persistenceContext != null );
	     assertTrue("Missing @EJB or @PersistenceContext annotation", wasFound);
	     
	     field.setAccessible(true);
	     field.set(target, dep);       
	}

	public EJBTestCase() {
		super();
	}

	protected void injectEntityManager( Object manager, EntityManager mockEntityManager ) {
		try {
			injectField( manager, "em", mockEntityManager );
		} catch( Exception e ){
			e.printStackTrace();
			fail( "mock not injected correctly" );
			return;
		}
	}

	public EJBTestCase( String name ) {
		super( name );
	}

	
	
	
	
}