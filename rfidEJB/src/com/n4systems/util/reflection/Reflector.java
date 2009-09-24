package com.n4systems.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import com.n4systems.util.CollectionFactory;

/**
 * This is where the magic happens ...
 */
public class Reflector {
	// all methods are static, hide the constructor
	protected Reflector() {}
	
	/**
	 * Given an object "obj" and path "exField.subObj.someMethod("Arg1", 25L, 10.0D, 2, 30.5f).anotherObj", returns an object
	 * the value of "obj.getExField().getSubObj().someMethod(new String("Arg1"), new Long(25L), new Double(10.0D), new Integer(2), new Float(30.5f)).getAnotherObj()".
	 * A note on itterables.  If a part of the path is itterable, any further path parts will be executed on the itterable elements not on the list.  For example
	 * "listField.get(5)" will attempt to execute the "get(5)" method on each element of "listField" rather then on "listField" itself.
	 * 
	 * @param  object	The base object which will be used to find the value
	 * @param  path 	String path expression representing fields and method calls relative to object 
	 * 
	 * @return			The object returned by the final path part's method invocation
	 * 
	 * @throws NoSuchMethodException		If a method for a path part cannot be found.  @see java.lang.Class#getMethod(String, Class[])
	 * @throws InvocationTargetException	If a method invocation fails. @see java.lang.reflect.Method#Invoke(Object, Object[])
	 * @throws IllegalAccessException		@see java.lang.reflect.Method#Invoke(Object, Object[])
	 */
	public static <T> Object getPathValue(T object, String path) throws ReflectionException {
		return getPathValue(object, path, null);
	}
	
