package com.n4systems.taskscheduling;

import static org.junit.Assert.*;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.mail.MailManager;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.taskscheduling.task.DownloadTask;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.mail.TemplateMailMessage;

public class DownloadTaskTest {
	private static final String DOWNLOAD_URL = "some_url";
	private static final String MESSAGE_TEMPLATE = "template";
	private static final String USER_EMAIL = "email_addr";
	private static final String DOWNLOAD_NAME = "download_link";
	private static final Long DOWNLOAD_ID = 123L;
	
	class EmptyDownloadTask extends DownloadTask {
		public int genereateFileCount = 0;
		public EmptyDownloadTask(DownloadLink downloadLink, String downloadUrl, String templateName, DownloadLinkSaver linkSaver, MailManager mailManager) {
			super(downloadLink, downloadUrl, templateName, linkSaver, mailManager);
		}

		@Override
		protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
			genereateFileCount++;
			assertEquals(downloadLink.getFile(), downloadFile);
			assertSame(downloadLink.getUser(), user);
			assertEquals(downloadLink.getName(), downloadName);
		}
	}
	
	class FailingDownloadTask extends DownloadTask {
		private final Exception e = new Exception("Fail");
		public int sendFailureNotificationCount = 0;
		public int genereateFileCount = 0;
		
		public FailingDownloadTask(DownloadLink downloadLink, String downloadUrl, String templateName, DownloadLinkSaver linkSaver, MailManager mailManager) {
			super(downloadLink, downloadUrl, templateName, linkSaver, mailManager);
		}

		@Override
		protected void generateFile(File downloadFile, UserBean user, String downloadName) throws Exception {
			genereateFileCount++;
			throw e;
		}

		@Override
		protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink, Exception cause) throws MessagingException {
			sendFailureNotificationCount++;
			assertSame(this.downloadLink, downloadLink);
			assertSame(e, cause);
		}
	}
	
	class DownloadLinkTestSaver extends DownloadLinkSaver {
		private final DownloadState[] updateStates;
		public int updateCount = 0;
		
		public DownloadLinkTestSaver(DownloadState ... updateStates) {
			this.updateStates = updateStates;
		}
		
		@Override
		public DownloadLink update(DownloadLink entity) {
			assertEquals(updateStates[updateCount], entity.getState());
			updateCount++;
			return entity;
		}	
	}
	
	class SuccessMailManager implements MailManager {
		public int sendMessageCount = 0;
		@Override
		public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
			sendMessageCount++;

			assertTrue(mailMessage instanceof TemplateMailMessage);
			TemplateMailMessage tmm = (TemplateMailMessage)mailMessage;
			
			assertEquals(USER_EMAIL, tmm.getToAddresses().iterator().next());
			assertEquals(DOWNLOAD_NAME, tmm.getSubject());
			assertEquals(MESSAGE_TEMPLATE, tmm.getTemplatePath());
			assertEquals(link, tmm.getTemplateMap().get("downloadLink"));
			assertEquals(DOWNLOAD_URL + DOWNLOAD_ID, tmm.getTemplateMap().get("downloadUrl"));
		}
	}
	
	private final UserBean user;
	private final DownloadLink link;
	
	@SuppressWarnings("serial")
	public DownloadTaskTest() {
		user = UserBuilder.anEmployee().build();
		user.setEmailAddress(USER_EMAIL);
		
		link = new DownloadLink() {
			public File getFile(boolean createParents) {
				if (!createParents) {
					fail("DownloadLink getFile called with createParents = false");
				}
				return new File("some_file_path");
			}
		};
		
		link.setId(DOWNLOAD_ID);
		link.setName(DOWNLOAD_NAME);
		link.setContentType(ContentType.EXCEL);
		link.setState(DownloadState.REQUESTED);
		link.setUser(user);
	}
	
	@Test
	public void test_link_status_is_updated_and_success_message_is_constructed() throws NoSuchProviderException, MessagingException {
		DownloadLinkTestSaver linkSaver = new DownloadLinkTestSaver(DownloadState.INPROGRESS, DownloadState.COMPLETED);
		
		SuccessMailManager mail = new SuccessMailManager();
		
		EmptyDownloadTask downloadTask = new EmptyDownloadTask(link, DOWNLOAD_URL, MESSAGE_TEMPLATE, linkSaver, mail);
		downloadTask.run();
		
		assertEquals(1, downloadTask.genereateFileCount);
		assertEquals(2, linkSaver.updateCount);
		assertEquals(1, mail.sendMessageCount);
	}
	
	@Test
	public void test_link_status_is_updated() throws NoSuchProviderException, MessagingException {		
		DownloadLinkTestSaver linkSaver = new DownloadLinkTestSaver(DownloadState.INPROGRESS, DownloadState.FAILED);
		
		MailManager mail = EasyMock.createMock(MailManager.class);
		EasyMock.replay(mail);
		
		FailingDownloadTask downloadTask = new FailingDownloadTask(link, DOWNLOAD_URL, MESSAGE_TEMPLATE, linkSaver, mail);
		downloadTask.run();
		EasyMock.verify(mail);

		assertEquals(1, downloadTask.genereateFileCount);
		assertEquals(2, linkSaver.updateCount);
		assertEquals(1, downloadTask.sendFailureNotificationCount);
	}
	
	@Test
	public void failing_to_send_message_does_not_fail_download() throws NoSuchProviderException, MessagingException {
		DownloadLinkTestSaver linkSaver = new DownloadLinkTestSaver(DownloadState.INPROGRESS, DownloadState.COMPLETED);
		
		MailManager mail = new MailManager() {
			
			@Override
			public void sendMessage(MailMessage mailMessage) throws NoSuchProviderException, MessagingException {
				throw new MessagingException();
			}
		};
		
		EmptyDownloadTask downloadTask = new EmptyDownloadTask(link, DOWNLOAD_URL, MESSAGE_TEMPLATE, linkSaver, mail);
		downloadTask.run();
		
		assertEquals(DownloadState.COMPLETED, link.getState());
		
		assertEquals(1, downloadTask.genereateFileCount);
		assertEquals(2, linkSaver.updateCount);
	}
}
