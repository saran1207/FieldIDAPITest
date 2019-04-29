from PageObjectLibrary import PageObject

class AssetSummaryPage(PageObject):
    PAGE_URL = "/fieldid/w/assetSummary"

    _locators = {
        "events_button": "name:eventHistoryLink",
        "start_event_link": "name:startEventLink",
        "summary_button":  "name:summaryLink"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_events_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.events_button)
        self.se2lib.click_element(self.locator.events_button)
        
    def click_start_event_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.start_event_link)
        self.se2lib.click_element(self.locator.start_event_link)
        
    def wait_for_summary_button(self):
         self.se2lib.wait_until_element_is_visible(self.locator.summary_button)