	/**
	 * Given an object "obj" and path "exField.subObj.someMethod("Arg1", 25L, 10.0D, 2, 30.5f).anotherObj", returns an object
	 * the value of "obj.getExField().getSubObj().someMethod(new String("Arg1"), new Long(25L), new Double(10.0D), new Integer(2), new Float(30.5f)).getAnotherObj()".
	 * 
	 * @param	object	The base object which will be used to find the value
	 * @param	path 	String path expression representing fields and method calls relative to object
	 * @param	filter	A ReflectionFilter
	 * 
	 * @return			The object returned by the final path part's method invocation
	 * 
	 * @see	ReflectionFilter
	 * @throws NoSuchMethodException		If a method for a path part cannot be found.  @see java.lang.Class#getMethod(String, Class[])
	 * @throws InvocationTargetException	If a method invocation fails. @see java.lang.reflect.Method#Invoke(Object, Object[])
	 * @throws IllegalAccessException		@see java.lang.reflect.Method#Invoke(Object, Object[])
	 */
	public static <T, K> Object getPathValue(T object, String path, ReflectionFilter<K> filter) throws ReflectionException {
		Stack<String> pathStack = new Stack<String>();
		
		// split the string on the .'s
		String[] pathParts = path.split("\\.");
		
		// iterate the pathParts backwards since we want the first element to
		// be pushed onto the stack last
		for(int i = pathParts.length - 1; i >= 0; i--) {
			if(pathParts[i].length() > 0) {
				// don't add empty path parts
				pathStack.push(pathParts[i]);
			}
		}
		
		//we have no need for the path parts anymore
		pathParts = null;
		
		try {
			return getPathValue(object, pathStack, filter);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException("Illegal argument given to method call", e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("Unable to find method", e);
		} catch (Exception e) {
			throw new ReflectionException("Unable to reflect object for provided path", e);
		}

	}
	
	/**
	 * Recurses the pathStack invoking and returning method calls for each object/path part
	 * @param	object		The current object to operate on
	 * @param	 pathStack	Stack of String method fields/method calls
	 * @return				The object returned from a method call parsed from the top of the path stack
	 * 						object if the pathStack is empty
	 * 						null if object is null
	 * @see		#getPathValue(T, String)
	 */
	@SuppressWarnings("unchecked")
	protected static <T, K> Object getPathValue(T object, Stack<String> pathStack, ReflectionFilter<K> filter) throws ReflectionException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		Object pathValue = null;
		
		//if the object is null, we cannot go further, return null
		if (object == null) {
			return null;
		}
		
		// if our path stack is empty, then we have reached the end
		if (pathStack.empty()) {
			if(filter != null) {
				return filterObject(object, filter);
			} else {
				// no filter was defined, just return the object itself
				return object;
			}
		}
		
		/*
		 *  XXX - I don't like this syntax anymore since it precludes you from being able to run methods
		 *  on the list itself.  An example would be "product.infoOptions.size()" will attempt to run the 
		 *  size() method on each element of infoOptions.  A better syntax would be to make you explicitly 
		 *  specify that you want to itterate.  eg "product.infoOptions.foreach.infoOption" 
		 */
		// if the object is a collection of some type, we need to iterate the collection and return
		// the value paths for each element   (eg product.infoOptions.infoField will return a list of infoFields)
		if (isIterable(object)) {
			// create list of reflected array elements, note that we do not pop off the pathStack at this point
			pathValue = new ArrayList<Object>();
			
			/*
			 * We will take a copy of the stack on each iteration so that successive calls do not exhaust the original stack.
			 * This process is a little inefficient as it means allocating a new stack for each call and coping all the elements in.
			 * Could probably be improved a little ...
			 */
			Object tmpValue;
			Stack<String> duplicateStack;
			for(Object element: (Iterable<?>)object) {
				duplicateStack = new Stack<String>();
				duplicateStack.addAll(pathStack);
				
				tmpValue = getPathValue(element, duplicateStack, filter);
				
				if(tmpValue != null) {
					// if the value returned is itself a collection, we need to add the elements to our list rather then add the collection.  This is
					// so that we get a single aggregated collection, not a collection of collections
					if(isIterable(tmpValue)) {
						for(Object subElement: (Iterable<?>)tmpValue) {
							((List<Object>)pathValue).add(subElement);
						}
					} else {
						((List<Object>)pathValue).add(tmpValue);
					}
				}
			}
			
		} else {
			//pop the next part off the stack
			String pathVar = pathStack.pop();
			
			String methodName;
			boolean isMethodCall = pathIsMethodCall(pathVar);
			if (isMethodCall) {
				// the path is a method call, we need to isolate the method name
				methodName = parseMethodNameFromMethod(pathVar);
			} else {
				// we have a field name, convert it to a getter
				methodName = createGetter(pathVar);
			}
			
			// get a list of our method args if there are any
			Object[] methodArgs = parseMethodArgs(pathVar);
			
			Method method;
			try {
				// find our path method
				method = findClassMethod(object.getClass(), methodName, methodArgs);
			} catch(NoSuchMethodException e1) {
				if (isMethodCall) {
					// nothing more we can do here
					throw e1;
				} else {
					try {
						// if the path was not a method call (ie a getter), it could be we're dealing with a 
						// boolean which uses the isField syntax. let try and find that before rethrowing
						method = findClassMethod(object.getClass(), createBooleanGetter(pathVar), methodArgs);
					} catch(NoSuchMethodException e2) {
						// the path was not a boolean getter either, we'll ignore the second NME and rethrow the first
						throw e1;
					}
				}
			}
			
			
			// invoke the method with arguments (if any)
			Object methodValue = method.invoke(object, parseMethodArgs(pathVar));
			
			// TODO: check/return element index here eg infoOptions[0]
			if (pathIsIndexCall(pathVar)) {
				String indexStr = parseIndexFromIndexPath(pathVar);
				
				if (isList(methodValue)) {
					// parse the index string as an integer (we'll let it throw NumberFormatExceptions)
					int index = Integer.parseInt(indexStr);
					methodValue = ((List<?>)methodValue).get(index);
				} else if (isArray(methodValue)) {
					// parse the index string as an integer (we'll let it throw NumberFormatExceptions)
					int index = Integer.parseInt(indexStr);
					methodValue = ((Object[])methodValue)[index];					
				} else if (isMap(methodValue)) {
					// parse the index as a map key (this allows us to support Strings and Numbers as keys)
					Object key = parseArgumentToType(indexStr);
					methodValue = ((Map<?, ?>)methodValue).get(key);
				} else {
					// index calls only make sense for lists and maps
					throw new IllegalArgumentException("Index path called on element that is not a Map or List");
				}
			}
			
			// get the next value in the path stack
			pathValue = getPathValue(methodValue, pathStack, filter);
		}
		
		return pathValue;
	}
	
