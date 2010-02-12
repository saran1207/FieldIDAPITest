package com.n4systems.util.reflection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.util.reflection.beans.NonEmptyChild;
import com.n4systems.util.reflection.beans.ReflectionTestBeanA;
import com.n4systems.util.reflection.beans.ReflectionTestBeanB;
import com.n4systems.util.reflection.beans.ReflectionTestBeanC;
import com.n4systems.util.reflection.beans.ReflectorTestAnno;
import com.n4systems.util.reflection.builders.ReflectionTestBeanABuilder;

public class ReflectorTest {
	private class ExposedReflector extends Reflector {
		
		public String _createGetter(String field) {
			return Reflector.createGetter(field);
		}
		
		public String _createBooleanGetter(String field) {
			return Reflector.createBooleanGetter(field);
		}
		
		public boolean _isArray(Object o) {
			return Reflector.isArray(o);
		}
		
		public boolean _isCollection(Object o) {
			return Reflector.isCollection(o);
		}
		
		public boolean _isIterable(Object o) {
			return Reflector.isIterable(o);
		}
		
		public boolean _isList(Object o) {
			return Reflector.isList(o);
		}
		
		public boolean _isMap(Object o) {
			return Reflector.isMap(o);
		}

		public String _parseFieldFromIndexPath(String path) {
			return Reflector.parseFieldFromIndexPath(path);
		}
		
		public String _parseIndexFromIndexPath(String path) {
			return Reflector.parseIndexFromIndexPath(path);
		}
		
		public String _parseMethodNameFromMethod(String path) {
			return Reflector.parseMethodNameFromMethod(path);
		}
		
		public boolean _pathIsIndexCall(String path) {
			return Reflector.pathIsIndexCall(path);
		}
		
		public boolean _pathIsMethodCall(String path) {
			return Reflector.pathIsMethodCall(path);
		}
	}
	
	private ExposedReflector exposedReflector = new ExposedReflector();
	private ReflectionTestBeanA bean;
	
	@Before
	public void setup() {
		bean = ReflectionTestBeanABuilder.defaultBuilder().build();
	}
	
	@Test(expected=ReflectionException.class)
	public void bad_path_throws_exception() throws ReflectionException {
		Reflector.getPathValue(bean, "jklasdjlkas");
	}
	
	@Test 
	public void empty_path_returns_root_object() throws ReflectionException {
		assertSame(bean, Reflector.getPathValue(bean, ""));
	}
	
	@Test 
	public void get_shallow_values() throws ReflectionException {
		assertSame(bean.getId(), Reflector.getPathValue(bean, "id"));
		assertSame(bean.getName(), Reflector.getPathValue(bean, "name"));
		assertSame(bean.getBeanB(), Reflector.getPathValue(bean, "beanB"));
		assertSame(bean.getBeanBList(), Reflector.getPathValue(bean, "beanBList"));
		assertSame(bean.getBeanBSet(), Reflector.getPathValue(bean, "beanBSet"));
		assertSame(bean.getSimpleMap(), Reflector.getPathValue(bean, "simpleMap"));
		assertSame(bean.getBeanMap(), Reflector.getPathValue(bean, "beanMap"));	
	}
	
	@Test
	public void get_path_value_tries_boolean_getter() throws ReflectionException {
		assertEquals(bean.isBoolValue(), Reflector.getPathValue(bean, "boolValue"));
	}
	
	@Test 
	public void get_deep_values() throws ReflectionException {
		assertSame(bean.getBeanB().getBeanC().getName(), Reflector.getPathValue(bean, "beanB.beanC.name"));
		assertSame(bean.getBeanB().getBeanCList(), Reflector.getPathValue(bean, "beanB.beanCList"));
	}

	@Test 
	public void method_calls() throws ReflectionException {
		assertSame(bean.getBeanB(), Reflector.getPathValue(bean, "returnBeanB()"));
		assertSame(bean.getBeanB().getBeanC(), Reflector.getPathValue(bean, "returnBeanB().beanC"));
		assertSame(bean.getBeanB().getBeanC().getClass().getName(), Reflector.getPathValue(bean, "returnBeanB().beanC.class.name"));
	}
	
	@Test 
	public void sub_lists() throws ReflectionException {
		List<String> beanBNames = new ArrayList<String>();
		for (ReflectionTestBeanB beanB: bean.getBeanBList()) {
			beanBNames.add(beanB.getName());
		}
		
		assertEquals(beanBNames, Reflector.getPathValue(bean, "beanBList.name"));
	}
	
	@Test 
	public void sub_sets() throws ReflectionException {
		List<String> beanCNames = new ArrayList<String>();
		for (ReflectionTestBeanB beanB: bean.getBeanBSet()) {
			beanCNames.add(beanB.getBeanC().getName());
		}
		
		assertEquals(beanCNames, Reflector.getPathValue(bean, "beanBSet.beanC.name"));
	}
	
