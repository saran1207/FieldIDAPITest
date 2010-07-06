package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import com.opensymphony.xwork2.util.ValueStack;

public class DynamicLocationComponent extends UIBean {
	
	public static final String TEMPLATE = "dynamicLocation";
 
	private List<Object> nodesList;
	
	public DynamicLocationComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}
	
	@Override
	public void evaluateParams() {
		super.evaluateParams();
		
		addParameter("nodesList", nodesList);
	}

	public void setNodesList(List<Object> list){
		nodesList = new ArrayList<Object>(list);
	}
	
	public List<Object> getNodesList(){
		return  nodesList;
	}

}
