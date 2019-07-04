from PageObjectLibrary import PageObject

class ObservationGroupsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/observationCountGroups"
      
    _locators = {
        
        "observation_group_name_textbox": "//form[@class='newScoreGroupForm']/input",
        "save_observation_group_button": "link:Save",
        "save_edit_observation_group_link": "//div[@class='editCopyLinks']/a[text()='Save']",
        "view_observation_group_link": "link:%s",
        "edit_observation_group_link": "//a/span[text()='%s']/../../../div[@class='editCopyLinks']/a[text()='Edit']",
        "copy_observation_group_link": "//a/span[text()='%s']/../../../div[@class='editCopyLinks']/a[text()='Copy']",
        "delete_observation_group_img": "//a/span[text()='%s']/../../../../div[@class='deleteLinkSection']",
        "edit_observation_group_textbox": "name:newText",
        "score_name_textbox": "name:newObservationCount:observationCountForm:name",
        "add_score_button": "link:Add",
        "score_name_label": "//div[@class='score']//a/span[text()='%s']",
        "save_score_button": "//div[@class='saveLinkContainer']/a",
        "view_all_link": "link:View All"
        
        
     }
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def select_event_type_group(self, event_group):
        if event_group != "":
            self.se2lib.driver.find_element_by_xpath(self.locator.eventTypeGroup_dropdown % event_group).click()
                
    def input_observation_group_name(self, observation_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.observation_group_name_textbox)
        self.se2lib.input_text(self.locator.observation_group_name_textbox, observation_group_name)
            
    def click_save_observation_group_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_observation_group_button)
        self.se2lib.click_element(self.locator.save_observation_group_button)
        
    def verify_if_observation_group_is_added(self, observation_group_name):
        print(len(self.se2lib.driver.find_elements_by_xpath(self.locator.copy_observation_group_link % observation_group_name)))
        return len(self.se2lib.driver.find_elements_by_xpath(self.locator.edit_observation_group_link % observation_group_name))>0 and len(self.se2lib.driver.find_elements_by_xpath(self.locator.copy_observation_group_link % observation_group_name))>0
               
    def input_score_name(self, score_name):
        self.se2lib.wait_until_element_is_visible(self.locator.score_name_textbox)
        self.se2lib.input_text(self.locator.score_name_textbox, score_name)
        
    def click_add_score_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_score_button)
        self.se2lib.click_element(self.locator.add_score_button)
        
    def click_save_score_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_score_button)
        self.se2lib.click_element(self.locator.save_score_button)
        
    def click_view_all_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.view_all_link)
        self.se2lib.click_element(self.locator.view_all_link)
        
    def select_observation_group(self, observation_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.view_observation_group_link % observation_group_name)
        self.se2lib.click_element(self.locator.view_observation_group_link % observation_group_name)  
        
    def click_delete_observation_group(self, observation_group_name):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_observation_group_img % observation_group_name)
        self.se2lib.click_element(self.locator.delete_observation_group_img % observation_group_name)
        
           
    
            