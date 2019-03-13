from PageObjectLibrary import PageObject

class ManageUsersPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/usersList"

    _locators = {
        "add_button": "link:Add"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_add_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_button)
        self.se2lib.click_element(self.locator.add_button)