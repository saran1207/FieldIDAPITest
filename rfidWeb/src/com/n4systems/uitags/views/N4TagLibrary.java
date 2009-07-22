package com.n4systems.uitags.views;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.views.TagLibrary;

import com.n4systems.exceptions.NotImplementedException;
import com.opensymphony.xwork2.util.ValueStack;

public class N4TagLibrary implements TagLibrary {

	public Object getFreemarkerModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new N4TagModels(stack, req, res);
	}

	@SuppressWarnings("unchecked")
	public List<Class> getVelocityDirectiveClasses() {
		throw new NotImplementedException("no velocity templates will be implemented.");
	}

}
