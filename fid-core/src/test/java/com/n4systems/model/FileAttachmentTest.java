package com.n4systems.model;

import static com.n4systems.model.builders.FileAttachmentBuilder.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class FileAttachmentTest {

	
	@Test
	public void should_find_null_file_name_does_not_have_an_attached_file() throws Exception {
		FileAttachment sut = aFileAttachment().withFileName(null).build();
		assertFalse(sut.hasAttachedFile());
	}
	
	@Test
	public void should_find_an_empty_file_name_does_not_have_an_attached_file() throws Exception {
		FileAttachment sut = aFileAttachment().withFileName("").build();
		assertFalse(sut.hasAttachedFile());
	}
	
	
	@Test
	public void should_find_a_filled_in_file_name_to_have_an_attachments() throws Exception {
		FileAttachment sut = anImageFile().build();
		assertTrue(sut.hasAttachedFile());
	}
	
	
	@Test
	public void should_find_a_file_that_is_an_image_when_the_content_type_starts_with_image() {
		FileAttachment sut = new FileAttachmentWithContentTypeFetchingOverrided("image/jpeg");
		assertTrue(sut.isImage());
	}

	
	@Test
	public void should_find_a_file_that_is_not_an_image_when_the_content_type_does_not_starts_with_image() {
		FileAttachment sut = new FileAttachmentWithContentTypeFetchingOverrided("text/html");
		assertFalse(sut.isImage());
	}
	
	@Test
	public void should_find_a_file_that_is_not_an_image_when_the_content_type_has_image_in_it_but_not_at_the_start() {
		FileAttachment sut = new FileAttachmentWithContentTypeFetchingOverrided("application/image-proccessing");
		assertFalse(sut.isImage());
	}
	
	
	@Test
	public void should_find_attachment_with_no_file_is_not_an_image() {
		FileAttachment sut = aFileAttachmentWithNoFile().build();
		assertFalse(sut.isImage());
	}
	
	
	
	
	
	private class FileAttachmentWithContentTypeFetchingOverrided extends FileAttachment {
		
		private String contentType;
		
		public FileAttachmentWithContentTypeFetchingOverrided(String contentType) {
			super();
			this.contentType = contentType;
		}
		
		@Override
		public String getContentType(String fileName) {
			return contentType;
		}
		@Override
		public boolean hasAttachedFile() {
			return true;
		}
	}
}