	protected static <K> Object filterObject(Object object, ReflectionFilter<K> filter) throws ReflectionException {
		// if our object is a collection we need to filter the collection elements
		if(isIterable(object)) {
			if(!isCollection(object)) {
				// we can only handle instances of Collection
				throw new ReflectionException("ReflectionFilters can only be used on instances of Collection.  Type was [" + object.getClass() + "]");
			}
			// filter on the collection
			return filterCollectionObject((Collection<?>)object, filter);
		} else {
			// object was not a collection, filter the object itself
			return filterNonCollectionObject(object, filter);        
		}
	}
	
	@SuppressWarnings("unchecked")
	protected static <K> Object filterNonCollectionObject(Object object, ReflectionFilter<K> filter) throws ReflectionException {
		K filterObject;
		
		try {
			// reflect the object with our filter path
			filterObject = (K)getPathValue(object, filter.getPath());
		} catch(ClassCastException e) {
			throw new ReflectionException("Reflected filter path value did not match filter type", e);
		}
		
		// test against the filter
		return filter.isValid(filterObject) ? object : null;
	}
	
	@SuppressWarnings("unchecked")
	protected static <K> Object filterCollectionObject(Collection<?> object, ReflectionFilter<K> filter) throws ReflectionException {
		Collection<Object> newCollection;
		try {
			newCollection = CollectionFactory.createCollection((Collection<Object>)object);
		} catch (Exception e) {
			throw new ReflectionException("Unable to instantate collection of type [" + object.getClass().getName() + "]", e);
		}
		
		Object filterObject;
		for(Object subObject: object) {
			// filter the subobject
			filterObject = filterNonCollectionObject(subObject, filter);
			if(filterObject != null) {
				// our sub object passed filtering, add it to our collection list
				newCollection.add(subObject);
			}
		}
	
		return newCollection;
	}
	
	/**
	 * Converts field names to getters using Java Beans convention.  Eg myField to getMyField
	 * @param	field	String field name
	 * @return			String getter representation of the field
	 */
	protected static String createGetter(String field) {
		String fieldNoIndex = parseFieldFromIndexPath(field);
		return "get" + fieldNoIndex.substring(0, 1).toUpperCase() + fieldNoIndex.substring(1);
	}
	
	/**
	 * Converts field names to a boolean getter using Java Beans convention.  Eg myField to isMyField
	 * @param	field	String field name
	 * @return			String boolean getter representation of the field
	 */
	protected static String createBooleanGetter(String field) {
		String fieldNoIndex = parseFieldFromIndexPath(field);
		return "is" + fieldNoIndex.substring(0, 1).toUpperCase() + fieldNoIndex.substring(1);
	}
	
	/**
	 * Parses method calls to method names.  Eg myMethod("asd", 123) to myMethod
	 * @param 	method 	String representation of a method call eg myMethod("asd", 123)
	 * @return			A String method name parsed from the 'method' or 'method' itself
	 * 					if it contained no opening '('
	 */
	protected static String parseMethodNameFromMethod(String method) {
		int idx = method.indexOf('(');
		
		// method has no '(', just return it back
		if(idx == -1) {
			return method;
		} else {
			return method.substring(0, idx);
		}
	}
	
	/**
	 * Parses fields from field and index paths.  
	 * @param 	method 	String representation of a field and index eg fieldName[2]
	 * @return			A String field name or the field itself
	 * 					if it did not contain an opening '['
	 */
	protected static String parseFieldFromIndexPath(String indexCall) {
		int idx = indexCall.indexOf('[');
		
		// method has no '(', just return it back
		if(idx == -1) {
			return indexCall;
		} else {
			return indexCall.substring(0, idx);
		}
	}
	
