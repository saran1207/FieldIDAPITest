package com.n4systems.exporting.beanutils;

import com.n4systems.api.validation.validators.NotNullValidator;

public class TestExportBean {
	@ExportField(title = "Type", order = 0, validators = {NotNullValidator.class})
	private String type;

	@ExportField(title="Name", order = 10)
	private String name;

	@ExportField(title="Age", order = 30)
	private Integer age;
	
	@ExportField(title="Other", order = 40, handler = DummySerializationHandler.class)
	private Integer other;
	
	public TestExportBean() {}
	
	public TestExportBean(String type, String name, Integer age) {
		this.type = type;
		this.name = name;
		this.age = age;
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
	
}
