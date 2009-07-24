package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;

import com.opensymphony.xwork2.util.ValueStack;

public class PercentBarComponent extends UIBean {
	private static final int UNLIMITED = -1;  // TODO: should tie into the value from the limit classes   or a more global concept of unlimited in the system.

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
