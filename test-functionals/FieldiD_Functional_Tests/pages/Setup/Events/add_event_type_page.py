from Setup.Events.event_types_base_page import EventTypesBasePage

class AddEventTypePage(EventTypesBasePage):
    PAGE_URL = "/fieldid/w/setup/eventTypeForm"
      
    _locators = {
        "save_button": "name:expandableSection:saveButton",
        "name_textbox": "name:name",
        "eventTypeGroup_dropdown": "//select[@name='eventTypeGroups']/option[text()='%s']",
        "printable_checkbox": "name:printable",
        "update_assigned_to_checkbox": "name:assignedToAvailableSection:assignedToAvailable",
        "delete_link": "link:Delete"
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
                
    def input_event_type_name(self, event_type_name):
        self.se2lib.wait_until_element_is_visible(self.locator.name_textbox)
        self.se2lib.input_text(self.locator.name_textbox, event_type_name)
            
    def click_printable_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.printable_checkbox)
        self.se2lib.click_element(self.locator.printable_checkbox)
            
    def click_update_assigned_to_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.update_assigned_to_checkbox)
        self.se2lib.click_element(self.locator.update_assigned_to_checkbox)
        
    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def click_delete_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_link)
        self.se2lib.click_element(self.locator.delete_link)