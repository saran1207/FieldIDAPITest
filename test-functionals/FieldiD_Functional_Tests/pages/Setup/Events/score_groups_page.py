from PageObjectLibrary import PageObject

class ScoreGroupsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/scoreGroups"
      
    _locators = {
        
        "score_group_name_textbox": "name:scoreGroupName",
        "save_score_group_button": "//input[@name='submitButton']",
        "save_edit_score_group_link": "link:Save",
        "view_score_group_link": "link:%s",
        "edit_score_group_link": "//a/span[text()='%s']/../../../div[@class='editCopyLinks']/a[text()='Edit']",
        "copy_score_group_link": "//a/span[text()='%s']/../../../div[@class='editCopyLinks']/a[text()='Copy']",
        "delete_score_group_img": "//a/span[text()='%s']/../../../../div[@class='deleteLinkSection']",
        "edit_score_group_textbox": "name:newText",
        "score_name_textbox": "name:newScore:scoreForm:name",
        "score_value_textbox": "name:newScore:scoreForm:value",
        "save_score_button": "name:saveButton",
        "score_name_label": "//div[@class='score']//a/span[text()='%s']",
                
     }
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def input_score_group_name(self, score_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.score_group_name_textbox)
        self.se2lib.input_text(self.locator.score_group_name_textbox, score_group_name)
        
    def input_edit_score_group_name(self, score_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.edit_score_group_textbox)
        self.se2lib.input_text(self.locator.edit_score_group_textbox, score_group_name)
            
    def click_save_score_group_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_score_group_button)
        self.se2lib.click_element(self.locator.save_score_group_button)
    
    def click_save_edit_score_group_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_edit_score_group_link)
        self.se2lib.click_element(self.locator.save_edit_score_group_link)
        
    def click_edit_score_group_link(self, score_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.edit_score_group_link % score_group_name)
        self.se2lib.click_element(self.locator.edit_score_group_link % score_group_name)
        
    def verify_if_score_group_is_added(self, score_group_name):
        return len(self.se2lib.driver.find_elements_by_xpath(self.locator.edit_score_group_link % score_group_name))>0 and len(self.se2lib.driver.find_elements_by_xpath(self.locator.copy_score_group_link % score_group_name))>0
    
    def verify_if_score_is_added(self, score_name):
        self.se2lib.wait_until_element_is_visible(self.locator.score_name_label % score_name)
        return len(self.se2lib.driver.find_elements_by_xpath(self.locator.score_name_label % score_name))>0 
               
    def input_score_name(self, score_name):
        self.se2lib.wait_until_element_is_visible(self.locator.score_name_textbox)
        self.se2lib.input_text(self.locator.score_name_textbox, score_name)
        
    def input_score_value(self, score_value):
        self.se2lib.wait_until_element_is_visible(self.locator.score_value_textbox)
        self.se2lib.input_text(self.locator.score_value_textbox, score_value)
        
    def click_save_score_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_score_button)
        self.se2lib.click_element(self.locator.save_score_button)
        
    def select_score_group(self, score_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.view_score_group_link % score_group_name)
        self.se2lib.click_element(self.locator.view_score_group_link % score_group_name)  
        
    def click_delete_score_group(self, score_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_score_group_img % score_group_name)
        self.se2lib.click_element(self.locator.delete_score_group_img % score_group_name)
        
           
    
            