package com.n4systems.model;

import static com.n4systems.model.builders.FileAttachmentBuilder.*;
import static com.n4systems.model.builders.InspectionBuilder.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.n4systems.test.helpers.FluentHashSet;


public class AbstractInspectionTest {
	
	@Test
	public void should_return_no_image_attachments_when_attachment_list_is_empty() throws Exception {
		AbstractInspection sut = anInspection().build();
		assertTrue(sut.getImageAttachments().isEmpty());
	}

	
	@Test
	public void should_return_same_list_when_all_attachments_are_images() throws Exception {
		Set<FileAttachment> attachments = new FluentHashSet<FileAttachment>(anImageFile().build(), anImageFile().build());
		AbstractInspection sut = anInspection().withAttachment(attachments).build();
		
		assertEquals(attachments, sut.getImageAttachments());
	}
	
	
	
	@Test
	public void should_return_filtered_list_when_attachments_are_mixed() throws Exception {
		Set<FileAttachment> expectedAttachments = new FluentHashSet<FileAttachment>(anImageFile().build(), anImageFile().build());
		
		Set<FileAttachment> attachments = new FluentHashSet<FileAttachment>(aNonImageFile().build()).stickOn(expectedAttachments);
		AbstractInspection sut = anInspection().withAttachment(attachments).build();
		
		assertEquals(expectedAttachments, sut.getImageAttachments());
	}
}
