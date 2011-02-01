package com.n4systems.fieldid.selenium.testcase.setup;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.fieldid.selenium.FieldIDTestCase;
import com.n4systems.fieldid.selenium.pages.setup.ManageCommentTemplatesPage;

public class ManageCommentTemplatesTest extends FieldIDTestCase {
	
	private static final String TEMPLATE_NAME_THAT_IS_TOO_SHORT = "";
	private static final String TEST_TEMPLATE_NAME = "SelTest-";
	private static final String TEST_TEMPLATE_COMMENT = "My Test Template Comment";
	private ManageCommentTemplatesPage templatesPage;
	
	@Before
	public void setupDrivers() throws Exception {
		templatesPage = startAsCompany("test1").login().clickSetupLink().clickManageCommentTemplates();
		removeTestTemplates();
	}
	
	@Test
	public void should_allow_the_creation_editing_and_removal_of_a_comment_template() throws Exception {
		templatesPage.clickAddTab();
		
		String testTemplateName = TEST_TEMPLATE_NAME;
		addTemplate(testTemplateName, TEST_TEMPLATE_COMMENT);
		
		assertTrue("Template should have been created", templatesPage.getCommentTemplateNames().contains(testTemplateName));
		
		templatesPage.clickEditOnTemplate(testTemplateName);
		assertEquals(testTemplateName, templatesPage.getTemplateName());
		assertEquals(TEST_TEMPLATE_COMMENT, templatesPage.getTemplateComment());
		
		templatesPage.enterTemplateName(testTemplateName+"EDIT");
		templatesPage.enterTemplateComment(TEST_TEMPLATE_COMMENT+"EDIT");
		templatesPage.clickSaveButton();
		
		assertTrue("Edited template name should be present", templatesPage.getCommentTemplateNames().contains(testTemplateName+"EDIT"));
		templatesPage.removeTemplateNamed(testTemplateName+"EDIT");
		assertFalse(templatesPage.getCommentTemplateNames().contains(testTemplateName+"EDIT"));
	}
	
	@Test
	public void should_require_a_new_comment_template_to_have_name() throws Exception {
		templatesPage.clickAddTab();
		templatesPage.enterTemplateName(TEMPLATE_NAME_THAT_IS_TOO_SHORT);
		templatesPage.clickSaveButton();
		
		assertThat(templatesPage.getValidationErrorFor(ManageCommentTemplatesPage.FIELD_TEMPLATE_NAME), containsString("required"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_require_an_edited_comment_template_to_have_name() throws Exception {
		templatesPage.clickAddTab();
		
		String testTemplateName = TEST_TEMPLATE_NAME;
		addTemplate(testTemplateName, TEST_TEMPLATE_COMMENT);
		
		templatesPage.clickEditOnTemplate(testTemplateName);
		templatesPage.enterTemplateName(TEMPLATE_NAME_THAT_IS_TOO_SHORT);
		templatesPage.clickSaveButton();
		
		assertThat(templatesPage.getValidationErrorFor(ManageCommentTemplatesPage.FIELD_TEMPLATE_NAME), allOf(containsString("required"), containsString("name")));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_not_allow_two_comment_template_to_have_the_same_name() throws Exception {
		templatesPage.clickAddTab();
		
		String testTemplateName = TEST_TEMPLATE_NAME;
		addTemplate(testTemplateName, TEST_TEMPLATE_COMMENT);
		
		templatesPage.clickAddTab();
		addTemplate(testTemplateName, TEST_TEMPLATE_COMMENT);
		
		assertThat(templatesPage.getValidationErrorFor(ManageCommentTemplatesPage.FIELD_TEMPLATE_NAME), allOf(containsString("duplicate"), containsString("name")));
	}
	
	private void removeTestTemplates() {
		List<String> templateNames = templatesPage.getCommentTemplateNames();
		for (String templateName : templateNames) {
			if (templateName.startsWith(TEST_TEMPLATE_NAME)) {
				templatesPage.removeTemplateNamed(templateName);
				assertFalse("Test template needs to be removed", templatesPage.getCommentTemplateNames().contains(templateName));
			}
		}
	}
	
	private void addTemplate(String templateName, String templateComment) {
		templatesPage.enterTemplateName(templateName);
		templatesPage.enterTemplateComment(templateComment);
		templatesPage.clickSaveButton();
	}
	
}
