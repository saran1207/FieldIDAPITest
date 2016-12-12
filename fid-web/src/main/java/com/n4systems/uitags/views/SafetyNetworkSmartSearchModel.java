package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SafetyNetworkSmartSearchModel extends TagModel {

	public SafetyNetworkSmartSearchModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		super(stack, req, res);
	}

	@Override
	protected Component getBean() {
		return new SafetyNetworkSmartSearchComponent(stack, req, res);
	}
}
