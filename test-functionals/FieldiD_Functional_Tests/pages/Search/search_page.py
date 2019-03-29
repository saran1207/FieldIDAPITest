from PageObjectLibrary import PageObject

class SearchPage(PageObject):
    PAGE_URL = "/fieldid/w/search"

    _locators = {
        "serial_number_field": "xpath=//input[@name='filters:identifiers:containedPanel:identifier']",
        "search_button": "xpath=//a[@name='submitSearch']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_serial_number(self, serialNumber):
        self.se2lib.wait_until_element_is_visible(self.locator.serial_number_field)
        self.se2lib.input_text(self.locator.serial_number_field, serialNumber)


    def click_search_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.search_button)
        self.se2lib.click_element(self.locator.search_button)