package com.n4systems.fieldid.validators;

import static com.google.common.collect.Lists.*;
import static com.n4systems.model.builders.FileAttachmentBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.n4systems.model.FileAttachment;


public class AvailableFileNameValidatorTest {

	@Test
	public void should_handle_there_being_nulls_in_the_attachment_list() throws Exception {
		List<FileAttachment> existingFileAttachments = newArrayList(null, aFileAttachment().build());
		List<FileAttachment> uploadedFileAttachments = newArrayList(aFileAttachment().build());
		
		
		AvailableFileNameValidator sut = new AvailableFileNameValidator(existingFileAttachments, uploadedFileAttachments);
		sut.isFileNameAvailable(aFileAttachment().withFileName("someName").build());
	}
	
	@Test
	public void should_handle_there_being_nulls_in_the_upload_list() throws Exception {
		List<FileAttachment> existingFileAttachments = newArrayList(aFileAttachment().build());
		List<FileAttachment> uploadedFileAttachments = newArrayList(null, null, aFileAttachment().build());

		
		AvailableFileNameValidator sut = new AvailableFileNameValidator(existingFileAttachments, uploadedFileAttachments);
		sut.isFileNameAvailable(aFileAttachment().withFileName("someName").build());
	}
	
	@Test
	public void should_find_that_the_file_is_available_if_the_only_comes_up_once() throws Exception {
		List<FileAttachment> existingFileAttachments = newArrayList(aFileAttachment().build());
		FileAttachment uploadedFile = aFileAttachment().withFileName("someName").build();
		
		List<FileAttachment> uploadedFileAttachments = newArrayList(uploadedFile);
		
		
		AvailableFileNameValidator sut = new AvailableFileNameValidator(existingFileAttachments, uploadedFileAttachments);
		assertThat(sut.isFileNameAvailable(uploadedFile), is(true));
	}
	
	@Test
	public void should_find_the_file_name_is_not_available_when_is_used_by_an_existing_uploaded_attachment() throws Exception {
		FileAttachment existingFileAttachment = aFileAttachment().withFileName("someName").build();
		FileAttachment uploadedFileWithSameNameAsExisting = aFileAttachment().withFileName("someName").build();
		
		List<FileAttachment> existingFileAttachments = newArrayList(existingFileAttachment);
		List<FileAttachment> uploadedFileAttachments = newArrayList(uploadedFileWithSameNameAsExisting);
		
		
		AvailableFileNameValidator sut = new AvailableFileNameValidator(existingFileAttachments, uploadedFileAttachments);
		assertThat(sut.isFileNameAvailable(uploadedFileWithSameNameAsExisting), is(false));
	}
	
	@Test
	public void should_find_two_newly_uploaded_attachment_with_the_same_file_name_are_both_not_available() throws Exception {
		FileAttachment firstUploadedFile = aFileAttachment().withFileName("someName").build();
		FileAttachment secondUploadedFileWithSameNameAsExisting = aFileAttachment().withFileName("someName").build();
		
		List<FileAttachment> existingFileAttachments = newArrayList(aFileAttachment().build());
		List<FileAttachment> uploadedFileAttachments = newArrayList(firstUploadedFile, secondUploadedFileWithSameNameAsExisting);
		
		
		AvailableFileNameValidator sut = new AvailableFileNameValidator(existingFileAttachments, uploadedFileAttachments);
		
		assertThat(sut.isFileNameAvailable(firstUploadedFile), is(false));
		assertThat(sut.isFileNameAvailable(secondUploadedFileWithSameNameAsExisting), is(false));
	}
	
	
}
