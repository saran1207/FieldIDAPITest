package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.n4systems.model.orgs.BaseOrg;
import com.opensymphony.xwork2.util.ValueStack;

public class OrgPickerComponent extends UIBean {
	public static final String TEMPLATE = "orgPicker";
	
	
	public OrgPickerComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	@Override
	public void evaluateParams() {
		super.evaluateParams();
		BaseOrg selectedOrg = (BaseOrg)getParameters().get("nameValue");
		addParameter("idName", findString("name") + "Id");
		
		if (selectedOrg != null) {
			addParameter("idNameValue", selectedOrg.getId());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Class getValueClassType() {
		return BaseOrg.class;
	}
	
	
	
}
