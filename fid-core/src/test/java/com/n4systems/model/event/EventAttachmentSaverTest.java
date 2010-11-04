package com.n4systems.model.event;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.SubEventBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.builders.FileAttachmentBuilder;
import com.n4systems.reporting.PathHandler;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.testutils.TestConfigContext;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class EventAttachmentSaverTest {
	private File appRoot;
	private EventAttachmentSaver saver;
	private Event event;
	private SubEvent subEvent;
	private FileAttachment attachment;
	private byte[] attachmentData;
	
	private void setup_config_context() {
		appRoot = new File(System.getProperty("java.io.tmpdir"), TestHelper.randomString());
		
		TestConfigContext.newContext().setEntry(ConfigEntry.GLOBAL_APPLICATION_ROOT, appRoot.getPath());
		assertEquals("ConfigContext isn't setup properly,", appRoot, ConfigContext.getCurrentContext().getAppRoot());
	}
	
	@Before
	public void setup_test_config() {
		setup_config_context();
		
		attachment = FileAttachmentBuilder.aFileAttachment().build();
		attachmentData = TestHelper.randomString().getBytes();
		
		subEvent = SubEventBuilder.aSubEvent("bleh").build();
		subEvent.setCreated(new Date());
		subEvent.setTenant(attachment.getTenant());
		
		event = EventBuilder.anEvent().withSubEvents(Arrays.asList(subEvent)).build();
		event.setCreated(subEvent.getCreated());
		event.setTenant(attachment.getTenant());
		
		saver = new EventAttachmentSaver();
		saver.setData(attachmentData);
		saver.setEvent(event);
	}
	
	@After
	public void cleanup_temp_files_and_config_context() throws IOException {
		if (appRoot.exists()) {
			FileUtils.deleteDirectory(appRoot);
		}
		TestConfigContext.resetToDefaultContext();
	}
	
	@Test
	public void test_saves_against_regular_event() throws IOException {
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(event)).andReturn(event);
		EasyMock.replay(em);
		
		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(event.getAttachments().contains(attachment));
		
		verifyData(PathHandler.getEventAttachmentFile(event, attachment));
	}
	
	@Test
	public void test_saves_against_sub_event() throws IOException {
		saver.setSubEvent(subEvent);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(subEvent)).andReturn(subEvent);
		EasyMock.replay(em);
		
		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(subEvent.getAttachments().contains(attachment));
		
		verifyData(PathHandler.getEventAttachmentFile(event, subEvent, attachment));
	}
	
	@Test(expected=NotImplementedException.class)
	public void update_throws_exception() {
		saver.update(new DummyEntityManager(), attachment);
	}
	
	@Test(expected=NotImplementedException.class)
	public void remove_throws_exception() {
		saver.remove(new DummyEntityManager(), attachment);
	}
	
	private void verifyData(File attachment) throws IOException {
		InputStream in = null;
		byte[] writtenData = null;
		
		try {
			in = new FileInputStream(attachment);
			writtenData = IOUtils.toByteArray(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		assertArrayEquals(attachmentData, writtenData);
	}
}
