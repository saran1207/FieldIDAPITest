package com.n4systems.uitags.views;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.xwork2.util.ValueStack;

public class N4TagModels {
	
    private ValueStack stack;
    private HttpServletRequest req;
    private HttpServletResponse res;
    
    private PercentBarModel percentBar;

    public N4TagModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        this.stack = stack;
        this.req = req;
        this.res = res;
    }

    
    public PercentBarModel getPercentbar() {
    	 if (this.percentBar == null) {
             this.percentBar = new PercentBarModel(this.stack, this.req, this.res);
         }

         return this.percentBar;
    }
    
    public void getIncludeJs() {
    	
    }
    
    public void getIncludeStyle() {
    	
    }
    
    public void getIncludeVendorJs() {
    	
    }

}
