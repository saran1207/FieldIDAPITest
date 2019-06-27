from Setup.Events.event_types_base_page import EventTypesBasePage

class EventTypeDeleteConfirmPage(EventTypesBasePage):
    PAGE_URL = "/fieldid/w/setup/eventTypeDeleteConfirm"
      
    _locators = {
        "delete_button": "//input[@value='Delete']"
     }
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
        
    def click_delete_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_button)
        self.se2lib.click_element(self.locator.delete_button)