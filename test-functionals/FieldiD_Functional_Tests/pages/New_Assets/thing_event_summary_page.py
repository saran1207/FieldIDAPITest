from PageObjectLibrary import PageObject

class ThingEventSummaryPage(PageObject):
    PAGE_URL = "/fieldid/w/thingEventSummary"

    _locators = {
        "print_link": "link:Print",
        "edit_button": "link:Edit",
        "summary_link": "link:Summary"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def click_edit_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.edit_button)
        self.se2lib.click_element(self.locator.edit_button)
        
    def click_summary_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.summary_link)
        self.se2lib.click_element(self.locator.summary_link)    