	/**
	 * Parses the index portion of an index path
	 * @param 	method 	String representation of a field and index eg fieldName[2]
	 * @return			The index representation
	 */
	protected static String parseIndexFromIndexPath(String indexCall) {
		return indexCall.substring(indexCall.indexOf('[') + 1, indexCall.indexOf(']'));
	}
	
	/**
	 * Finds a Method object represented by methodName for a given Class and with the arguments given by methodArgs
	 * NOTE: none of the method Args can be null.  If a method arg is null, this method will return a null since
	 * it is impossible to get a Class type from a null argument.
	 * NOTE2: This will not find methods with primitive arguments.
	 * 
	 * @param 	objClass	Class implementing the method
	 * @param	methodName	String method name. Eg myMethod
	 * @param	methodArgs	Object array of arguments to be passed to the method (cannot be or contain null's)
	 * @return				The located Method object or null if no method could be found
	 */
	public static Method findClassMethod(Class<?> objClass, String methodName, Object[] methodArgs) throws NoSuchMethodException {
		
		// convert our method args to an array containing their types
		Class<?>[] argTypes = new Class<?>[methodArgs.length];
		for(int i = 0; i < argTypes.length; i++) {
			argTypes[i] = methodArgs[i].getClass();
		}
		
		return objClass.getMethod(methodName, argTypes);
	}
	
	/**
	 * Given a method call like myMethod("arg1", 123L, 123.10), parses out each 
	 * argument into an array of objects, converting them into specific types
	 */
	protected static Object[] parseMethodArgs(String methodCall) {
		
		List<String> argList = parseArgsToArray(methodCall);
		
		// if there are no args to parse return an empty array
		if(argList.isEmpty()) {
			return new Object[0];
		}
		
		Object[] argValues = new Object[argList.size()];
		for(int i = 0; i < argList.size(); i++) {
			argValues[i] = parseArgumentToType(argList.get(i));
		}
		
		return argValues;
	}
	
	/**
	 * Given a String method argument representation, converts the argument into
	 * a specific type defined by the conversion rules below and return it as an Object.
	 * <pre>
	 * Conversion Rules:
	 * 		Type Returned			Rule				Example arg value
	 * 		String				arg is double-quoted.		"my argument"
	 * 						or arg does not start with 
	 * 						a number and is not true/false.
	 * 		Long				arg ends in 'l' or 'L'.		234l or 432L
	 * 		Float				arg ends in 'f' or 'F'		234.1f or 432.1F
	 * 		Double				arg ends in 'd' or 'D'		432.1d or 432.1D
	 * 		Boolean				arg is 'true' or 'false' 	true or false
	 * 						(case insensitive)
	 * 		null				arg is 'null'			null
	 * 		Integer				default				1232
	 * </pre>
	 * @param	arg		String representing the argument
	 * @returns			Object representation of arg based on Conversion Rules above.  Or null if arg is null or zero length
	 * @throws	NumberFormatException	if a number could not be parsed
	 */
	protected static Object parseArgumentToType(String arg) throws NumberFormatException {
		Object argObj;
		
		if(arg == null || arg.length() == 0 || arg.equals("null")) {
			return null;
		}
		
		String argLastCharRemoved = arg.substring(0, arg.length() - 1);
		String argLower = arg.toLowerCase();
		char firstChar = arg.charAt(0);
		char lastCharLower = argLower.charAt(arg.length() - 1);
		
		// if the first char is not a number try to parse a String or boolean
		if(!Character.isDigit(firstChar)) {
			if(argLower.equals("true") || argLower.equals("false")) {
				argObj = new Boolean(argLower);
			} else if (firstChar == '"' || firstChar == '\''){
				//XXX - assumes the last character is also a quote.  Perhaps an exception should be thrown if it is not
				argObj = argLastCharRemoved.substring(1);
			} else {
				// allow to pass through unmodified
				argObj = arg;
			}
		} else if(lastCharLower == 'l') { 
			argObj = new Long(argLastCharRemoved);
		} else if(lastCharLower == 'f') { 
			argObj = new Float(argLastCharRemoved);
		} else if(lastCharLower == 'd') { 
			argObj = new Double(argLastCharRemoved);
		} else {
			argObj = new Integer(arg);
		}
		
		return argObj;
	}
	
