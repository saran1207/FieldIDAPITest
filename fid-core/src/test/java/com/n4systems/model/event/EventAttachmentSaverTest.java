package com.n4systems.model.event;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.model.builders.EventBuilder;
import com.n4systems.model.builders.FileAttachmentBuilder;
import com.n4systems.model.builders.SubEventBuilder;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.testutils.TestHelper;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class EventAttachmentSaverTest {
	private EventAttachmentSaver saver;
	private Event event;
	private SubEvent subEvent;
	private FileAttachment attachment;
	private byte[] attachmentData;
	
	@Before
	public void setup_test_config() {
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
	
	@Test
	public void test_saves_against_regular_event() throws IOException {

		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(event)).andReturn(event);
		EasyMock.replay(em);

        S3Service s3Service = EasyMock.createMock(S3Service.class);
        saver.setS3Service(s3Service);
        EasyMock.expect(s3Service.getFileAttachmentPath(attachment)).andReturn("");
        s3Service.uploadFileAttachmentData((byte[]) EasyMock.anyObject(), EasyMock.anyObject(FileAttachment.class));
        EasyMock.expectLastCall();
        EasyMock.replay(s3Service);
		
		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(event.getAttachments().contains(attachment));
		
		//PathHandler is getting deprecated verifyData(PathHandler.getEventAttachmentFile(event, attachment));
	}
	
	@Test
	public void test_saves_against_sub_event() throws IOException {
		saver.setSubEvent(subEvent);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(subEvent)).andReturn(subEvent);
		EasyMock.replay(em);

        S3Service s3Service = EasyMock.createMock(S3Service.class);
        saver.setS3Service(s3Service);
        EasyMock.expect(s3Service.getFileAttachmentPath(attachment)).andReturn("");
        s3Service.uploadFileAttachmentData((byte[]) EasyMock.anyObject(), EasyMock.anyObject(FileAttachment.class));
        EasyMock.expectLastCall();
        EasyMock.replay(s3Service);

		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(subEvent.getAttachments().contains(attachment));
	}
	
	@Test(expected=NotImplementedException.class)
	public void update_throws_exception() {
		saver.update(new DummyEntityManager(), attachment);
	}
	
	@Test(expected=NotImplementedException.class)
	public void remove_throws_exception() {
		saver.remove(new DummyEntityManager(), attachment);
	}
}
