package com.n4systems.fieldid.utils;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.n4systems.fieldid.actions.api.FlashScopeAware;

public class FlashScopeMarshaller {

	public static final String FLASH_MESSAGES = "flashScope_Messages";
	public static final String FLASH_ERRORS = "flashScope_Errors";
	
	private final FlashScopeAware flashScopeAction;
	private final HttpSession session;
	private Collection<String> previousFlashMessages = new ArrayList<String>();
	private Collection<String> perviousFlashErrors = new ArrayList<String>();

	

	public FlashScopeMarshaller(FlashScopeAware flashScopeAction, HttpSession session) {
		this.flashScopeAction = flashScopeAction;
		this.session = session;
	}

	public void storeAndRemovePreviousFlashMessages() {
		storePreviousFlashMessages();
		removePreviousFlashMessages();
	}

	private void removePreviousFlashMessages() {
		session.removeAttribute(FLASH_MESSAGES);
		session.removeAttribute(FLASH_ERRORS);
	}

	private void storePreviousFlashMessages() {
		previousFlashMessages = getMessageCollectionFromSession(FLASH_MESSAGES);
		perviousFlashErrors = getMessageCollectionFromSession(FLASH_ERRORS);
	}
	
	@SuppressWarnings("unchecked")
	private Collection<String> getMessageCollectionFromSession(String key) {
		Collection<String> sessionMessages = (Collection<String>) session.getAttribute(key);
		return sessionMessages != null ? sessionMessages : new ArrayList<String>();
	}

	public void applyStoredFlashMessage() {
		applyMessages();
		applyErrors();
	}

	private void applyErrors() {
		for (String error : perviousFlashErrors) {
			flashScopeAction.addActionError(error);
		}
	}

	private void applyMessages() {
		for (String message : previousFlashMessages) {
			flashScopeAction.addActionMessage(message);
		}
	}

	public void moveCurrentRequestFlashMessagesToFlashScope() {
		session.setAttribute(FLASH_MESSAGES, new ArrayList<String>(flashScopeAction.getFlashMessages()));
		session.setAttribute(FLASH_ERRORS, new ArrayList<String>(flashScopeAction.getFlashErrors()));
	}
	
}
