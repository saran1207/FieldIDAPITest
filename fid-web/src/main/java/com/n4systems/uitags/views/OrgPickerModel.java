package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.freemarker.tags.TagModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrgPickerModel extends TagModel {

	public OrgPickerModel(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		super(stack, req, res);
	}

	@Override
	protected Component getBean() {
		return new OrgPickerComponent(stack, req, res);
	}

}
