from PageObjectLibrary import PageObject

class NewAssetWithOrderPage(PageObject):
    PAGE_URL = "fieldid/w/assetImport"

    _locators = {
        "add_tab": "link:Add",
        "rfid_number_field": "//input[@name='singleIdentifyContainer:rfidContainer:rfidNumber']",
        "save_button": "//input[@name='actionsContainer:saveButton']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_add_tab(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_tab)
        self.se2lib.click_element(self.locator.add_tab)