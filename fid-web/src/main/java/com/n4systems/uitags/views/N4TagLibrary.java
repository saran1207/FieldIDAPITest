package com.n4systems.uitags.views;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.TagLibraryModelProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class N4TagLibrary implements TagLibraryModelProvider {

    @Override
    public Object getModels(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new N4TagModels(stack, req, res);
    }
}
