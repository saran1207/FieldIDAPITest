package com.n4systems.exporting.beanutils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.api.validation.validators.NotNullValidator;

public class TestExportBean {
	@SerializableField(title = "Type", order = 0, validators = {NotNullValidator.class})
	private String type;

	@SerializableField(title="Name", order = 10)
	private String name;

	@SerializableField(title="Age", order = 30)
	private Integer age;
	
	@SerializableField(title="Other", order = 40, handler = DummySerializationHandler.class)
	private Integer other;
	
	@SerializableField(title="Date", order = 45)
	private Date date;	
	
	@SerializableField(title="M:", order = 50, handler = MapSerializationHandler.class)
	private Map<String, String> map = new HashMap<String, String>();
	
	public TestExportBean() {}

	public TestExportBean(String type, String name, Integer age, Date date) {
		this.type = type;
		this.name = name;
		this.age = age;
		this.date = date;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getOther() {
		return other;
	}

	public void setOther(Integer other) {
		this.other = other;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
