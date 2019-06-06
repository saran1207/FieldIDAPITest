from PageObjectLibrary import PageObject

class EventTypeAssocationPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/selectEventTypes"
    
    _locators = {
        "save_button": "link:Save",
        "event_type_checkbox": "//td/label[text()='%s']/../../td/input"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_save_eventtype_association_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def click_event_type_checkbox(self, eventTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.event_type_checkbox % eventTypeName)
        self.se2lib.click_element(self.locator.event_type_checkbox % eventTypeName)
        
    def verify_if_event_type_checkbox_is_checked(self, eventTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.event_type_checkbox % eventTypeName)
        return self.se2lib.driver.find_element_by_xpath(self.locator.event_type_checkbox % eventTypeName).get_attribute("checked")=='true'