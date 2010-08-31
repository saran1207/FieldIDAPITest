package com.n4systems.util.persistence;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.exceptions.InvalidQueryException;

public class NewObjectSelect extends SelectClause {
	private static final long serialVersionUID = 1L;
	
	private Class<?> objectClass;
	private List<String> constructorArgs = new ArrayList<String>();
	
	public NewObjectSelect() {}
	
	public NewObjectSelect(Class<?> objectClass, String...constructorArgs) {
		this.objectClass = objectClass;
		
		for(String arg: constructorArgs) {
			this.constructorArgs.add(arg);
		}
	}
	
	public Class<?> getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(Class<?> objectClass) {
		this.objectClass = objectClass;
	}

	public List<String> getConstructorArgs() {
		return constructorArgs;
	}

	public void setConstructorArgs(List<String> constructorArgs) {
		this.constructorArgs = constructorArgs;
	}

	private String buildConstructor() {
		String constructor = "(";
		
		if(constructorArgs != null && !constructorArgs.isEmpty()) {
			boolean firstArg = true;
			for(String arg: constructorArgs) {
				if(firstArg) {
					firstArg = false;
				} else {
					constructor += ", ";
				}
				
				constructor += arg;
			}
		}
		
		constructor += ")";
		return constructor;
	}
	
	@Override
	protected String getClauseArgument(FromTable table) throws InvalidQueryException {
		if(objectClass == null) {
			throw new InvalidQueryException("Object Class is required for a new object select query");
		}
		
		return "new " + objectClass.getName() + buildConstructor();
	}

}
