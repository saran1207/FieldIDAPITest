from PageObjectLibrary import PageObject

class QuickEventPage(PageObject):
    PAGE_URL = "/fieldid/w/quickEvent"

    _locators = {
        "unscheduled_event_link": "link:%s",
        "start_event_link": "name:startEventLink"
        
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_unscheduled_event_link(self, event_name):
        self.se2lib.wait_until_element_is_visible(self.locator.unscheduled_event_link % event_name)
        self.se2lib.click_element(self.locator.unscheduled_event_link % event_name)
        
    def click_start_event_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.start_event_link)
        self.se2lib.click_element(self.locator.start_event_link)