	/**
	 * Given a method call like myMethod("arg1", 123L, 123.10), returns a string list containing each arg trimmed
	 * @param	methodCall	string representation of a method call
	 * @returns				String list of arguments
	 */ 
	protected static List<String> parseArgsToArray(String methodCall) {
		List<String> argList = new ArrayList<String>();
		
		// if the path is not a method call, return an empty array
		if(pathIsMethodCall(methodCall)) {
			String argLine = methodCall.substring(methodCall.indexOf('(') + 1, methodCall.indexOf(')'));
			StringTokenizer tok = new StringTokenizer(argLine, ",");
			while(tok.hasMoreTokens()) {
				argList.add(tok.nextToken().trim());
			}
		}
		
		return argList;
	}
	
	/**
	 * Tests if a string representing a method is a call (as opposed to a method name).  Calls have a closing ')'.
	 * @param	pathPart	String path representing a method name or call
	 * @return				true if path contains a ')'.  false otherwise.
	 */
	protected static boolean pathIsMethodCall(String pathPart) {
		return pathPart.contains(")");
	}
	
	/**
	 * Tests if a string representing an index call.  Calls have a closing ']'.
	 * @param	pathPart	String path
	 * @return				true if path contains a ']'.  false otherwise.
	 */
	protected static boolean pathIsIndexCall(String pathPart) {
		return pathPart.contains("]");
	}
	
	/**
	 * @return true iff Object is instanceof java.lang.Iterable<?>
	 */
	protected static boolean isIterable(Object o) {
		return (o instanceof Iterable<?>);
	}
	
	/**
	 * @return true iff Object is instanceof java.util.Collection<?>
	 */
	protected static boolean isCollection(Object o) {
		return (o instanceof Collection<?>);
	}

	/**
	 * @return true iff Object is instanceof java.util.Map<?>
	 */
	protected static boolean isMap(Object o) {
		return (o instanceof Map<?, ?>);
	}

	/**
	 * @return true iff Object is instanceof java.util.List<?>
	 */
	protected static boolean isList(Object o) {
		return (o instanceof List<?>);
	}

	/**
	 * @return true iff Object is an Array
	 */
	protected static boolean isArray(Object o) {
		return o.getClass().isArray();
	}
	
	/**
	 * Recurses a Classes super chain, collecting up all available fields and attempts to locate one with a name matching.
	 * Fields from child classes will be found before parent classes.
	 * @param clazz		Starting class to find fields from
	 * @return			The Field represented by fieldName or null
	 */
	public static Field findField(Class<?> clazz, String fieldName) {
		Field[] allFields = findAllFields(clazz, new Field[0]);
		
		Field searchField = null;
		for (Field field: allFields) {
			if (field.getName().equals(fieldName)) {
				searchField = field;
				break;
			}
		}
		
		return searchField;
	}
	
	/**
	 * Recurses a Classes super chain, collecting up all available fields.  Fields from child classes will appear before super classes.
	 * @param clazz		Starting class to find fields from
	 * @return			An array of fields
	 */
	public static Field[] findAllFields(Class<?> clazz) {
		return findAllFields(clazz, new Field[0]);
	}
	
	/**
	 * Recurses up a Classes super chain, collecting up all available fields.  Fields from child classes will appear before super classes.
	 * @param clazz		Starting class to find fields from
	 * @param fields	An array of fields
	 * @return			An array of fields
	 */
	protected static Field[] findAllFields(Class<?> clazz, Field[] fields) {
		Field[] decFields = clazz.getDeclaredFields();
		Field[] newFields;
		
		if(decFields.length > 0) {
			// if our class has declared fields, create a new array containing our current fields as well as the new ones.
			newFields = new Field[fields.length + decFields.length];
			System.arraycopy(fields, 0, newFields, 0, fields.length);
			System.arraycopy(decFields, 0, newFields, fields.length, decFields.length);
		} else {
			newFields = decFields;
		}
		
		// if the next super class is Object, then return our field array, otherwise recurse into that super class
		return (clazz.getSuperclass().equals(Object.class)) ? newFields : findAllFields(clazz.getSuperclass(), newFields);
	}
}