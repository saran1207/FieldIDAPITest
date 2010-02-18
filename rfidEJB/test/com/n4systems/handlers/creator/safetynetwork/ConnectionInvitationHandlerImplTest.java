package com.n4systems.handlers.creator.safetynetwork;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.net.URI;
import java.util.Random;

import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.messages.Message;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.messages.MessageSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.AdminUserListLoader;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.NullNotifier;
import com.n4systems.notifiers.TestSingleNotifier;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;
import com.n4systems.util.NonDataSourceBackedConfigContext;
import com.n4systems.util.uri.ActionURLBuilder;


public class ConnectionInvitationHandlerImplTest {

	
	private static final String BASE_URI = "https://n4.fielid.com/";
	private PrimaryOrg remoteOrg;
	private PrimaryOrg localOrg;
	private UserBean adminUser;

	@Before
	public void setup() {
		remoteOrg = aPrimaryOrg().withName("receiving org").build();
		localOrg = aPrimaryOrg().withName("sending org").build();
		adminUser = anAdminUser().build();
	}

	@Test
	public void should_save_invitation_message() throws Exception {
		MessageSaver saver = createMock(MessageSaver.class);
		saver.save((Transaction)anyObject(), (Message)anyObject());
		replay(saver);
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		verify(saver);
	}
	
	private void addRequiredFieldsToCreationHandler(ConnectionInvitationHandlerImpl handler) {
		MessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		handler.withCommand(command).from(localOrg).to(remoteOrg);
		
	}
	
	
	@Test
	public void should_create_the_message_with_the_supplied_command() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		MessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.withCommand(command);
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		assertEquals(command, capturedMessage.getValue().getCommand());
	}
	
	
	@Test
	public void should_create_message_with_the_correct_sender_and_recievers() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), null, getAdminLoader(), actionURLBuilder()).withCommand(new CreateSafetyNetworkConnectionMessageCommand());
		
		sut.from(localOrg).to(remoteOrg);
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		assertEquals(localOrg.getName(), capturedMessage.getValue().getSender());
		assertEquals(remoteOrg.getName(), capturedMessage.getValue().getReceiver());
	}
	
	
	@Test
	public void should_create_message_with_the_correct_personalized_body() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.personalizeBody("My personalized body.");
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		
		assertEquals("My personalized body.", capturedMessage.getValue().getBody());
	}
	
	
	
	@Test
	public void should_create_message_with_the_correct_with_the_provided_subject() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), "subject", getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		assertEquals("subject", capturedMessage.getValue().getSubject());
	}
	
	

	@Test
	public void should_set_notification_status_to_failed_if_an_exception_is_thrown_while_sending_the_notification_email() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		Notifier notifier = new Notifier() {
			@Override
			public boolean success(Notification notification) {
				return false;
			}
		};
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, notifier, "subject", getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		assertTrue(!sut.wasNotificationSent());
	}
	
	@Test
	public void should_when_the_email_notification_is_sent_successfully_the_notification_status_should_be_true() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, new NullNotifier(), "subject", getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		assertTrue(sut.wasNotificationSent());
	}
	
	
	@Test
	public void should_send_notification_of_the_invitation() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		TestSingleNotifier notifier = new TestSingleNotifier();
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, notifier, "subject", getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		assertNotNull(notifier.notification);
	}
	
	
	
	
	
	
	@Test
	public void should_send_notification_of_the_invitation_with_the_right_vaules_handed_to_the_template() throws Exception {
		Capture<Message> capturedMessage = new Capture<Message>();
		MessageSaver saver = createMessageSaverWithCapture(capturedMessage);
		
		TestSingleNotifier notifier = new TestSingleNotifier();
		
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(saver, notifier, "subject", getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		sut.personalizeBody("a personalized message");
		
		sut.create(new NoCommitAndRollBackTransaction());
		
		
		ConnectionInviteReceivedNotification expectedNotification = new ConnectionInviteReceivedNotification();
		expectedNotification.notifiyUser(adminUser);
		expectedNotification.setSubject("subject");
		expectedNotification.setCompanyName(localOrg.getName());
		expectedNotification.setMessage("a personalized message");
		//TODO this looks like a bad choice of responsibility.
		String domainAdjustedBaseUri = BASE_URI.replaceAll("n4", remoteOrg.getTenant().getName());
		expectedNotification.setMessageUrl(domainAdjustedBaseUri + "message.action?uniqueID=" + capturedMessage.getValue().getId());
		
		
		assertEquals(expectedNotification, notifier.notification);
		
	}
	
	
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_a_null_command() throws Exception {
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(null, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.withCommand(null);
		
		sut.create(new NoCommitAndRollBackTransaction());
	}

	private ActionURLBuilder actionURLBuilder() {
		return new ActionURLBuilder(URI.create(BASE_URI), new NonDataSourceBackedConfigContext());
	}
	
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_a_null_to_org() throws Exception {
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(null, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.to(null);
		
		sut.create(new NoCommitAndRollBackTransaction());
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_a_null_from_org() throws Exception {
		
		ConnectionInvitationHandlerImpl sut = new ConnectionInvitationHandlerImpl(null, new NullNotifier(), null, getAdminLoader(), actionURLBuilder());
		addRequiredFieldsToCreationHandler(sut);
		
		sut.from(null);
		
		sut.create(new NoCommitAndRollBackTransaction());
	}

	private AdminUserListLoader getAdminLoader() {
		AdminUserListLoader adminLoader = createMock(AdminUserListLoader.class);
		
		expect(adminLoader.load((Transaction)anyObject())).andReturn(new FluentArrayList<UserBean>(adminUser));
		replay(adminLoader);
		return adminLoader;
	}
	
	

	private MessageSaver createMessageSaverWithCapture(Capture<Message> capturedMessage) {
		MessageSaver saver = createMock(MessageSaver.class);
		saver.save((Transaction)anyObject(), capture(capturedMessage));
		expectLastCall().andAnswer(new IAnswer<Void>() {
			public Void answer() throws Throwable {
	             Message message = (Message) (getCurrentArguments()[1]);
	             message.setId(new Random().nextLong());
	             return null;
	        }
		});
		replay(saver);
		return saver;
	}
}
