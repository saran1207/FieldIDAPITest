package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.UIBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class LocationComponent extends UIBean {

	public static final String TEMPLATE = "location";
	
	
	private List<HierarchicalNode> nodesList;
	private String fullName = "";
	
	public LocationComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
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
		addParameter("fullName", fullName);
	}

	public void setNodesList(List<HierarchicalNode> list){
		nodesList = new ArrayList<HierarchicalNode>(list);
	}
	
	public List<HierarchicalNode> getNodesList(){
		return  nodesList;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
