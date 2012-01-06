package com.n4systems.fieldid;

import static org.easymock.EasyMock.*;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Maps;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;


/**
 * class used for spring based tests where the fixture needs to be injected with mock beans
 * for example, most tests will be like this...
 *    fixture = ServiceA,    fixture collaborates with ServiceB, ServiceC, ServiceD      // i use the term fixture, others use SUT or system under test.
 *  so, in effect what you want to do for each test is...
 *    fixture = new ServiceA(). 
 *    fixture.setServiceB(createMock(ServiceB.class)); 
 *    fixture.setServiceC(createMock(ServiceC.class)); 
 *    fixture.setServiceD(createMock(ServiceD.class));
 *  the autoWire method does all this stuff for you. 
 */
public class FieldIdUnitTest {

	private static final Logger logger = Logger.getLogger(FieldIdUnitTest.class);

	public FieldIdUnitTest() {
		super();
	}

	public void setUp() {
		try {
			Field sutField = findSutField();
			Object sut = createSut(sutField);
			ReflectionTestUtils.setField(this, sutField.getName(), sut);						
			autoWireSut(sut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Object createSut(Field sutField) throws Exception {
		return sutField.getType().newInstance();
	}

	private Field findSutField() {
		Field sutField = null;
		for (Field field:getClass().getDeclaredFields()) {
			if (field.getAnnotation(TestTarget.class)!=null) {
				if (sutField!=null) { 
					throw new IllegalStateException("You can not have more than one @" + TestTarget.class.getSimpleName() + " fields in the same test"); 
				}
				sutField = field;	
			}
		}
		if (sutField==null) { 
			throw new IllegalStateException("Every test must have one field annotated with @" + TestTarget.class.getSimpleName() + " to denote which class is being tested"); 
		}
		return sutField;
	}

	private  Object autoWireSut(Object sut) {
		Map<String,Object> mocks = createTestMocks();
		inject(sut, mocks);		
		return sut;
	}

	private void inject(Object sut, Map<String,Object> mocks) {
		for (String name:mocks.keySet()) {
			Field field = ReflectionUtils.findField(sut.getClass(), name); 
			if (field!=null) { 
				ReflectionTestUtils.setField(sut, name, mocks.get(name));
			} else { 
				throw new IllegalStateException("Can't find field " + name + " in sut " + sut.getClass().getSimpleName() + " when inject mocks. ");
			}
		}		
	}
	
	private Map<String, Object> createTestMocks() {
		Map<String, Object> mocks = Maps.newHashMap();
		for (Field field:getClass().getDeclaredFields()) {
			if (field.getAnnotation(TestMock.class)!=null) {
				Object mock = createMock(field.getType());
				mocks.put(field.getName(), mock);
				ReflectionTestUtils.setField(this, field.getName(), mock);								
			}
		}
		return mocks;		
	}

	
	protected void verifyTestMocks() { 
		try {
			for (Field field:getClass().getDeclaredFields()) {
				if (field.getAnnotation(TestMock.class)!=null) {
					field.setAccessible(true);
					verify(field.get(this));
				}
			}		
		} catch (Exception e) {
			System.out.println("Can't verify mocks " + e.getLocalizedMessage());
		}
	}
	
}