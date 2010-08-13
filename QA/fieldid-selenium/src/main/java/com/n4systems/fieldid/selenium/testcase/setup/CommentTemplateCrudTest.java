package com.n4systems.fieldid.selenium.testcase.setup;


import static com.n4systems.fieldid.selenium.datatypes.CommentTemplate.randomStatusComment;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.administration.page.ManageCommentTemplatesDriver;
import com.n4systems.fieldid.selenium.datatypes.CommentTemplate;
import com.n4systems.fieldid.selenium.lib.LoggedInTestCase;

public class CommentTemplateCrudTest extends LoggedInTestCase {
	
	private static final String TEMPLATE_NAME_THAT_IS_TOO_SHORT = "";
	private ManageCommentTemplatesDriver driver;

	@Before
	public void setupDrivers() throws Exception {
		driver = systemDriverFactory.createCommentTemplateDriver();
	}
	
	@Test
	public void should_allow_the_creation_editing_and_removal_of_a_comment_template() throws Exception {
		CommentTemplate template = CommentTemplate.aVaildCommentTemplate();
		CommentTemplate editedTemplate = CommentTemplate.aVaildCommentTemplate();
		
		driver.gotoAddTemplate();
		
		driver.createTemplate(template);
		
		driver.assertTemplateWasCreated(template);
		
		driver.editTemplate(template, editedTemplate);
	
		driver.assertTemplateWasEdited(editedTemplate, template);
		
		driver.removeTemplate(editedTemplate);
		
		driver.assertTemplateWasRemoved(editedTemplate);
	}
	
	@Test
	public void should_require_a_new_comment_template_to_have_name() throws Exception {
		CommentTemplate invalidNameInTempalte = new CommentTemplate(TEMPLATE_NAME_THAT_IS_TOO_SHORT, randomStatusComment());
		
		driver.gotoAddTemplate();
		
		driver.createTemplate(invalidNameInTempalte);
		
		driver.assertVaildationErrorFor(ManageCommentTemplatesDriver.FieldName.NAME_FIELD, containsString("required"));
	}
	
	@Test
	public void should_require_an_edited_comment_template_to_have_name() throws Exception {
		CommentTemplate invalidNameInTempalte = new CommentTemplate(TEMPLATE_NAME_THAT_IS_TOO_SHORT, randomStatusComment());
		
		CommentTemplate template = driver.selectAnExistingTemplate();
		
		driver.editTemplate(template, invalidNameInTempalte);
		
		driver.assertVaildationErrorFor(ManageCommentTemplatesDriver.FieldName.NAME_FIELD, allOf(containsString("required"), containsString("name")));
	}
	
	@Test
	public void should_not_allow_two_comment_template_to_have_the_same_name() throws Exception {
		CommentTemplate existingTemplate = driver.selectAnExistingTemplate();
		
		driver.gotoAddTemplate();
		
		driver.createTemplate(existingTemplate);
		
		driver.assertVaildationErrorFor(ManageCommentTemplatesDriver.FieldName.NAME_FIELD, allOf(containsString("duplicate"), containsString("name")));
	}
	
}
