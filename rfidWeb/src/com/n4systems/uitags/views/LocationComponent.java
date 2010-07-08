package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

public class LocationComponent extends UIBean {

	public static final String TEMPLATE = "location";
	
	
	private List<HeirarchicalNode> nodesList;
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

	public void setNodesList(List<HeirarchicalNode> list){
		nodesList = new ArrayList<HeirarchicalNode>(list);
	}
	
	public List<HeirarchicalNode> getNodesList(){
		return  nodesList;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
