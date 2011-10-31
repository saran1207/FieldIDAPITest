package com.n4systems.fieldid.wicket.util;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

public interface AjaxCallback<T> extends Serializable {

    public void call(AjaxRequestTarget target, T arg);

}