	@Test 
	public void sub_sub_lists() throws ReflectionException {
		List<String> beanCNames = new ArrayList<String>();
		
		for (ReflectionTestBeanB beanB: bean.getBeanBList()) {
			for (ReflectionTestBeanC beanC: beanB.getBeanCList()) {
				beanCNames.add(beanC.getName());
			}
		}
		
		assertEquals(beanCNames, Reflector.getPathValue(bean, "beanBList.beanCList.name"));
	}
	
	@Test 
	public void find_all_fields() {
		// fields from child classes come before super classes
		String[] fieldNames = {"beanB", "beanBList", "beanBArray", "beanBSet", "simpleMap", "beanMap", "boolValue", "id", "name"};
		Field[] fields = Reflector.findAllFields(ReflectionTestBeanA.class);
		
		assertEquals(fieldNames.length, fields.length);
		
		// fields should be in the same order
		for (int i = 0; i < fields.length; i++) {
			assertEquals("i = " + i + ": expected: " + fieldNames[i] + ", was: " + fields[i].getName(), fieldNames[i], fields[i].getName());
		}
	}
	
	@Test 
	public void find_field() {
		assertEquals("beanB", Reflector.findField(ReflectionTestBeanA.class, "beanB").getName());		
		assertEquals("id", Reflector.findField(ReflectionTestBeanA.class, "id").getName());
	}
	
	@Test 
	public void find_method() throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String str1 = "some string";
		Long lng1 = 15L;
		boolean bool1 = true;
		Integer int1 = 3;
		
		Method method1 = Reflector.findClassMethod(ReflectionTestBeanA.class, "testMethod", new Object[] {str1, lng1, bool1});
		Method method2 = Reflector.findClassMethod(ReflectionTestBeanA.class, "testMethod", new Object[] {str1, lng1, int1});
		
