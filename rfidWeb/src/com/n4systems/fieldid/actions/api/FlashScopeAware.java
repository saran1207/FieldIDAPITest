package com.n4systems.fieldid.actions.api;

import java.util.Collection;

import com.opensymphony.xwork2.ValidationAware;

public interface FlashScopeAware extends ValidationAware {
	

	public void addFlashMessage(String message);

	public void clearFlashScope();

	public void clearFlashMessages();

	public void addFlashError(String error);

	public void clearFlashErrors();
	
	public Collection<String> getFlashMessages();
	
	public Collection<String> getFlashErrors();

}