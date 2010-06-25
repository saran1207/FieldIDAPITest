package com.n4systems.fieldid.selenium.administration.page;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;

import com.n4systems.fieldid.selenium.data.CommentTemplate;
import com.n4systems.fieldid.selenium.lib.FieldIdSelenium;

public class ManageCommentTemplatesDriver {
	public enum FieldName {
		NAME_FIELD(NAME_FIELD_LOCATOR);
		
		private String inputId;
		
		private FieldName(String inputId) {
			this.inputId = inputId;
			
		}
	}
	
	private static final String SAVE_BUTTON_LOCATOR = "commentTemplateEdit_hbutton_save";
	private static final String ADD_ACTION_LINK_LOCATOR = "css=.options .add a";
	private static final String NAME_FIELD_LOCATOR = "commentTemplateEdit_name";
	private static final String COMMENT_FIELD_LOCATOR = "commentTemplateEdit_comment";
	
	
	
	private final FieldIdSelenium selenium;
	

	public ManageCommentTemplatesDriver(FieldIdSelenium selenium) {
		this.selenium = selenium;
	}


	public void gotoCommentTemplates() {
		selenium.open("/fieldid/commentTemplateList.action");
	}

	public void gotoAddTemplate() {
		gotoCommentTemplates();
		selenium.clickAndWaitForPageLoad(ADD_ACTION_LINK_LOCATOR);
	}

	public void assertTemplateWasCreated(CommentTemplate template) {
		assertTemplateValuesHaveBeenSaved(template);
	}


	private void assertTemplateValuesHaveBeenSaved(CommentTemplate template) {
		selectTemplateEditPage(template);
		assertEquals(template.comment, selenium.getValue(COMMENT_FIELD_LOCATOR));
		assertEquals(template.name, selenium.getValue(NAME_FIELD_LOCATOR));
	}

	public void createTemplate(CommentTemplate template) {
		fillInTemplateFormAndSave(template);
	}


	private void fillInTemplateFormAndSave(CommentTemplate template) {
		selenium.type(NAME_FIELD_LOCATOR, template.name);
		selenium.type(COMMENT_FIELD_LOCATOR, template.comment);
		selenium.clickAndWaitForPageLoad(SAVE_BUTTON_LOCATOR);
	}


	public void removeTemplate(CommentTemplate template) {
		gotoCommentTemplates();
		selenium.chooseOkOnNextConfirmation();
		selenium.click("css=td:contains('" + template.name + "') + td a:contains('Remove')");
		
		assertThat(selenium.getConfirmation(), startsWith("Are you sure you want to delete this?"));
		selenium.waitForPageToLoad();
	}


	public void assertTemplateWasRemoved(CommentTemplate template) {
		assertFalse(selenium.isTextPresent(template.name));
	}


	public void editTemplate(CommentTemplate originalTemplate, CommentTemplate editedTemplate) {
		gotoCommentTemplates();
		selectTemplateEditPage(originalTemplate);
		fillInTemplateFormAndSave(editedTemplate);
	}


	private void selectTemplateEditPage(CommentTemplate template) {
		assertTrue("comment template " + template.name + " does not appear in the list", selenium.isTextPresent(template.name));
		selenium.clickAndWaitForPageLoad("css=td:contains('" + template.name + "') + td a:contains('Edit')");
	}


	public void assertTemplateWasEdited(CommentTemplate editedTemplate, CommentTemplate originalTemplate) {
		assertFalse("original template " + originalTemplate.name + " does still appears in the list", selenium.isTextPresent(originalTemplate.name));
		
		assertTemplateValuesHaveBeenSaved(editedTemplate);
	}


	public void assertVaildationErrorFor(FieldName field, Matcher<String> messageMatcher) {
		String fieldErrorLocator = "css=*[errorfor='" + field.inputId + "']";
		assertThat("there is no error message for field " + field.name(), selenium.isElementPresent(fieldErrorLocator), is(true));
		
		assertThat(selenium.getAttribute(fieldErrorLocator + "@class"), containsString("errorMessage"));
		assertThat(selenium.getText(fieldErrorLocator), messageMatcher);
		
	}


	public CommentTemplate selectAnExistingTemplate() {
		gotoCommentTemplates();
		selenium.clickAndWaitForPageLoad("css=a:contains('Edit')");
		return new CommentTemplate(selenium.getValue(NAME_FIELD_LOCATOR), selenium.getValue(COMMENT_FIELD_LOCATOR));
	}
}
