package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.ClosingUIBean;
import org.apache.struts2.util.StrutsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IncludeScriptComponent extends ClosingUIBean {
	public static final String TEMPLATE = "script-close";
	public static final String OPEN_TEMPLATE = "script-open";
	
	private static final String JAVASCRIPT_DIRECTORY = "javascript";
	private static final String JAVASCRIPT_EXTENSION = "js";
	
	private String src;
	private StrutsUtil strutsUtil;

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
			String result = getStrutsUtil().buildUrl(src);
            addParameter("src", result);
        }
	}

    private StrutsUtil getStrutsUtil() {
        if(strutsUtil == null) {
            strutsUtil = new StrutsUtil(stack, request, response);
        }
        return strutsUtil;
    }

	private void formateSrc() {
		if (src.startsWith("/") || src.toLowerCase().startsWith("http")) {
			return;
		}
		
		src = "/" + JAVASCRIPT_DIRECTORY + "/" + src;
		if (!src.toLowerCase().endsWith("." + JAVASCRIPT_EXTENSION)) {
			src += "." + JAVASCRIPT_EXTENSION;
		}

        Object version = stack.findValue("version");
        src += "?"+version;   //e.g. src="javascript/foo.js?ver=e.g. 2012.6"

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
