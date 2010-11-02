package com.n4systems.model;

import static com.n4systems.model.builders.FileAttachmentBuilder.*;
import static com.n4systems.model.builders.EventBuilder.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.test.helpers.FluentArrayList;


public class AbstractInspectionTest {
	
	@Test
	public void should_return_no_image_attachments_when_attachment_list_is_empty() throws Exception {
		AbstractEvent sut = anEvent().build();
		assertTrue(sut.getImageAttachments().isEmpty());
	}

	
	@Test
	public void should_return_same_list_when_all_attachments_are_images() throws Exception {
		
		List<FileAttachment> attachments = new FluentArrayList<FileAttachment>(anImageFile().build(), anImageFile().build());
		AbstractEvent sut = anEvent().withAttachment(attachments).build();
		
		assertEquals(attachments, sut.getImageAttachments());
	}
	
	
	
	@Test
	public void should_return_filtered_list_when_attachments_are_mixed() throws Exception {
		List<FileAttachment> expectedAttachments = new FluentArrayList<FileAttachment>(anImageFile().build(), anImageFile().build());
		
		List<FileAttachment> attachments = new FluentArrayList<FileAttachment>(aNonImageFile().build()).stickOn(expectedAttachments);
		AbstractEvent sut = anEvent().withAttachment(attachments).build();
		
		assertEquals(expectedAttachments, sut.getImageAttachments());
	}
}
