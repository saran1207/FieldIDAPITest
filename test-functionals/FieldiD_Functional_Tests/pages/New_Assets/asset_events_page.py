from PageObjectLibrary import PageObject

class AssetEventsPage(PageObject):
    PAGE_URL = "/fieldid/w/assetEvents"

    _locators = {
        "summary_button": "name:summaryLink",
        "start_event_link": "name:startEventLink",
        "view_button": "link:View",
        "toggle_button": "class:dropdown-menu-container",
        "edit_link": "link:Edit",
        "due_link": "link:Due"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_view_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.view_button)
        self.se2lib.click_element(self.locator.view_button)
        
    def click_edit_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.edit_link)
        self.se2lib.click_element(self.locator.edit_link)
        
    def click_due_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.due_link)
        self.se2lib.click_element(self.locator.due_link)