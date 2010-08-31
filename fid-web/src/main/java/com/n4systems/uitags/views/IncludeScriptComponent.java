package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.ClosingUIBean;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.util.ValueStack;

public class IncludeScriptComponent extends ClosingUIBean {
	public static final String TEMPLATE = "script-close";
	public static final String OPEN_TEMPLATE = "script-open";
	
	private static final String JAVASCRIPT_DIRECTORY = "javascript";
	private static final String JAVASCRIPT_EXTENSION = "js";
	
	private String src;
	
		
	
	public IncludeScriptComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	 @Override
    public void evaluateParams() {
        super.evaluateParams();
        buildScriptSource();
    }

	private void buildScriptSource() {
		if (src != null) {
        	formateSrc();
			String result = UrlHelper.buildUrl(src, request, response, null);
            addParameter("src", result);
        }
	}
	
	private void formateSrc() {
		if (src.startsWith("/") || src.toLowerCase().startsWith("http")) {
			return;
		}
		
		src = "/" + JAVASCRIPT_DIRECTORY + "/" + src;
		if (!src.toLowerCase().endsWith("." + JAVASCRIPT_EXTENSION)) {
			src += "." + JAVASCRIPT_EXTENSION;
		}
	}

	public String getDefaultOpenTemplate() {
		return OPEN_TEMPLATE;
	}

	   
	 
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}
	


	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	

}
