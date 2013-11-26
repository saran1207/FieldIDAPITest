package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import com.n4systems.util.StringUtils;

public class OwnerSerializationHandler extends SerializationHandler<String[]> {

	public static final int ORGANIZATION_INDEX = 0;
	public static final int CUSTOMER_ID = 1;
	public static final int DIVISION_INDEX = 2;

	public static final String ORGANIZATION_MAP_KEY = "Organization";
	public static final String CUSTOMER_MAP_KEY = "Customer/Job Site";
	public static final String DIVISION_MAP_KEY = "Division";

    private String organizationMapKey;
    private String customerMapKey;
    private String divisionMapKey;
	
	public OwnerSerializationHandler(Field field) {
		this(field, ORGANIZATION_MAP_KEY, CUSTOMER_MAP_KEY, DIVISION_MAP_KEY);
	}

    protected OwnerSerializationHandler(Field field, String organizationMapKey, String customerMapKey, String divisionMapKey) {
        super(field);
        this.organizationMapKey = organizationMapKey;
        this.customerMapKey = customerMapKey;
        this.divisionMapKey = divisionMapKey;
    }

	@Override
	public boolean handlesField(String title) {
		return title.equals(organizationMapKey) || title.equals(customerMapKey) || title.equals(divisionMapKey);
	}

	@Override
	public Map<String, Object> marshal(Object bean) throws MarshalingException {
		Map<String, Object> outputMap = new LinkedHashMap<String, Object>();
		
		String[] ownerArray = getFieldValue(bean);
		outputMap.put(organizationMapKey, ownerArray[ORGANIZATION_INDEX]);
		outputMap.put(customerMapKey, ownerArray[CUSTOMER_ID]);
		outputMap.put(divisionMapKey, ownerArray[DIVISION_INDEX]);
		
		return outputMap;
	}

	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		String[] ownerArray = getFieldValue(bean);
		
		String cleanName = StringUtils.clean((String)value);
		
		if (title.equals(organizationMapKey)) {
			ownerArray[ORGANIZATION_INDEX] = cleanName;
		} else if (title.equals(customerMapKey)) {
			ownerArray[CUSTOMER_ID] = cleanName;
		} else if (title.equals(divisionMapKey)) {
			ownerArray[DIVISION_INDEX] = cleanName;
		}
	}
	
}
