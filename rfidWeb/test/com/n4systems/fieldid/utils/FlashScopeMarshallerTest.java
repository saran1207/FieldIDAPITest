package com.n4systems.fieldid.utils;

import static com.n4systems.fieldid.utils.FlashScopeMarshaller.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.actions.api.FlashScopeAware;
import com.n4systems.test.helpers.FluentArrayList;
import com.opensymphony.xwork2.ValidationAwareSupport;


public class FlashScopeMarshallerTest {
	private FlashScopeAware flashScopeAction;

	private final class FlashScopeAwareFake extends ValidationAwareSupport implements FlashScopeAware {
		
		private Collection<String> flashMessages = new ArrayList<String>();
		private Collection<String> flashError = new ArrayList<String>();

		public void clearFlashScope() {
			clearFlashErrors();
			clearFlashMessages();
		}

		public void clearFlashMessages() {
			flashMessages.clear();
		}

		public void clearFlashErrors() {
			flashError.clear();
		}

		public void addFlashMessage(String message) {
			flashMessages.add(message);
			
		}

		public void addFlashError(String error) {
			flashError.add(error);
		}

		public Collection<String> getFlashErrors() {
			return flashError;
		}

		public Collection<String> getFlashMessages() {
			return flashMessages;
		}
	}

	
	@Before
	public void createFlashScopeAction() {
		flashScopeAction = new FlashScopeAwareFake();
	}
	
	
	@Test
	public void should_retieve_flash_messages_from_session_and_apply_to_action() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		expect(session.getAttribute(FLASH_MESSAGES)).andReturn(new FluentArrayList<String>("message 1"));
		replay(session);
			
			
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		sut.storeAndRemovePreviousFlashMessages();
		sut.applyStoredFlashMessage();
		
		assertEquals(new FluentArrayList<String>("message 1"), flashScopeAction.getActionMessages());
	}
	
	
	@Test
	public void should_retieve_flash_errors_from_session_and_apply_to_action() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		expect(session.getAttribute(FLASH_ERRORS)).andReturn(new FluentArrayList<String>("error 1"));
		replay(session);
			
			
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		sut.storeAndRemovePreviousFlashMessages();
		sut.applyStoredFlashMessage();
		
		assertEquals(new FluentArrayList<String>("error 1"), flashScopeAction.getActionErrors());
	}
	
	@Test
	public void should_clear_flash_messages_after_retrieveal() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		expect(session.getAttribute(FLASH_MESSAGES)).andReturn(new ArrayList<String>());
		session.removeAttribute(FLASH_MESSAGES);
		replay(session);
			
			
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		sut.storeAndRemovePreviousFlashMessages();
		
		verify(session);
	}
	
	@Test
	public void should_clear_flash_error_after_retrieveal() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		expect(session.getAttribute(FLASH_ERRORS)).andReturn(new ArrayList<String>());
		session.removeAttribute(FLASH_ERRORS);
		replay(session);
			
			
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		sut.storeAndRemovePreviousFlashMessages();
		
		verify(session);
	}
	
	
	@Test
	public void should_store_actions_flash_messages_to_session() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		session.setAttribute(FLASH_MESSAGES, new FluentArrayList<String>("message"));
		replay(session);
		
		flashScopeAction.addFlashMessage("message");
		
		
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		sut.moveCurrentRequestFlashMessagesToFlashScope();

		verify(session);
	}
	
	
	@Test
	public void should_store_actions_flash_errors_to_session() throws Exception {
		HttpSession session = createNiceMock(HttpSession.class);
		session.setAttribute(FLASH_ERRORS, new FluentArrayList<String>("error"));
		replay(session);
		
		flashScopeAction.addFlashError("error");
		
		
		FlashScopeMarshaller sut = new FlashScopeMarshaller(flashScopeAction, session);
		
		
		sut.moveCurrentRequestFlashMessagesToFlashScope();
		
		verify(session);
	}
	
	
	
	
}
