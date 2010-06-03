package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;
//TODO: extend to move usage to warnings to a a tenant limit bar.  a percent bar in general wouldn't know that there are implications to be at 100%, if it is used as a progress bar then it will be good to be at 100% 
public class PercentBarComponent extends UIBean {
	private static final int USAGE_WARNING_LIMIT = 90;

	private static final int USAGE_EXCEED_LIMIT = 98;

	public static final int UNLIMITED = -1;  

	public static final String TEMPLATE = "percentBar";

	protected Long total;
	protected Long progress;
	
	
	public PercentBarComponent(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(valueStack, httpServletRequest, httpServletResponse);
	}

	
	 @Override
    public void evaluateParams() {
        super.evaluateParams();
        
        addParameter("total", total);
        addParameter("progress", progress);
        addParameter("percentage", getPercentage());
        addParameter("modifierClass", getModifierClass());
    }

    private String getModifierClass() {
    	String result = "usageGoodStanding";
    	if (getPercentage() >= USAGE_WARNING_LIMIT && getPercentage() < USAGE_EXCEED_LIMIT) {
    		result = "usageWarningLevel";
    	} else if (getPercentage() >= USAGE_EXCEED_LIMIT) {
    		result = "usageExceeded";
    	}
		return result;
	}


	private int getPercentage() {
    	if (total == UNLIMITED) {
    		return 0;
    	}
    	if (progress >= total) { 
    		return 100;
    	}
    	return (int)((progress/(double)total) * 100);
	}


	@Override
    protected String getDefaultTemplate() {
        return TEMPLATE;
    }


	public Long getTotal() {
		return total;
	}


	public void setTotal(Long total) {
		this.total = total;
	}


	public Long getProgress() {
		return progress;
	}


	public void setProgress(Long progress) {
		this.progress = progress;
	}
	
	

}
