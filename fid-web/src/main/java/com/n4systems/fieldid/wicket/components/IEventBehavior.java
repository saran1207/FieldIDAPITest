package com.n4systems.fieldid.wicket.components;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;


/**
 * class required when more than one pieces of code to be called when an event is fired. 
 * i.e. currently, if you just add two AjaxEventBehavior("onchange") to a component, only one will get fired. 
 * 
 *  use this interface to chain these behaviors together.
 */
public interface IEventBehavior extends Serializable {
	
	void onEvent(AjaxRequestTarget target);

	
}