		assertEquals(bean.testMethod(str1, lng1, true), method1.invoke(bean, str1, lng1, bool1));		
		assertEquals(bean.testMethod(str1, lng1, int1), method2.invoke(bean, str1, lng1, int1));
	}
	
	@Test 
	public void filtered_empty_path() throws ReflectionException {
		final String fieldName = "aname";
		
		bean.setName(fieldName);
		
		Object rootBean = Reflector.getPathValue(bean, "", new ReflectionFilter<String>() {
			public String getPath() {
				return "name";
			}

			public boolean isValid(String object) {
				return (object == fieldName);
			}
		});
		
		assertSame(bean, rootBean);
	
		
		// no bean will pass filter, reflector must return null
		assertNull(Reflector.getPathValue(bean, "", new ReflectionFilter<String>() {
			public String getPath() {
				return "name";
			}

			public boolean isValid(String object) {
				return false;
			}
		}));
	}
	
	@Test 
	public void filtered_single() throws ReflectionException {
		final String beanBName = bean.getBeanB().getName();
		
		 Object beanB = Reflector.getPathValue(bean, "beanB", new ReflectionFilter<String>() {

			public String getPath() {
				return "name";
			}

			public boolean isValid(String object) {
				return (object == beanBName);
			}
			
		});
		
		assertSame(bean.getBeanB(), beanB);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void filtered_list() throws ReflectionException {
		
		final Long[] ids = {
				bean.getBeanBList().get(0).getId(),
				bean.getBeanBList().get(1).getId(),
				bean.getBeanBList().get(4).getId()
			}; 
		
		
		List<ReflectionTestBeanB> beanBList = (List<ReflectionTestBeanB>)Reflector.getPathValue(bean, "beanBList", new ReflectionFilter<Long>() {
			public String getPath() {
				return "id";
			}

			public boolean isValid(Long object) {
				for (Long id: ids) {
					if (id.equals(object)) {
						return true;
					}
				}
				return false;
			}
		});
		
		assertEquals(ids.length, beanBList.size());
		
		assertEquals(bean.getBeanBList().get(0), beanBList.get(0));
		assertEquals(bean.getBeanBList().get(1), beanBList.get(1));
		assertEquals(bean.getBeanBList().get(4), beanBList.get(2));
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void filtered_list_using_simple_reflection_filter() throws ReflectionException, ParseException {
		String bean0Name = bean.getBeanBList().get(0).getName();
		
		List<ReflectionTestBeanB> beanBList = (List<ReflectionTestBeanB>) Reflector.getPathValue(bean, "beanBList", new SimpleReflectionFilter("name=" + bean0Name));
		
		assertEquals(1, beanBList.size());
		assertEquals(bean.getBeanBList().get(0), beanBList.get(0));
	}
	
	@Test 
	public void get_list_element() throws ReflectionException {
		assertSame(bean.getBeanBList().get(3), Reflector.getPathValue(bean, "beanBList[3]"));
	}
	
	@Test 
	public void get_array_element() throws ReflectionException {
		assertSame(bean.getBeanBArray()[3], Reflector.getPathValue(bean, "beanBArray[3]"));
	}
	
	@Test 
	public void get_simple_map_element() throws ReflectionException {
		List<String> keys = new ArrayList<String>(bean.getSimpleMap().keySet());
		
		String mapKey = keys.get(3);
		
		assertSame(bean.getSimpleMap().get(mapKey), Reflector.getPathValue(bean, "simpleMap['" + mapKey + "']"));
	}
	
	@Test 
	public void get_complex_map_element() throws ReflectionException {
		List<String> keys = new ArrayList<String>(bean.getBeanMap().keySet());
		
		String mapKey = keys.get(3);
		
		assertSame(bean.getBeanMap().get(mapKey).getBeanC().getName(), Reflector.getPathValue(bean, "beanMap['" + mapKey + "'].beanC.name"));
	}
	
	@Test 
	public void get_complex_map_element_from_method() throws ReflectionException {
		List<String> keys = new ArrayList<String>(bean.getBeanMap().keySet());
		
		String mapKey = keys.get(3);
		
		assertSame(bean.getBeanMap().get(mapKey).getBeanC().getName(), Reflector.getPathValue(bean, "returnBeanMap()['" + mapKey + "'].beanC.name"));
	}
	
	@Test 
	public void get_map_element_when_key_not_found_returns_null() throws ReflectionException {
		assertNull(Reflector.getPathValue(bean, "beanMap[12412].beanC.name"));
	}
	
	@Test (expected=ReflectionException.class)
	public void get_list_element_throws_exception_when_index_cannot_be_parsed() throws ReflectionException {
		Reflector.getPathValue(bean, "beanBList[xyz].beanC.name");
	}
	
	@Test
	public void test_type_checking() {
		assertTrue(exposedReflector._isArray(new Object[3]));
		assertTrue(exposedReflector._isCollection(new ArrayList<Object>()));
		assertTrue(exposedReflector._isCollection(new HashSet<Object>()));
		assertTrue(exposedReflector._isIterable(new ArrayList<Object>()));
		assertTrue(exposedReflector._isIterable(new HashSet<Object>()));
		assertTrue(exposedReflector._isList(new ArrayList<Object>()));
		assertTrue(exposedReflector._isList(new LinkedList<Object>()));
		assertFalse(exposedReflector._isList(new HashSet<Object>()));
		assertTrue(exposedReflector._isMap(new HashMap<Object, Object>()));
		assertTrue(exposedReflector._isMap(new TreeMap<Object, Object>()));
	}
	
	@Test
	public void test_create_getters() {
		assertEquals("getMyField", exposedReflector._createGetter("myField"));
		assertEquals("getMyField", exposedReflector._createGetter("myField[index]"));
	}
	
	@Test
	public void test_create_boolean_getters() {
		assertEquals("isMyField", exposedReflector._createBooleanGetter("myField"));
		assertEquals("isMyField", exposedReflector._createBooleanGetter("myField[index]"));
	}
	
	@Test
	public void test_parse_field_from_path() {
		assertEquals("myField", exposedReflector._parseFieldFromIndexPath("myField[index]"));
	}
	
	@Test
	public void test_parse_index_from_path() {
		assertTrue(exposedReflector._pathIsIndexCall("myMethod(args, 123)[213]"));
		assertFalse(exposedReflector._pathIsIndexCall("myMethod(args, 123)"));
		
		assertEquals("index", exposedReflector._parseIndexFromIndexPath("myField[index]"));
	}
	
	@Test
	public void test_parse_method_name() {
		assertTrue(exposedReflector._pathIsMethodCall("myMethod(args, 123)"));
		assertFalse(exposedReflector._pathIsMethodCall("myMethod"));
		
		assertEquals("myMethod", exposedReflector._parseMethodNameFromMethod("myMethod()"));
		assertEquals("myMethod", exposedReflector._parseMethodNameFromMethod("myMethod(args, 123)"));
		assertEquals("myMethod", exposedReflector._parseMethodNameFromMethod("myMethod(args, 123)[23]"));
	}
	
	@Test
	public void test_find_all_fields_with_annotation() {
		Field[] fields = Reflector.findAllFieldsWithAnnotation(ReflectionTestBeanA.class, ReflectorTestAnno.class);
		
		Set<String> fieldNames = new HashSet<String>();
		for (Field field: fields) {
			fieldNames.add(field.getName());
		}
	
		assertEquals(3, fieldNames.size());
		assertTrue(fieldNames.containsAll(Arrays.asList("name", "beanBArray", "beanMap")));
	}
	
	@Test
	public void test_find_all_fields_allows_parent_class_to_have_no_fields() {

		
		Field[] fields = Reflector.findAllFields(NonEmptyChild.class);
		
		assertEquals(1, fields.length);
		assertEquals("field", fields[0].getName());
	}
}
