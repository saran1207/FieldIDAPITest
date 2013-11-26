package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;

public class NewOwnerSerializationHandler extends OwnerSerializationHandler {

	public static final String ORGANIZATION_MAP_KEY = "New Organization";
	public static final String CUSTOMER_MAP_KEY = "New Customer/Job Site";
	public static final String DIVISION_MAP_KEY = "New Division";

	public NewOwnerSerializationHandler(Field field) {
		super(field, ORGANIZATION_MAP_KEY, CUSTOMER_MAP_KEY, DIVISION_MAP_KEY);
	}

}
