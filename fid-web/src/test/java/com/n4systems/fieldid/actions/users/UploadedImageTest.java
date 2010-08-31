package com.n4systems.fieldid.actions.users;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;


public class UploadedImageTest {

	@Test
	public void should_find_that_an_image_exists_when_a_current_file_exists() throws Exception {
		UploadedImage sut = new UploadedImage();
		sut.setImage(new File("someFile"));
		assertTrue(sut.isExistingImage());
	}
	
	@Test
	public void should_find_that_an_image_does_not_exists_when_a_current_file_does_not_exist() throws Exception {
		UploadedImage sut = new UploadedImage();
		assertFalse(sut.isExistingImage());
	}
	
	
	@Test
	public void should_find_that_an_image_exists_when_a_there_is_a_non_empty_uploaded_file() throws Exception {
		UploadedImage sut = new UploadedImage();
		sut.setUploadDirectory("somer/field/place");
		assertTrue(sut.isExistingImage());
	}
	
	@Test
	public void should_find_that_an_image_does_not_exists_when_there_is_a_empty_uploaded_file() throws Exception {
		UploadedImage sut = new UploadedImage();
		sut.setUploadDirectory("");
		assertFalse(sut.isExistingImage());
	}
	
}
